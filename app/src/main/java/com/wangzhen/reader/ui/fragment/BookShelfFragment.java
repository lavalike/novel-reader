package com.wangzhen.reader.ui.fragment;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wangzhen.reader.R;
import com.wangzhen.reader.model.bean.CollBookBean;
import com.wangzhen.reader.model.local.BookRepository;
import com.wangzhen.reader.ui.activity.ReadActivity;
import com.wangzhen.reader.ui.adapter.CollBookAdapter;
import com.wangzhen.reader.ui.base.BaseFragment;

import butterknife.BindView;

/**
 * Created by wangzhen on 17-4-15.
 */

public class BookShelfFragment extends BaseFragment {
    @BindView(R.id.book_shelf_rv_content)
    RecyclerView mRvContent;

    private CollBookAdapter mCollBookAdapter;

    @Override
    protected int getContentId() {
        return R.layout.fragment_bookshelf;
    }

    @Override
    protected void initWidget(Bundle savedInstanceState) {
        super.initWidget(savedInstanceState);
        setUpAdapter();
    }

    private void setUpAdapter() {
        mCollBookAdapter = new CollBookAdapter();
        mCollBookAdapter.setOnItemClickListener((view, pos) -> {
            CollBookBean collBook = mCollBookAdapter.getItem(pos);
            ReadActivity.startActivity(requireContext(), collBook, true);
        });
        mRvContent.setLayoutManager(new LinearLayoutManager(getContext()));
        mRvContent.setAdapter(mCollBookAdapter);
        mCollBookAdapter.refreshItems(BookRepository.getInstance().getCollBooks());
    }

    @Override
    public void onResume() {
        super.onResume();
        setUpAdapter();
    }
}
