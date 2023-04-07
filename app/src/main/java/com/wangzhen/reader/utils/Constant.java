package com.wangzhen.reader.utils;

import java.io.File;

/**
 * Created by wangzhen on 17-4-16.
 */

public class Constant {
    /*URL_BASE*/
    public static final String API_BASE_URL = "http://api.zhuishushenqi.com";
    //Book Date Convert Format
    public static final String FORMAT_BOOK_DATE = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String FORMAT_TIME = "HH:mm";
    public static final String FORMAT_FILE_DATE = "yyyy-MM-dd";
    //BookCachePath (因为getCachePath引用了Context，所以必须是静态变量，不能够是静态常量)
    public static String BOOK_CACHE_PATH = FileUtils.getCachePath() + File.separator + "book_cache" + File.separator;

    public static final class Text {
        public static final int DEFAULT_TEXT_SIZE = 22;
    }

    public static final class Screen {
        public static final int DEFAULT_BRIGHTNESS = 40;
    }
}
