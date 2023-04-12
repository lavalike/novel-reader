package com.wangzhen.reader.utils;

/**
 * charsets
 * Created by wangzhen on 2023/4/12
 */
public enum Charset {
    UTF8("UTF-8"), GBK("GBK");

    private final String mName;
    public static final byte BLANK = 0x0a;

    Charset(String name) {
        mName = name;
    }

    public String getName() {
        return mName;
    }
}
