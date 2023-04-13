package com.wangzhen.reader.utils

import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

/**
 * MD5Utils
 * Created by wangzhen on 2023/4/13
 */
object MD5Utils {
    private fun strToMd5By32(str: String): String? {
        var value: String? = null
        try {
            val md5 = MessageDigest.getInstance("MD5")
            val bytes = md5.digest(str.toByteArray())
            val stringBuffer = StringBuffer()
            for (b in bytes) {
                val bt = b.toInt() and 0xff
                if (bt < 16) {
                    stringBuffer.append(0)
                }
                stringBuffer.append(Integer.toHexString(bt))
            }
            value = stringBuffer.toString()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
        return value
    }

    @JvmStatic
    fun strToMd5By16(str: String): String? {
        val value = strToMd5By32(str)
        return value?.substring(8, 24) ?: value
    }
}