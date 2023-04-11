package com.wangzhen.reader.ui.base;

import com.wangzhen.commons.toolbar.ToolbarActivity;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * NewBaseActivity
 * Created by wangzhen on 2023/4/11
 */
public class NewBaseActivity extends ToolbarActivity {
    protected CompositeDisposable mDisposable;

    protected void addDisposable(Disposable d) {
        if (mDisposable == null) {
            mDisposable = new CompositeDisposable();
        }
        mDisposable.add(d);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDisposable != null) {
            mDisposable.dispose();
        }
    }
}
