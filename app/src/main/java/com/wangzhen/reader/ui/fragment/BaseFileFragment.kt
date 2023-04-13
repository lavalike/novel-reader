package com.wangzhen.reader.ui.fragment

import com.wangzhen.reader.ui.adapter.FileSystemAdapter
import com.wangzhen.reader.ui.base.BaseFragment
import java.io.File

/**
 * BaseFileFragment
 * Created by wangzhen on 2023/4/11
 */
abstract class BaseFileFragment : BaseFragment() {
    protected lateinit var adapter: FileSystemAdapter

    protected var callback: OnFileCheckedCallback? = null

    private var isCheckedAll = false

    fun setCheckedAll(checkedAll: Boolean) {
        isCheckedAll = checkedAll
        adapter.setCheckedAll(checkedAll)
    }

    fun setChecked(checked: Boolean) {
        isCheckedAll = checked
    }

    fun isCheckedAll() = isCheckedAll

    fun getCheckedCount() = adapter.checkedCount

    fun getCheckedFiles(): List<File>? = adapter.checkedFiles

    fun getCheckableCount() = adapter.checkableCount

    fun deleteCheckedFiles() {
        getCheckedFiles()?.let { files ->
            adapter.removeItems(files)
            for (file in files) {
                if (file.exists()) {
                    val ignored = file.delete()
                }
            }
        }
    }

    fun setOnFileCheckedCallback(listener: OnFileCheckedCallback?) {
        callback = listener
    }

    interface OnFileCheckedCallback {
        fun onItemCheckedChange(isChecked: Boolean)
        fun onCategoryChanged()
    }
}