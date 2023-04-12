package com.wangzhen.reader

import android.app.Application
import com.wangzhen.reader.utils.AppUtils

/**
 * MainApplication
 * Created by wangzhen on 2023/4/11
 */
class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AppUtils.init(this)
    }
}