package com.wangzhen.reader.utils

import androidx.annotation.StringRes
import java.text.SimpleDateFormat
import java.util.*

/**
 * StringUtils
 * Created by wangzhen on 2023/4/13
 */
object StringUtils {

    @JvmStatic
    fun dateConvert(time: Long, pattern: String): String {
        return SimpleDateFormat(pattern, Locale.getDefault()).format(Date(time))
    }

    @JvmStatic
    fun getString(@StringRes id: Int): String {
        return AppUtils.getContext().resources.getString(id)
    }

    /**
     * 将文本中的半角字符，转换成全角字符
     *
     * @param input half
     * @return full
     */
    @JvmStatic
    fun halfToFull(input: String): String {
        val c = input.toCharArray()
        for (i in c.indices) {
            if (c[i].code == 32) {
                //半角空格
                c[i] = 12288.toChar()
                continue
            }
            //根据实际情况，过滤不需要转换的符号
            //if (c[i] == 46) //半角点号，不转换
            // continue;
            if (c[i].code in 33..126) //其他符号都转换为全角
                c[i] = (c[i].code + 65248).toChar()
        }
        return String(c)
    }

    /**
     * 字符串全角转换为半角
     *
     * @param input input
     * @return output
     */
    @JvmStatic
    fun fullToHalf(input: String): String {
        val c = input.toCharArray()
        for (i in c.indices) {
            if (c[i].code == 12288) {
                //全角空格
                c[i] = 32.toChar()
                continue
            }
            if (c[i].code in 65281..65374) {
                c[i] = (c[i].code - 65248).toChar()
            }
        }
        return String(c)
    }
}