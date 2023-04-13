package com.wangzhen.reader.ui.adapter.holder

import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import com.wangzhen.adapter.base.RecyclerViewHolder
import com.wangzhen.reader.R
import com.wangzhen.reader.databinding.ItemReadBgBinding

/**
 * PageStyleHolder
 * Created by wangzhen on 2023/4/13
 */
class PageStyleHolder(parent: ViewGroup) : RecyclerViewHolder<Drawable>(
    parent, R.layout.item_read_bg
) {
    private lateinit var binding: ItemReadBgBinding

    override fun bind() {
        binding = ItemReadBgBinding.bind(itemView).apply {
            readBgView.background = mData
            readBgIvChecked.visibility = View.GONE
        }
    }

    fun setChecked() {
        binding.readBgIvChecked.visibility = View.VISIBLE
    }
}