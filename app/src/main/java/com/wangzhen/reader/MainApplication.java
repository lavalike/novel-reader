package com.wangzhen.reader;

import android.app.Application;
import android.content.Context;

/**
 * MainApplication
 * Created by wangzhen on 2023/4/11
 */
public class MainApplication extends Application {
    private static Context sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }

    public static Context getContext() {
        return sInstance;
    }
}