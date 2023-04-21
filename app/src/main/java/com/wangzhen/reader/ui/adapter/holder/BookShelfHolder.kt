package com.wangzhen.reader.ui.adapter.holder

import android.view.ViewGroup
import com.wangzhen.adapter.base.RecyclerViewHolder
import com.wangzhen.reader.R
import com.wangzhen.reader.databinding.ItemCollBookBinding
import com.wangzhen.reader.model.bean.CollBookBean

/**
 * BookShelfHolder
 * Created by wangzhen on 2023/4/12
 */
class BookShelfHolder(parent: ViewGroup) :
    RecyclerViewHolder<CollBookBean>(parent, R.layout.item_coll_book) {
    override fun bind() {
        ItemCollBookBinding.bind(itemView).apply {
            bookName.text = mData.title
            lastChapter.text = if (mData.isUpdate()) "未阅读" else mData.lastChapter.trim()
        }
    }
}