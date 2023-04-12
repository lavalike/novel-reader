package com.wangzhen.reader.ui.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wangzhen.reader.databinding.FragmentBookshelfBinding;
import com.wangzhen.reader.model.bean.CollBookBean;
import com.wangzhen.reader.model.local.BookRepository;
import com.wangzhen.reader.ui.activity.ReadActivity;
import com.wangzhen.reader.ui.adapter.CollBookAdapter;
import com.wangzhen.reader.ui.base.BaseFragment;

import java.util.Locale;

/**
 * Created by wangzhen on 17-4-15.
 */

public class BookShelfFragment extends BaseFragment {
    private FragmentBookshelfBinding binding;
    private RecyclerView mRvContent;

    private CollBookAdapter mCollBookAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentBookshelfBinding.inflate(inflater);
        mRvContent = binding.bookShelfRvContent;
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpAdapter();
    }

    private void setUpAdapter() {
        mCollBookAdapter = new CollBookAdapter();
        mCollBookAdapter.setOnItemClickListener((view, pos) -> {
            CollBookBean collBook = mCollBookAdapter.getItem(pos);
            ReadActivity.startActivity(requireContext(), collBook);
        });
        mCollBookAdapter.setOnItemLongClickListener((view, pos) -> {
            CollBookBean book = mCollBookAdapter.getItem(pos);
            deleteBook(book);
            return false;
        });
        mRvContent.setLayoutManager(new LinearLayoutManager(getContext()));
        mRvContent.setAdapter(mCollBookAdapter);
        mCollBookAdapter.refreshItems(BookRepository.getInstance().getCollBooks());
    }

    private void deleteBook(CollBookBean book) {
        new AlertDialog.Builder(requireContext()).setMessage(String.format(Locale.getDefault(), "确定删除%s吗", book.getTitle())).setNegativeButton("取消", null).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                BookRepository.getInstance().deleteBook(book);
                Toast.makeText(getContext(), book.getTitle() + "已删除", Toast.LENGTH_SHORT).show();
                setUpAdapter();
            }
        }).create().show();
    }

    @Override
    public void onResume() {
        super.onResume();
        setUpAdapter();
    }
}
