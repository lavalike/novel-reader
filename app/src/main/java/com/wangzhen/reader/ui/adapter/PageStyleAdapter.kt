package com.wangzhen.reader.ui.adapter;

import android.graphics.drawable.Drawable;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wangzhen.adapter.RecyclerAdapter;
import com.wangzhen.reader.ui.adapter.holder.PageStyleHolder;
import com.wangzhen.reader.widget.page.PageStyle;

import java.util.List;

/**
 * PageStyleAdapter
 * Created by wangzhen on 2023/4/13
 */
public class PageStyleAdapter extends RecyclerAdapter<Drawable> {
    private int currentChecked;

    public PageStyleAdapter(List<Drawable> list) {
        super(list);
    }

    @Override
    public RecyclerView.ViewHolder onAbsCreateViewHolder(ViewGroup parent, int viewType) {
        return new PageStyleHolder(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        if (currentChecked == position && holder instanceof PageStyleHolder) {
            ((PageStyleHolder) holder).setChecked();
        }
    }

    public void setPageStyleChecked(PageStyle pageStyle) {
        currentChecked = pageStyle.ordinal();
        notifyDataSetChanged();
    }
}
