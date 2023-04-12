package com.wangzhen.reader.utils

import android.annotation.SuppressLint
import android.content.Context

/**
 * AppUtils
 * Created by wangzhen on 2023/4/12
 */
@SuppressLint("StaticFieldLeak")
object AppUtils {
    private lateinit var context: Context

    @JvmStatic
    fun init(ctx: Context) {
        context = ctx
    }

    @JvmStatic
    fun getContext() = context
}