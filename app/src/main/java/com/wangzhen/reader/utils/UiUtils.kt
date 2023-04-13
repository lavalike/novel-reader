package com.wangzhen.reader.utils

import android.util.TypedValue

/**
 * UiUtils
 * Created by wangzhen on 2023/4/13
 */
object UiUtils {
    private val metrics = AppUtils.getContext().resources.displayMetrics

    @JvmStatic
    fun dpToPx(dp: Int): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), metrics).toInt()
    }

    @JvmStatic
    fun spToPx(sp: Int): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp.toFloat(), metrics).toInt()
    }
}