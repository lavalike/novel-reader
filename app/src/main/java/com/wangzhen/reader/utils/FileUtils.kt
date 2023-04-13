package com.wangzhen.reader.utils

import java.io.BufferedInputStream
import java.io.FileInputStream
import java.text.DecimalFormat
import kotlin.math.log10
import kotlin.math.pow

/**
 * FileUtils
 * Created by wangzhen on 2023/4/13
 */
object FileUtils {
    @JvmStatic
    fun getFileSize(size: Long): String {
        if (size <= 0) return "0"
        val units = arrayOf("b", "kb", "M", "G", "T")
        //计算单位的，原理是利用lg,公式是 lg(1024^n) = nlg(1024)，最后 nlg(1024)/lg(1024) = n。
        val digitGroups = (log10(size.toDouble()) / log10(1024.0)).toInt()
        //计算原理是，size/单位值。单位值指的是:比如说b = 1024,KB = 1024^2
        return DecimalFormat("#,##0.##").format(
            size / 1024.0.pow(digitGroups.toDouble())
        ) + " " + units[digitGroups]
    }

    @JvmStatic
    fun getCharset(fileName: String?): Charset {
        var bis: BufferedInputStream? = null
        var charset = Charset.GBK
        val first3Bytes = ByteArray(3)
        try {
            var checked = false
            bis = BufferedInputStream(FileInputStream(fileName))
            bis.mark(0)
            var read = bis.read(first3Bytes, 0, 3)
            if (read == -1) return charset
            if (first3Bytes[0] == 0xEF.toByte() && first3Bytes[1] == 0xBB.toByte() && first3Bytes[2] == 0xBF.toByte()) {
                charset = Charset.UTF8
                checked = true
            }
            /*
             * 不支持 UTF16LE 和 UTF16BE
            else if (first3Bytes[0] == (byte) 0xFF && first3Bytes[1] == (byte) 0xFE) {
                charset = Charset.UTF16LE;
                checked = true;
            } else if (first3Bytes[0] == (byte) 0xFE
                    && first3Bytes[1] == (byte) 0xFF) {
                charset = Charset.UTF16BE;
                checked = true;
            } else */bis.mark(0)
            if (!checked) {
                while (bis.read().also { read = it } != -1) {
                    if (read >= 0xF0) break
                    if (read in 0x80..0xBF) {
                        // 单独出现BF以下的，也算是GBK
                        break
                    }
                    if (read in 0xC0..0xDF) {
                        read = bis.read()
                        if (read in 0x80..0xBF) {
                            // 双字节 (0xC0 - 0xDF)
                            // (0x80 - 0xBF),也可能在GB编码内
                            continue
                        } else {
                            break
                        }
                    } else if (read in 0xE0..0xEF) { // 也有可能出错，但是几率较小
                        read = bis.read()
                        if (read in 0x80..0xBF) {
                            read = bis.read()
                            if (read in 0x80..0xBF) {
                                charset = Charset.UTF8
                                break
                            } else break
                        } else break
                    }
                }
            }
        } catch (ignored: Exception) {
        } finally {
            bis?.closeIO()
        }
        return charset
    }
}