package com.wangzhen.reader.utils

import android.app.Activity
import android.provider.Settings
import android.provider.Settings.SettingNotFoundException
import android.util.Log
import android.view.WindowManager

/**
 * 调节亮度工具类
 * Created by wangzhen on 2023/4/12
 */
object BrightnessUtils {
    private const val TAG = "BrightnessUtils"

    @JvmStatic
    fun isAutoBrightness(activity: Activity) = try {
        Settings.System.getInt(
            activity.contentResolver, Settings.System.SCREEN_BRIGHTNESS_MODE
        ) == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC
    } catch (e: SettingNotFoundException) {
        false
    }

    /**
     * 获取屏幕的亮度
     * 系统亮度模式中，自动模式与手动模式获取到的系统亮度的值不同
     */
    @JvmStatic
    fun getScreenBrightness(activity: Activity) = if (isAutoBrightness(activity)) {
        getAutoScreenBrightness(activity)
    } else {
        getManualScreenBrightness(activity)
    }

    /**
     * 获取手动模式下的屏幕亮度
     *
     * @return value:0~255
     */
    private fun getManualScreenBrightness(activity: Activity) = try {
        Settings.System.getInt(activity.contentResolver, Settings.System.SCREEN_BRIGHTNESS)
    } catch (e: Exception) {
        0
    }

    private fun getAutoScreenBrightness(activity: Activity): Int {
        var nowBrightnessValue = 0f
        //获取自动调节下的亮度范围在 0~1 之间
        val resolver = activity.contentResolver
        try {
            nowBrightnessValue =
                Settings.System.getFloat(resolver, Settings.System.SCREEN_BRIGHTNESS)
            Log.d(TAG, "getAutoScreenBrightness: $nowBrightnessValue")
        } catch (ignored: Exception) {
        }
        //转换范围为 (0~255)
        return (nowBrightnessValue * 225.0f).toInt()
    }

    @JvmStatic
    fun setBrightness(activity: Activity, brightness: Int) {
        try {
            activity.window.attributes = activity.window.attributes.apply {
                screenBrightness = brightness.toFloat() * (1f / 255f)
            }
        } catch (ignored: Exception) {
        }
    }

    @JvmStatic
    fun setDefaultBrightness(activity: Activity) {
        try {
            activity.window.attributes = activity.window.attributes.apply {
                screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE
            }
        } catch (ignored: Exception) {
        }
    }
}