package com.wangzhen.reader.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.wangzhen.adapter.base.RecyclerItem
import com.wangzhen.reader.R
import com.wangzhen.reader.databinding.FragmentLocalBookBinding
import com.wangzhen.reader.base.book.BookRepository.Companion.instance
import com.wangzhen.reader.ui.adapter.FileSystemAdapter
import com.wangzhen.reader.utils.media.MediaStoreHelper.MediaResultCallback
import com.wangzhen.reader.utils.media.MediaStoreHelper.getAllBookFile
import java.io.File

/**
 * LocalBookFragment
 * Created by wangzhen on 2023/4/12
 */
class LocalBookFragment : BaseFileFragment() {
    private lateinit var binding: FragmentLocalBookBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentLocalBookBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpAdapter()
        loadFiles()
    }

    private fun setUpAdapter() {
        with(binding) {
            localBookRvContent.layoutManager = LinearLayoutManager(context)
            localBookRvContent.adapter = FileSystemAdapter(null).apply {
                adapter = this
                setEmpty(object : RecyclerItem() {
                    override fun layout(): Int {
                        return R.layout.layout_file_system_empty
                    }
                }.onCreateView(binding.root))
                setOnClickCallback { _, pos ->
                    if (instance.getCollBookByPath(datas[pos].absolutePath) != null) {
                        return@setOnClickCallback
                    }
                    setCheckedItem(pos)
                    callback?.onItemCheckedChange(getItemIsChecked(pos))
                }
            }
        }
    }

    private fun loadFiles() {
        getAllBookFile(requireActivity(), object : MediaResultCallback {
            override fun onResultCallback(files: ArrayList<File>) {
                adapter.setData(files)
                callback?.onCategoryChanged()
            }
        })
    }
}