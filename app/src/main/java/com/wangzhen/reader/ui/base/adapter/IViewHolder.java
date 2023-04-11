package com.wangzhen.reader.ui.base.adapter;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by wangzhen on 17-5-17.
 */

public interface IViewHolder<T> {
    View createItemView(ViewGroup parent);

    void initView();

    void onBind(T data, int pos);

    void onClick();
}
