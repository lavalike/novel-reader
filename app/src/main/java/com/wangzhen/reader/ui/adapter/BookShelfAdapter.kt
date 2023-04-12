package com.wangzhen.reader.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wangzhen.adapter.RecyclerAdapter
import com.wangzhen.reader.model.bean.CollBookBean
import com.wangzhen.reader.ui.adapter.holder.BookShelfHolder

/**
 * BookShelfAdapter
 * Created by wangzhen on 2023/4/12
 */
class BookShelfAdapter(list: List<CollBookBean>?) : RecyclerAdapter<CollBookBean>(list) {
    override fun onAbsCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return BookShelfHolder(parent)
    }
}