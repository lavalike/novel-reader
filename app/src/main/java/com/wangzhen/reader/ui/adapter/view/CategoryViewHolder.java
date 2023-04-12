package com.wangzhen.reader.ui.adapter.view;

import android.graphics.drawable.Drawable;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.wangzhen.adapter.base.RecyclerViewHolder;
import com.wangzhen.reader.R;
import com.wangzhen.reader.databinding.ItemCategoryBinding;
import com.wangzhen.reader.widget.page.TxtChapter;

/**
 * CategoryHolder
 * Created by wangzhen on 2023/4/12
 */
public class CategoryViewHolder extends RecyclerViewHolder<TxtChapter> {
    private ItemCategoryBinding binding;

    public CategoryViewHolder(@NonNull ViewGroup parent) {
        super(parent, R.layout.item_category);
    }

    @Override
    public void bind() {
        binding = ItemCategoryBinding.bind(itemView);
        Drawable drawable = ContextCompat.getDrawable(itemView.getContext(), R.drawable.selector_category_load);
        binding.categoryTvChapter.setSelected(false);
        binding.categoryTvChapter.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.text_default));
        binding.categoryTvChapter.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
        binding.categoryTvChapter.setText(mData.getTitle());
    }


    public void setSelectedChapter() {
        binding.categoryTvChapter.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.light_red));
        binding.categoryTvChapter.setSelected(true);
    }
}
