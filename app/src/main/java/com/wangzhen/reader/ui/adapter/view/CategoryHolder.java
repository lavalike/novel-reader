package com.wangzhen.reader.ui.adapter.view;

import android.graphics.drawable.Drawable;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.wangzhen.reader.R;
import com.wangzhen.reader.ui.base.adapter.ViewHolderImpl;
import com.wangzhen.reader.widget.page.TxtChapter;

/**
 * Created by wangzhen on 17-5-16.
 */

public class CategoryHolder extends ViewHolderImpl<TxtChapter> {

    private TextView mTvChapter;

    @Override
    public void initView() {
        mTvChapter = findById(R.id.category_tv_chapter);
    }

    @Override
    public void onBind(TxtChapter value, int pos) {
        Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.selector_category_load);
        mTvChapter.setSelected(false);
        mTvChapter.setTextColor(ContextCompat.getColor(getContext(), R.color.text_default));
        mTvChapter.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
        mTvChapter.setText(value.getTitle());
    }

    @Override
    protected int getItemLayoutId() {
        return R.layout.item_category;
    }

    public void setSelectedChapter() {
        mTvChapter.setTextColor(ContextCompat.getColor(getContext(), R.color.light_red));
        mTvChapter.setSelected(true);
    }
}
