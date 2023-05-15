package com.wangzhen.reader.base.book

import com.wangzhen.reader.model.bean.CollBookBean
import com.wangzhen.reader.utils.AppConfig
import com.wangzhen.reader.utils.MD5Utils
import com.wangzhen.reader.utils.StringUtils
import java.io.File

/**
 * BookConverter
 * Created by wangzhen on 2023/5/15
 */
object BookConverter {

    /**
     * 将文件转换成CollBook
     *
     * @param files:需要加载的文件列表
     * @return list of [CollBookBean]
     */
    @JvmStatic
    fun convertCollBook(files: List<File>): List<CollBookBean> {
        val collBooks: MutableList<CollBookBean> = ArrayList(files.size)
        for (file in files) {
            //判断文件是否存在
            if (!file.exists()) continue
            val collBook = CollBookBean()
            collBook._id = MD5Utils.strToMd5By16(file.absolutePath)
            collBook.title = file.name.replace(".txt", "")
            collBook.author = ""
            collBook.shortIntro = "无"
            collBook.cover = file.absolutePath
            collBook.setLocal(true)
            collBook.lastChapter = "开始阅读"
            collBook.updated =
                StringUtils.dateConvert(file.lastModified(), AppConfig.Format.FORMAT_BOOK_DATE)
            collBook.lastRead = StringUtils.dateConvert(
                System.currentTimeMillis(), AppConfig.Format.FORMAT_BOOK_DATE
            )
            collBooks.add(collBook)
        }
        return collBooks
    }
}