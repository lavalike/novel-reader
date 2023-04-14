package com.wangzhen.reader.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wangzhen.adapter.RecyclerAdapter
import com.wangzhen.reader.base.BookRepository.Companion.instance
import com.wangzhen.reader.ui.adapter.holder.FileHolder
import java.io.File

/**
 * Created by wangzhen on 17-5-27.
 */
class FileSystemAdapter(list: List<File>?) : RecyclerAdapter<File>(list) {
    private val mCheckMap = HashMap<File, Boolean>()
    var checkedCount = 0

    override fun onAbsCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return FileHolder(parent, mCheckMap)
    }

    override fun setData(list: List<File>?) {
        list?.let { data ->
            mCheckMap.clear()
            for (file in data) {
                mCheckMap[file] = false
            }
            super.setData(data)
        }
    }

    fun removeItems(value: List<File>) {
        //删除在HashMap中的文件
        for (file in value) {
            mCheckMap.remove(file)
            //因为，能够被移除的文件，肯定是选中的
            --checkedCount
        }
        //删除列表中的文件
        super.getDatas().removeAll(value)
    }

    //设置点击切换
    fun setCheckedItem(pos: Int) {
        val file = datas[pos]
        if (isFileLoaded(file.absolutePath)) return
        if (mCheckMap[file] == true) {
            mCheckMap[file] = false
            --checkedCount
        } else {
            mCheckMap[file] = true
            ++checkedCount
        }
        notifyDataSetChanged()
    }

    fun setCheckedAll(isChecked: Boolean) {
        val entries: Set<MutableMap.MutableEntry<File, Boolean>> = mCheckMap.entries
        checkedCount = 0
        for (entry in entries) {
            //必须是文件，必须没有被收藏
            if (entry.key.isFile && !isFileLoaded(entry.key.absolutePath)) {
                entry.setValue(isChecked)
                //如果选中，则增加点击的数量
                if (isChecked) {
                    ++checkedCount
                }
            }
        }
        notifyDataSetChanged()
    }

    private fun isFileLoaded(path: String): Boolean {
        //如果是已加载的文件，则点击事件无效。
        return instance.getCollBookByPath(path) != null
    }

    fun checkableCount(): Int {
        var count = 0
        if (datas != null) {
            for (file in datas) {
                if (!isFileLoaded(file!!.absolutePath) && file.isFile) ++count
            }
        }
        return count
    }

    fun getItemIsChecked(pos: Int): Boolean {
        return mCheckMap[datas[pos]] == true
    }

    fun checkedFiles(): List<File> {
        val files: MutableList<File> = ArrayList()
        for ((key, value) in mCheckMap.entries) {
            if (value) {
                files.add(key)
            }
        }
        return files
    }
}