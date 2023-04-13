package com.wangzhen.reader.ui.adapter;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wangzhen.adapter.RecyclerAdapter;
import com.wangzhen.reader.ui.adapter.holder.CategoryHolder;
import com.wangzhen.reader.widget.page.TxtChapter;

import java.util.List;

/**
 * CategoryAdapter
 * Created by wangzhen on 2023/4/12
 */
public class CategoryAdapter extends RecyclerAdapter<TxtChapter> {
    private int currentIndex;

    public CategoryAdapter(List<TxtChapter> list) {
        super(list);
    }

    @Override
    public RecyclerView.ViewHolder onAbsCreateViewHolder(ViewGroup parent, int viewType) {
        return new CategoryHolder(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        if (position == currentIndex && holder instanceof CategoryHolder) {
            ((CategoryHolder) holder).setSelectedChapter();
        }
    }

    public void setChapter(int pos) {
        currentIndex = pos;
        notifyDataSetChanged();
    }
}
