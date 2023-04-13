package com.wangzhen.reader.model.local

import com.wangzhen.reader.utils.AppConfig
import com.wangzhen.reader.utils.UiUtils
import com.wangzhen.reader.utils.SharedPreUtils
import com.wangzhen.reader.widget.page.PageMode
import com.wangzhen.reader.widget.page.PageStyle

/**
 * 阅读器的配置管理
 * Created by wangzhen on 2023/4/13
 */
class ReadSettingManager private constructor() {

    fun setAutoBrightness(isAuto: Boolean) {
        SharedPreUtils.putBoolean(SHARED_READ_IS_BRIGHTNESS_AUTO, isAuto)
    }

    var brightness
        get() = SharedPreUtils.getInt(SHARED_READ_BRIGHTNESS, AppConfig.Screen.DEFAULT_BRIGHTNESS)
        set(progress) {
            SharedPreUtils.putInt(SHARED_READ_BRIGHTNESS, progress)
        }

    val isBrightnessAuto = SharedPreUtils.getBoolean(SHARED_READ_IS_BRIGHTNESS_AUTO, false)

    var textSize
        get() = SharedPreUtils.getInt(
            SHARED_READ_TEXT_SIZE, UiUtils.spToPx(AppConfig.Text.DEFAULT_TEXT_SIZE)
        )
        set(textSize) {
            SharedPreUtils.putInt(SHARED_READ_TEXT_SIZE, textSize)
        }
    var isDefaultTextSize
        get() = SharedPreUtils.getBoolean(SHARED_READ_IS_TEXT_DEFAULT, false)
        set(isDefault) {
            SharedPreUtils.putBoolean(SHARED_READ_IS_TEXT_DEFAULT, isDefault)
        }

    var pageMode: PageMode
        get() {
            val mode = SharedPreUtils.getInt(SHARED_READ_PAGE_MODE, PageMode.SIMULATION.ordinal)
            return PageMode.values()[mode]
        }
        set(mode) {
            SharedPreUtils.putInt(SHARED_READ_PAGE_MODE, mode.ordinal)
        }

    var pageStyle: PageStyle
        get() {
            val style = SharedPreUtils.getInt(SHARED_READ_BG, PageStyle.BG_0.ordinal)
            return PageStyle.values()[style]
        }
        set(pageStyle) {
            SharedPreUtils.putInt(SHARED_READ_BG, pageStyle.ordinal)
        }

    var isNightMode
        get() = SharedPreUtils.getBoolean(SHARED_READ_NIGHT_MODE, false)
        set(isNight) {
            SharedPreUtils.putBoolean(SHARED_READ_NIGHT_MODE, isNight)
        }

    var isVolumeTurnPage
        get() = SharedPreUtils.getBoolean(SHARED_READ_VOLUME_TURN_PAGE, false)
        set(isTurn) {
            SharedPreUtils.putBoolean(SHARED_READ_VOLUME_TURN_PAGE, isTurn)
        }

    companion object {
        const val SHARED_READ_BG = "shared_read_bg"
        const val SHARED_READ_BRIGHTNESS = "shared_read_brightness"
        const val SHARED_READ_IS_BRIGHTNESS_AUTO = "shared_read_is_brightness_auto"
        const val SHARED_READ_TEXT_SIZE = "shared_read_text_size"
        const val SHARED_READ_IS_TEXT_DEFAULT = "shared_read_text_default"
        const val SHARED_READ_PAGE_MODE = "shared_read_mode"
        const val SHARED_READ_NIGHT_MODE = "shared_night_mode"
        const val SHARED_READ_VOLUME_TURN_PAGE = "shared_read_volume_turn_page"

        @JvmStatic
        val instance = ReadSettingManager()
    }
}