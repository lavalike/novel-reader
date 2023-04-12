package com.wangzhen.reader.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wangzhen.adapter.base.RecyclerItem;
import com.wangzhen.reader.R;
import com.wangzhen.reader.databinding.FragmentLocalBookBinding;
import com.wangzhen.reader.model.local.BookRepository;
import com.wangzhen.reader.ui.adapter.FileSystemAdapter;
import com.wangzhen.reader.utils.media.MediaStoreHelper;
import com.wangzhen.reader.widget.RefreshLayout;

/**
 * LocalBookFragment
 * Created by wangzhen on 2023/4/12
 */
public class LocalBookFragment extends BaseFileFragment {
    private FragmentLocalBookBinding binding;
    RefreshLayout mRlRefresh;
    RecyclerView mRvContent;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLocalBookBinding.inflate(inflater);
        mRlRefresh = binding.refreshLayout;
        mRvContent = binding.localBookRvContent;
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpAdapter();
        loadFiles();
    }

    private void setUpAdapter() {
        mAdapter = new FileSystemAdapter(null);
        mAdapter.setEmpty(new RecyclerItem() {
            @Override
            protected int layout() {
                return R.layout.layout_file_system_empty;
            }
        }.onCreateView(binding.getRoot()));
        mAdapter.setOnClickCallback((v, pos) -> {
            //如果是已加载的文件，则点击事件无效。
            String id = mAdapter.getDatas().get(pos).getAbsolutePath();
            if (BookRepository.getInstance().getCollBook(id) != null) {
                return;
            }

            //点击选中
            mAdapter.setCheckedItem(pos);

            //反馈
            if (mListener != null) {
                mListener.onItemCheckedChange(mAdapter.getItemIsChecked(pos));
            }
        });
        mRvContent.setLayoutManager(new LinearLayoutManager(getContext()));
        mRvContent.setAdapter(mAdapter);
    }

    private void loadFiles() {
        MediaStoreHelper.getAllBookFile(getActivity(), (files) -> {
            if (files.isEmpty()) {
                mRlRefresh.showEmpty();
            } else {
                mAdapter.setData(files);
                mRlRefresh.showFinish();
                if (mListener != null) {
                    mListener.onCategoryChanged();
                }
            }
        });
    }
}
