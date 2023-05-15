package com.wangzhen.reader

import android.app.Application
import com.wangzhen.utils.utils.AppUtils

/**
 * MainApplication
 * Created by wangzhen on 2023/4/11
 */
class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AppUtils.install(this)
    }
}