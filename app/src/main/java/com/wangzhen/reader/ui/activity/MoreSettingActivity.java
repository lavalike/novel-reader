package com.wangzhen.reader.ui.activity;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;

import android.widget.RelativeLayout;

import com.wangzhen.reader.R;
import com.wangzhen.reader.model.local.ReadSettingManager;
import com.wangzhen.reader.ui.base.BaseActivity;

import butterknife.BindView;

/**
 * Created by wangzhen on 17-6-6.
 * 阅读界面的更多设置
 */

public class MoreSettingActivity extends BaseActivity {
    @BindView(R.id.more_setting_rl_volume)
    RelativeLayout mRlVolume;
    @BindView(R.id.more_setting_sc_volume)
    SwitchCompat mScVolume;
    @BindView(R.id.more_setting_rl_full_screen)
    RelativeLayout mRlFullScreen;
    @BindView(R.id.more_setting_sc_full_screen)
    SwitchCompat mScFullScreen;
    private ReadSettingManager mSettingManager;
    private boolean isVolumeTurnPage;
    private boolean isFullScreen;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getContentId() {
        return R.layout.activity_more_setting;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        mSettingManager = ReadSettingManager.getInstance();
        isVolumeTurnPage = mSettingManager.isVolumeTurnPage();
        isFullScreen = mSettingManager.isFullScreen();
    }

    @Override
    protected void setUpToolbar(Toolbar toolbar) {
        super.setUpToolbar(toolbar);
        getSupportActionBar().setTitle("阅读设置");
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        initSwitchStatus();
    }

    private void initSwitchStatus() {
        mScVolume.setChecked(isVolumeTurnPage);
        mScFullScreen.setChecked(isFullScreen);
    }

    @Override
    protected void initClick() {
        super.initClick();
        mRlVolume.setOnClickListener((v) -> {
            isVolumeTurnPage = !isVolumeTurnPage;
            mScVolume.setChecked(isVolumeTurnPage);
            mSettingManager.setVolumeTurnPage(isVolumeTurnPage);
        });

        mRlFullScreen.setOnClickListener((v) -> {
            if (isFullScreen) {
                isFullScreen = false;
            } else {
                isFullScreen = true;
            }
            mScFullScreen.setChecked(isFullScreen);
            mSettingManager.setFullScreen(isFullScreen);
        });
    }
}
