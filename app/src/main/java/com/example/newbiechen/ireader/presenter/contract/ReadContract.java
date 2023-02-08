package com.example.newbiechen.ireader.presenter.contract;

import com.example.newbiechen.ireader.ui.base.BaseContract;
import com.example.newbiechen.ireader.widget.page.TxtChapter;

import java.util.List;

/**
 * Created by wangzhen on 17-5-16.
 */

public interface ReadContract extends BaseContract {
    interface View extends BaseContract.BaseView {
        void finishChapter();

        void errorChapter();
    }

    interface Presenter extends BaseContract.BasePresenter<View> {
        void loadChapter(String bookId, List<TxtChapter> bookChapterList);
    }
}
