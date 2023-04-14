package com.wangzhen.reader.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wangzhen.adapter.RecyclerAdapter
import com.wangzhen.reader.ui.adapter.holder.CategoryHolder
import com.wangzhen.reader.widget.page.TxtChapter

/**
 * CategoryAdapter
 * Created by wangzhen on 2023/4/12
 */
class CategoryAdapter(list: List<TxtChapter>?) : RecyclerAdapter<TxtChapter>(list) {
    private var currentIndex = 0
    override fun onAbsCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CategoryHolder(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        if (position == currentIndex && holder is CategoryHolder) {
            holder.setSelectedChapter()
        }
    }

    fun setChapter(pos: Int) {
        currentIndex = pos
        notifyDataSetChanged()
    }
}