package com.wangzhen.reader.utils;


import java.io.File;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wangzhen on 17-5-20.
 * 处理书籍的工具类，配合PageFactory使用
 * 已弃用，
 */

public class BookManager {
    private static final String TAG = "BookManager";
    private String chapterName;
    private String bookId;
    private long chapterLen;
    private long position;
    private Map<String, Cache> cacheMap = new HashMap<>();
    private static volatile BookManager sInstance;

    public static BookManager getInstance() {
        if (sInstance == null) {
            synchronized (BookManager.class) {
                if (sInstance == null) {
                    sInstance = new BookManager();
                }
            }
        }
        return sInstance;
    }
    /**
     * 根据文件名判断是否被缓存过 (因为可能数据库显示被缓存过，但是文件中却没有的情况，所以需要根据文件判断是否被缓存
     * 过)
     *
     * @param folderName : bookId
     * @param fileName:  chapterName
     * @return
     */
    public static boolean isChapterCached(String folderName, String fileName) {
        File file = new File(Constant.BOOK_CACHE_PATH + folderName
                + File.separator + fileName + FileUtils.SUFFIX_NB);
        return file.exists();
    }

    public class Cache {
        private long size;
        private WeakReference<char[]> data;

        public WeakReference<char[]> getData() {
            return data;
        }

        public void setData(WeakReference<char[]> data) {
            this.data = data;
        }

        public long getSize() {
            return size;
        }

        public void setSize(long size) {
            this.size = size;
        }
    }
}
