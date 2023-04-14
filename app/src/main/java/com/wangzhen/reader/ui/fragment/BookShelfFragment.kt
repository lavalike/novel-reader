package com.wangzhen.reader.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import com.wangzhen.adapter.base.RecyclerItem
import com.wangzhen.reader.R
import com.wangzhen.reader.databinding.FragmentBookshelfBinding
import com.wangzhen.reader.model.bean.CollBookBean
import com.wangzhen.reader.base.BookRepository
import com.wangzhen.reader.ui.activity.ReadActivity
import com.wangzhen.reader.ui.adapter.BookShelfAdapter
import com.wangzhen.reader.ui.base.BaseFragment
import java.util.*

/**
 * BookShelfFragment
 * Created by wangzhen on 2023/4/12
 */
class BookShelfFragment : BaseFragment() {
    private lateinit var binding: FragmentBookshelfBinding
    private lateinit var shelfAdapter: BookShelfAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentBookshelfBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpAdapter()
        loadBooks()
    }

    private fun loadBooks() {
        shelfAdapter.setData(BookRepository.instance.collBooks)
    }

    private fun setUpAdapter() {
        with(binding.bookShelfRvContent) {
            layoutManager = GridLayoutManager(context, 3)
            adapter = BookShelfAdapter(BookRepository.instance.collBooks).apply {
                shelfAdapter = this
                setOnClickCallback { _, pos ->
                    ReadActivity.startActivity(requireContext(), datas[pos])
                }
                setOnLongClickCallback { _, pos ->
                    deleteBook(datas[pos])
                }
                setEmpty(object : RecyclerItem() {
                    override fun layout() = R.layout.layout_bookshelf_empty
                }.onCreateView(binding.root))
            }
        }
    }

    private fun deleteBook(book: CollBookBean) {
        AlertDialog.Builder(requireContext())
            .setMessage(String.format(Locale.getDefault(), "确定删除%s吗", book.title))
            .setNegativeButton("取消", null).setPositiveButton("确定") { _, _ ->
                BookRepository.instance.deleteBook(book)
                Toast.makeText(context, book.title + "已删除", Toast.LENGTH_SHORT).show()
                setUpAdapter()
            }.create().show()
    }

    override fun onResume() {
        super.onResume()
        loadBooks()
    }
}