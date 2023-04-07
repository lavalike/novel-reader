package com.wangzhen.reader.presenter.contract;

import com.wangzhen.reader.ui.base.BaseContract;
import com.wangzhen.reader.widget.page.TxtChapter;

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
