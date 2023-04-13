package com.wangzhen.reader.utils.media

import android.content.Context

/**
 * LoaderCreator
 * Created by wangzhen on 2023/4/13
 */
object LoaderCreator {
    const val ALL_BOOK_FILE = 1

    @JvmStatic
    fun create(context: Context) = LocalFileLoader(context)
}