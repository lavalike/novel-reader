package com.wangzhen.reader.utils

/**
 * charsets
 * Created by wangzhen on 2023/4/12
 */
enum class Charset(val charset: String) {
    UTF8("UTF-8"), GBK("GBK");

    companion object {
        const val BLANK: Byte = 0x0a
    }
}