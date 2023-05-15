package com.wangzhen.reader.ui.adapter.holder

import android.view.View
import android.view.ViewGroup
import com.wangzhen.adapter.base.RecyclerViewHolder
import com.wangzhen.reader.R
import com.wangzhen.reader.databinding.ItemFileBinding
import com.wangzhen.reader.base.book.BookRepository
import com.wangzhen.reader.utils.AppConfig
import com.wangzhen.reader.utils.FileUtils.getFileSize
import com.wangzhen.reader.utils.MD5Utils
import com.wangzhen.reader.utils.StringUtils.dateConvert
import java.io.File

/**
 * FileHolder
 * Created by wangzhen on 2023/4/12
 */
class FileHolder(parent: ViewGroup, private val mSelectedMap: HashMap<File, Boolean>) :
    RecyclerViewHolder<File>(parent, R.layout.item_file) {
    private lateinit var binding: ItemFileBinding
    override fun bind() {
        binding = ItemFileBinding.bind(itemView)

        //判断是文件还是文件夹
        if (mData.isDirectory) {
            setFolder(mData)
        } else {
            setFile(mData)
        }
    }

    private fun setFile(file: File) {
        with(binding) {
            val id = MD5Utils.strToMd5By16(file.absolutePath) ?: ""
            if (BookRepository.instance.getCollBook(id) != null) {
                fileIvIcon.setImageResource(R.mipmap.ic_file_loaded)
                fileIvIcon.visibility = View.VISIBLE
                fileCbSelect.visibility = View.GONE
            } else {
                fileCbSelect.isChecked = mSelectedMap[file] == true
                fileIvIcon.visibility = View.GONE
                fileCbSelect.visibility = View.VISIBLE
            }
            fileLlBrief.visibility = View.VISIBLE
            fileTvSubCount.visibility = View.GONE
            fileTvName.text = file.name
            fileTvSize.text = getFileSize(file.length())
            fileTvDate.text = dateConvert(
                file.lastModified(), AppConfig.Format.FORMAT_FILE_DATE
            )
        }
    }

    private fun setFolder(folder: File) {
        with(binding) {
            //图片
            fileIvIcon.visibility = View.VISIBLE
            fileCbSelect.visibility = View.GONE
            fileIvIcon.setImageResource(R.drawable.ic_dir)
            //名字
            fileTvName.text = folder.name
            //介绍
            fileLlBrief.visibility = View.GONE
            fileTvSubCount.visibility = View.VISIBLE
            var count = 0
            val list = folder.list()
            if (list != null) {
                count = list.size
            }
            fileTvSubCount.text = itemView.context.getString(R.string.file_sub_count, count)
        }
    }
}