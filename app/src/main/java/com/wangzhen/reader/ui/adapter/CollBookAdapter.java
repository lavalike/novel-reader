package com.wangzhen.reader.ui.adapter;

import com.wangzhen.reader.model.bean.CollBookBean;
import com.wangzhen.reader.ui.adapter.view.CollBookHolder;
import com.wangzhen.reader.ui.base.adapter.IViewHolder;
import com.wangzhen.reader.widget.adapter.WholeAdapter;

/**
 * Created by wangzhen on 17-5-8.
 */

public class CollBookAdapter extends WholeAdapter<CollBookBean> {

    @Override
    protected IViewHolder<CollBookBean> createViewHolder(int viewType) {
        return new CollBookHolder();
    }

}
