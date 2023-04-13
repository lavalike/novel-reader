package com.wangzhen.reader.utils

import java.io.Closeable
import java.io.IOException

/**
 * IOUtils
 * Created by wangzhen on 2023/4/13
 */
object IOUtils {
    @JvmStatic
    fun close(vararg closeables: Closeable) {
        closeables.forEach {
            try {
                it.close()
            } catch (ignored: IOException) {
            }
        }
    }
}