package com.wangzhen.reader.ui.adapter

import android.graphics.drawable.Drawable
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wangzhen.adapter.RecyclerAdapter
import com.wangzhen.reader.ui.adapter.holder.PageStyleHolder
import com.wangzhen.reader.widget.page.PageStyle

/**
 * PageStyleAdapter
 * Created by wangzhen on 2023/4/13
 */
class PageStyleAdapter(list: List<Drawable>) : RecyclerAdapter<Drawable?>(list) {
    private var currentChecked = 0
    override fun onAbsCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PageStyleHolder(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        if (currentChecked == position && holder is PageStyleHolder) {
            holder.setChecked()
        }
    }

    fun setPageStyleChecked(pageStyle: PageStyle) {
        currentChecked = pageStyle.ordinal
        notifyDataSetChanged()
    }
}