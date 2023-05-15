package com.wangzhen.reader.utils

import android.os.Environment
import com.wangzhen.reader.BuildConfig
import java.io.File

/**
 * AppConfig
 * Created by wangzhen on 2023/4/12
 */
object AppConfig {
    object Format {
        const val FORMAT_BOOK_DATE = "yyyy-MM-dd'T'HH:mm:ss"
        const val FORMAT_TIME = "HH:mm"
        const val FORMAT_FILE_DATE = "yyyy-MM-dd"
    }

    object Text {
        const val DEFAULT_TEXT_SIZE = 22f
    }

    object Screen {
        const val DEFAULT_BRIGHTNESS = 40
    }

    object Dimension {
        const val DEFAULT_MARGIN_HEIGHT = 65 // 正文上下边距
        const val DEFAULT_MARGIN_WIDTH = 20 // 正文左右边距
        const val DEFAULT_TIP_MARGIN_TOP = 35 // 章节标题与顶部边距
        const val DEFAULT_TIP_SIZE = 12 // 章节标题大小
        const val EXTRA_TITLE_SIZE = 4 // 预留追加文本大小
        const val DEFAULT_BOTTOM_INFO_MARGIN = 15 // 底部信息(页码/时间/电量)边距
        const val CONTENT_LINE_SPACE = 30 // 正文行间距
    }

    object Pattern {
        //正则表达式章节匹配模式
        // "(第)([0-9零一二两三四五六七八九十百千万壹贰叁肆伍陆柒捌玖拾佰仟]{1,10})([章节回集卷])(.*)"
        @JvmField
        val CHAPTER_PATTERNS = listOf(
            "^(.{0,8})(\u7b2c)([0-9\u96f6\u4e00\u4e8c\u4e24\u4e09\u56db\u4e94\u516d\u4e03\u516b\u4e5d\u5341\u767e\u5343\u4e07\u58f9\u8d30\u53c1\u8086\u4f0d\u9646\u67d2\u634c\u7396\u62fe\u4f70\u4edf]{1,10})([\u7ae0\u8282\u56de\u96c6\u5377])(.{0,30})$",
            "^(\\s{0,4})([\\(\u3010\u300a]?(\u5377)?)([0-9\u96f6\u4e00\u4e8c\u4e24\u4e09\u56db\u4e94\u516d\u4e03\u516b\u4e5d\u5341\u767e\u5343\u4e07\u58f9\u8d30\u53c1\u8086\u4f0d\u9646\u67d2\u634c\u7396\u62fe\u4f70\u4edf]{1,10})([\\.:\uff1a\u0020\u000c\t])(.{0,30})$",
            "^(\\s{0,4})([\\(\uff08\u3010\u300a])(.{0,30})([\\)\uff09\u3011\u300b])(\\s{0,2})$",
            "^(\\s{0,4})(\u6b63\u6587)(.{0,20})$",
            "^(.{0,4})(Chapter|chapter)(\\s{0,4})([0-9]{1,4})(.{0,30})$"
        )
    }

    object Transfer {
        const val HTTP_PORT = 10123
        const val DIR_NAME = "WiFi-Transfer"

        @JvmField
        val DIR = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), DIR_NAME
        )
        const val ACTION_START_WEB_SERVICE =
            BuildConfig.APPLICATION_ID + ".transfer.action.START_WEB_SERVICE"
        const val ACTION_STOP_WEB_SERVICE =
            BuildConfig.APPLICATION_ID + ".transfer.action.STOP_WEB_SERVICE"
    }

    object ContentType {
        const val TEXT_CONTENT_TYPE = "text/html;charset=utf-8"
        const val CSS_CONTENT_TYPE = "text/css;charset=utf-8"
        const val BINARY_CONTENT_TYPE = "application/octet-stream"
        const val JS_CONTENT_TYPE = "application/javascript"
        const val PNG_CONTENT_TYPE = "application/x-png"
        const val JPG_CONTENT_TYPE = "application/jpeg"
        const val SWF_CONTENT_TYPE = "application/x-shockwave-flash"
        const val WOFF_CONTENT_TYPE = "application/x-font-woff"
        const val TTF_CONTENT_TYPE = "application/x-font-truetype"
        const val SVG_CONTENT_TYPE = "image/svg+xml"
        const val EOT_CONTENT_TYPE = "image/vnd.ms-fontobject"
        const val MP3_CONTENT_TYPE = "audio/mp3"
        const val MP4_CONTENT_TYPE = "video/mpeg4"
    }
}