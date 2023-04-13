package com.wangzhen.reader.ui.adapter.holder

import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.wangzhen.adapter.base.RecyclerViewHolder
import com.wangzhen.reader.R
import com.wangzhen.reader.databinding.ItemCategoryBinding
import com.wangzhen.reader.widget.page.TxtChapter

/**
 * CategoryHolder
 * Created by wangzhen on 2023/4/12
 */
class CategoryHolder(parent: ViewGroup) :
    RecyclerViewHolder<TxtChapter>(parent, R.layout.item_category) {
    private lateinit var binding: ItemCategoryBinding

    override fun bind() {
        binding = ItemCategoryBinding.bind(itemView).apply {
            categoryTvChapter.apply {
                isSelected = false
                setTextColor(
                    ContextCompat.getColor(
                        itemView.context, R.color.text_default
                    )
                )
                setCompoundDrawablesWithIntrinsicBounds(
                    ContextCompat.getDrawable(itemView.context, R.drawable.selector_category_load),
                    null,
                    null,
                    null
                )
                text = mData.title
            }
        }
    }

    fun setSelectedChapter() {
        with(binding) {
            categoryTvChapter.apply {
                setTextColor(
                    ContextCompat.getColor(
                        itemView.context, R.color.light_red
                    )
                )
                isSelected = true
            }
        }
    }
}