package com.wangzhen.reader.ui.fragment;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wangzhen.reader.R;
import com.wangzhen.reader.model.local.BookRepository;
import com.wangzhen.reader.ui.adapter.FileSystemAdapter;
import com.wangzhen.reader.utils.media.MediaStoreHelper;
import com.wangzhen.reader.widget.RefreshLayout;

import butterknife.BindView;

/**
 * Created by wangzhen on 17-5-27.
 * 本地书籍
 */

public class LocalBookFragment extends BaseFileFragment {
    @BindView(R.id.refresh_layout)
    RefreshLayout mRlRefresh;
    @BindView(R.id.local_book_rv_content)
    RecyclerView mRvContent;

    @Override
    protected int getContentId() {
        return R.layout.fragment_local_book;
    }

    @Override
    protected void initWidget(Bundle savedInstanceState) {
        super.initWidget(savedInstanceState);
        setUpAdapter();
    }

    private void setUpAdapter() {
        mAdapter = new FileSystemAdapter();
        mRvContent.setLayoutManager(new LinearLayoutManager(getContext()));
        mRvContent.setAdapter(mAdapter);
    }

    @Override
    protected void initClick() {
        super.initClick();
        mAdapter.setOnItemClickListener(
                (view, pos) -> {
                    //如果是已加载的文件，则点击事件无效。
                    String id = mAdapter.getItem(pos).getAbsolutePath();
                    if (BookRepository.getInstance().getCollBook(id) != null) {
                        return;
                    }

                    //点击选中
                    mAdapter.setCheckedItem(pos);

                    //反馈
                    if (mListener != null) {
                        mListener.onItemCheckedChange(mAdapter.getItemIsChecked(pos));
                    }
                }
        );
    }

    @Override
    protected void processLogic() {
        super.processLogic();
        MediaStoreHelper.getAllBookFile(getActivity(),
                (files) -> {
                    if (files.isEmpty()) {
                        mRlRefresh.showEmpty();
                    } else {
                        mAdapter.refreshItems(files);
                        mRlRefresh.showFinish();
                        //反馈
                        if (mListener != null) {
                            mListener.onCategoryChanged();
                        }
                    }
                });
    }
}
