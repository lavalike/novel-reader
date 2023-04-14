package com.wangzhen.reader.ui.fragment

import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.wangzhen.adapter.base.RecyclerItem
import com.wangzhen.reader.R
import com.wangzhen.reader.databinding.FragmentFileCategoryBinding
import com.wangzhen.reader.base.BookRepository.Companion.instance
import com.wangzhen.reader.ui.adapter.FileSystemAdapter
import com.wangzhen.reader.utils.FileStack
import com.wangzhen.reader.utils.FileStack.FileSnapshot
import java.io.File
import java.io.FileFilter
import java.util.*

/**
 * FileCategoryFragment
 * Created by wangzhen on 2023/4/13
 */
class FileCategoryFragment : BaseFileFragment() {
    private lateinit var binding: FragmentFileCategoryBinding
    private var fileStack = FileStack()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentFileCategoryBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpAdapter()
        loadTree(Environment.getExternalStorageDirectory())

        with(binding) {
            fileCategoryTvBackLast.setOnClickListener {
                val snapshot = fileStack.pop() ?: return@setOnClickListener
                val oldScrollOffset = fileCategoryRvContent.computeHorizontalScrollOffset()
                fileCategoryTvPath.text = snapshot.filePath
                adapter.setData(snapshot.files)
                fileCategoryRvContent.scrollBy(0, snapshot.scrollOffset - oldScrollOffset)
                callback?.onCategoryChanged()
            }
        }
    }

    private fun setUpAdapter() {
        with(binding) {
            fileCategoryRvContent.layoutManager = LinearLayoutManager(context)
            fileCategoryRvContent.adapter = FileSystemAdapter(null).apply {
                adapter = this
                setEmpty(object : RecyclerItem() {
                    override fun layout(): Int {
                        return R.layout.layout_file_system_empty
                    }
                }.onCreateView(binding.root))
                setOnClickCallback { v: View?, pos: Int ->
                    val file = datas[pos]
                    if (file.isDirectory) {
                        fileStack.push(FileSnapshot().apply {
                            filePath = fileCategoryTvPath.text.toString()
                            files = ArrayList(datas)
                            scrollOffset = fileCategoryRvContent.computeVerticalScrollOffset()
                        })
                        loadTree(file)
                    } else {
                        val path = datas[pos].absolutePath
                        if (instance.getCollBookByPath(path) != null) {
                            return@setOnClickCallback
                        }
                        setCheckedItem(pos)
                        callback?.onItemCheckedChange(getItemIsChecked(pos))
                    }
                }
            }
        }
    }

    private fun loadTree(file: File) {
        binding.fileCategoryTvPath.text = getString(R.string.file_path, file.path)
        file.listFiles(SimpleFileFilter())?.let { files ->
            val rootFiles = listOf(*files)
            Collections.sort(rootFiles, FileComparator())
            adapter.setData(rootFiles)
            callback?.onCategoryChanged()
        }
    }

    internal class FileComparator : Comparator<File> {
        override fun compare(o1: File, o2: File): Int {
            if (o1.isDirectory && o2.isFile) {
                return -1
            }
            return if (o2.isDirectory && o1.isFile) {
                1
            } else o1.name.compareTo(o2.name, ignoreCase = true)
        }
    }

    internal class SimpleFileFilter : FileFilter {
        override fun accept(file: File): Boolean {
            if (file.name.startsWith(".")) {
                return false
            }
            val list = file.list()
            if (file.isDirectory && list != null && list.isEmpty()) {
                return false
            }
            if (file.isFile && (file.length() == 0L || !file.name.endsWith(".txt", true))) {
                return false
            }
            return true
        }
    }
}