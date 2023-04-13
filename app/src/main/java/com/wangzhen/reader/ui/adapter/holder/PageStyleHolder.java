package com.wangzhen.reader.ui.adapter.holder;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;

import com.wangzhen.adapter.base.RecyclerViewHolder;
import com.wangzhen.reader.R;
import com.wangzhen.reader.databinding.ItemReadBgBinding;

/**
 * PageStyleHolder
 * Created by wangzhen on 2023/4/13
 */
public class PageStyleHolder extends RecyclerViewHolder<Drawable> {
    private ItemReadBgBinding binding;

    public PageStyleHolder(ViewGroup parent) {
        super(parent, R.layout.item_read_bg);
    }

    @Override
    public void bind() {
        binding = ItemReadBgBinding.bind(itemView);
        binding.readBgView.setBackground(mData);
        binding.readBgIvChecked.setVisibility(View.GONE);
    }

    public void setChecked() {
        binding.readBgIvChecked.setVisibility(View.VISIBLE);
    }
}
