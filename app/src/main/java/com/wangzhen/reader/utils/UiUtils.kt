package com.wangzhen.reader.utils

import android.util.TypedValue
import com.wangzhen.utils.utils.AppUtils

/**
 * UiUtils
 * Created by wangzhen on 2023/4/13
 */
object UiUtils {
    private val metrics = AppUtils.getContext().resources.displayMetrics

    @JvmStatic
    fun dpToPx(dp: Float): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, metrics).toInt()
    }

    @JvmStatic
    fun spToPx(sp: Float): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, metrics).toInt()
    }
}