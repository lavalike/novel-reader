package com.wangzhen.reader.utils

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
}