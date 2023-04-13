package com.wangzhen.reader.model.local;

import com.wangzhen.reader.utils.AppConfig;
import com.wangzhen.reader.utils.ScreenUtils;
import com.wangzhen.reader.utils.SharedPreUtils;
import com.wangzhen.reader.widget.page.PageMode;
import com.wangzhen.reader.widget.page.PageStyle;

/**
 * Created by wangzhen on 17-5-17.
 * 阅读器的配置管理
 */
public class ReadSettingManager {
    public static final String SHARED_READ_BG = "shared_read_bg";
    public static final String SHARED_READ_BRIGHTNESS = "shared_read_brightness";
    public static final String SHARED_READ_IS_BRIGHTNESS_AUTO = "shared_read_is_brightness_auto";
    public static final String SHARED_READ_TEXT_SIZE = "shared_read_text_size";
    public static final String SHARED_READ_IS_TEXT_DEFAULT = "shared_read_text_default";
    public static final String SHARED_READ_PAGE_MODE = "shared_read_mode";
    public static final String SHARED_READ_NIGHT_MODE = "shared_night_mode";
    public static final String SHARED_READ_VOLUME_TURN_PAGE = "shared_read_volume_turn_page";
    private static volatile ReadSettingManager sInstance;

    private final SharedPreUtils sharedPreUtils;

    public static ReadSettingManager getInstance() {
        if (sInstance == null) {
            synchronized (ReadSettingManager.class) {
                if (sInstance == null) {
                    sInstance = new ReadSettingManager();
                }
            }
        }
        return sInstance;
    }

    private ReadSettingManager() {
        sharedPreUtils = SharedPreUtils.getInstance();
    }

    public void setPageStyle(PageStyle pageStyle) {
        sharedPreUtils.putInt(SHARED_READ_BG, pageStyle.ordinal());
    }

    public void setBrightness(int progress) {
        sharedPreUtils.putInt(SHARED_READ_BRIGHTNESS, progress);
    }

    public void setAutoBrightness(boolean isAuto) {
        sharedPreUtils.putBoolean(SHARED_READ_IS_BRIGHTNESS_AUTO, isAuto);
    }

    public void setDefaultTextSize(boolean isDefault) {
        sharedPreUtils.putBoolean(SHARED_READ_IS_TEXT_DEFAULT, isDefault);
    }

    public void setTextSize(int textSize) {
        sharedPreUtils.putInt(SHARED_READ_TEXT_SIZE, textSize);
    }

    public void setPageMode(PageMode mode) {
        sharedPreUtils.putInt(SHARED_READ_PAGE_MODE, mode.ordinal());
    }

    public void setNightMode(boolean isNight) {
        sharedPreUtils.putBoolean(SHARED_READ_NIGHT_MODE, isNight);
    }

    public int getBrightness() {
        return sharedPreUtils.getInt(SHARED_READ_BRIGHTNESS, AppConfig.screen.DEFAULT_BRIGHTNESS);
    }

    public boolean isBrightnessAuto() {
        return sharedPreUtils.getBoolean(SHARED_READ_IS_BRIGHTNESS_AUTO, false);
    }

    public int getTextSize() {
        return sharedPreUtils.getInt(SHARED_READ_TEXT_SIZE, ScreenUtils.spToPx(AppConfig.text.DEFAULT_TEXT_SIZE));
    }

    public boolean isDefaultTextSize() {
        return sharedPreUtils.getBoolean(SHARED_READ_IS_TEXT_DEFAULT, false);
    }

    public PageMode getPageMode() {
        int mode = sharedPreUtils.getInt(SHARED_READ_PAGE_MODE, PageMode.SIMULATION.ordinal());
        return PageMode.values()[mode];
    }

    public PageStyle getPageStyle() {
        int style = sharedPreUtils.getInt(SHARED_READ_BG, PageStyle.BG_0.ordinal());
        return PageStyle.values()[style];
    }

    public boolean isNightMode() {
        return sharedPreUtils.getBoolean(SHARED_READ_NIGHT_MODE, false);
    }

    public void setVolumeTurnPage(boolean isTurn) {
        sharedPreUtils.putBoolean(SHARED_READ_VOLUME_TURN_PAGE, isTurn);
    }

    public boolean isVolumeTurnPage() {
        return sharedPreUtils.getBoolean(SHARED_READ_VOLUME_TURN_PAGE, false);
    }
}
