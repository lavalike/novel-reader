package com.wangzhen.reader.ui.base;

import android.os.Bundle;

import androidx.annotation.Nullable;

/**
 * BaseMVPActivity
 * Created by wangzhen on 2023/4/11
 */
public abstract class BaseMVPActivity<T extends BaseContract.BasePresenter> extends BaseActivity {

    protected T mPresenter;

    protected abstract T bindPresenter();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        attachView(bindPresenter());
    }

    private void attachView(T presenter) {
        mPresenter = presenter;
        mPresenter.attachView(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }
}