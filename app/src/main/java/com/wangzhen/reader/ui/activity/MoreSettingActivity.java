package com.wangzhen.reader.ui.activity;

import android.os.Bundle;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;

import com.wangzhen.commons.toolbar.impl.Toolbar;
import com.wangzhen.reader.R;
import com.wangzhen.reader.base.toolbar.AppCommonToolbar;
import com.wangzhen.reader.model.local.ReadSettingManager;
import com.wangzhen.reader.ui.base.BaseActivity;

import butterknife.BindView;

/**
 * MoreSettingActivity
 * Created by wangzhen on 2023/4/11
 */
public class MoreSettingActivity extends BaseActivity {
    @BindView(R.id.more_setting_rl_volume)
    RelativeLayout mRlVolume;
    @BindView(R.id.more_setting_sc_volume)
    SwitchCompat mScVolume;
    private ReadSettingManager mSettingManager;
    private boolean isVolumeTurnPage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public Toolbar createToolbar() {
        return new AppCommonToolbar(this, "阅读设置");
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
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        initSwitchStatus();
    }

    private void initSwitchStatus() {
        mScVolume.setChecked(isVolumeTurnPage);
    }

    @Override
    protected void initClick() {
        super.initClick();
        mRlVolume.setOnClickListener((v) -> {
            isVolumeTurnPage = !isVolumeTurnPage;
            mScVolume.setChecked(isVolumeTurnPage);
            mSettingManager.setVolumeTurnPage(isVolumeTurnPage);
        });
    }
}
