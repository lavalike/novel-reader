package com.wangzhen.reader.utils;

/**
 * AppConfig
 * Created by wangzhen on 2023/4/12
 */
public class AppConfig {
    public static final class format {
        public static final String FORMAT_BOOK_DATE = "yyyy-MM-dd'T'HH:mm:ss";
        public static final String FORMAT_TIME = "HH:mm";
        public static final String FORMAT_FILE_DATE = "yyyy-MM-dd";
    }

    public static final class text {
        public static final int DEFAULT_TEXT_SIZE = 22;
    }

    public static final class screen {
        public static final int DEFAULT_BRIGHTNESS = 40;
    }

    public static final class dimension {
        public static final int DEFAULT_MARGIN_HEIGHT = 65; // 正文上下边距
        public static final int DEFAULT_MARGIN_WIDTH = 20; // 正文左右边距
        public static final int DEFAULT_TIP_MARGIN_TOP = 35; // 章节标题与顶部边距
        public static final int DEFAULT_TIP_SIZE = 12; // 章节标题大小
        public static final int EXTRA_TITLE_SIZE = 4; // 预留追加文本大小
    }
}
