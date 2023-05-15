package com.wangzhen.reader.ui.activity

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import com.wangzhen.adapter.base.RecyclerItem
import com.wangzhen.permission.PermissionManager.request
import com.wangzhen.permission.callback.AbsPermissionCallback
import com.wangzhen.reader.R
import com.wangzhen.reader.base.BookRepository
import com.wangzhen.reader.databinding.ActivityMainBinding
import com.wangzhen.reader.databinding.HeaderBookShelfBinding
import com.wangzhen.reader.model.bean.CollBookBean
import com.wangzhen.reader.ui.adapter.BookShelfAdapter
import com.wangzhen.reader.ui.base.BaseActivity
import com.wangzhen.reader.utils.toast
import java.util.Locale

/**
 * MainActivity
 * Created by wangzhen on 2023/4/11
 */
class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var shelfAdapter: BookShelfAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fitLightStatusBar()
        setUpAdapter()
        loadBooks()
    }

    private fun checkPermissions() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            checkExternalPermissions()
        } else {
            if (!Environment.isExternalStorageManager()) {
                launcher.launch(Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION))
            } else {
                openFileSystem()
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (Environment.isExternalStorageManager()) {
                openFileSystem()
            } else {
                "请授予所有文件访问权限".toast()
            }
        }

    private fun checkExternalPermissions() {
        request(this, object : AbsPermissionCallback() {
            override fun onGrant(permissions: Array<String>) {
                openFileSystem()
            }

            override fun onDeny(
                deniedPermissions: Array<String>, neverAskPermissions: Array<String>
            ) {
                "请授予存储访问权限".toast()
            }
        }, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }

    private fun openFileSystem() {
        startActivity(Intent(this, FileSystemActivity::class.java))
    }

    private fun loadBooks() {
        shelfAdapter.setData(BookRepository.instance.collBooks)
    }

    private fun setUpAdapter() {
        with(binding.recycler) {
            layoutManager = GridLayoutManager(context, 3)
            adapter = BookShelfAdapter(BookRepository.instance.collBooks).apply {
                shelfAdapter = this
                setOnClickCallback { _, pos ->
                    ReadActivity.startActivity(this@MainActivity, datas[pos])
                }
                setOnLongClickCallback { _, pos ->
                    deleteBook(datas[pos])
                }
                addHeader(object : RecyclerItem() {
                    override fun layout() = R.layout.header_book_shelf
                    override fun onViewCreated(itemView: View) {
                        HeaderBookShelfBinding.bind(itemView).apply {
                            btnChooseFiles.setOnClickListener { checkPermissions() }
                            btnWifiTransfer.setOnClickListener {
                                startActivity(Intent(it.context, WifiTransferActivity::class.java))
                            }
                        }
                    }
                }.onCreateView(binding.root))
                setEmpty(object : RecyclerItem() {
                    override fun layout() = R.layout.layout_bookshelf_empty
                }.onCreateView(binding.root))
            }
        }
    }

    private fun deleteBook(book: CollBookBean) {
        AlertDialog.Builder(this)
            .setMessage(String.format(Locale.getDefault(), "确定删除%s吗", book.title))
            .setNegativeButton("取消", null).setPositiveButton("确定") { _, _ ->
                BookRepository.instance.deleteBook(book)
                Toast.makeText(this, book.title + "已删除", Toast.LENGTH_SHORT).show()
                setUpAdapter()
            }.create().show()
    }

    override fun onResume() {
        super.onResume()
        loadBooks()
    }
}