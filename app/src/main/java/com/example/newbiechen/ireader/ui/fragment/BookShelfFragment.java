package com.example.newbiechen.ireader.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.newbiechen.ireader.R;
import com.example.newbiechen.ireader.model.bean.CollBookBean;
import com.example.newbiechen.ireader.model.local.BookRepository;
import com.example.newbiechen.ireader.ui.activity.ReadActivity;
import com.example.newbiechen.ireader.ui.adapter.CollBookAdapter;
import com.example.newbiechen.ireader.ui.base.BaseFragment;

import butterknife.BindView;

/**
 * Created by newbiechen on 17-4-15.
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
        mRvContent.setLayoutManager(new LinearLayoutManager(getContext()));
        mRvContent.setAdapter(mCollBookAdapter);
        mCollBookAdapter.refreshItems(BookRepository.getInstance().getCollBooks());
    }

    @Override
    protected void initClick() {
        super.initClick();
        mCollBookAdapter.setOnItemClickListener(
                (view, pos) -> {
                    CollBookBean collBook = mCollBookAdapter.getItem(pos);
                    ReadActivity.startActivity(requireContext(), collBook, true);
                }
        );
    }
}
