package com.wangzhen.reader.ui.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.wangzhen.commons.toolbar.impl.Toolbar;
import com.wangzhen.reader.base.toolbar.AppCommonToolbar;
import com.wangzhen.reader.databinding.ActivityMoreSettingBinding;
import com.wangzhen.reader.model.local.ReadSettingManager;
import com.wangzhen.reader.ui.base.NewBaseActivity;

/**
 * MoreSettingActivity
 * Created by wangzhen on 2023/4/11
 */
public class MoreSettingActivity extends NewBaseActivity {
    private ActivityMoreSettingBinding binding;
    private ReadSettingManager mSettingManager;
    private boolean isVolumeTurnPage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMoreSettingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mSettingManager = ReadSettingManager.getInstance();
        isVolumeTurnPage = mSettingManager.isVolumeTurnPage();

        binding.moreSettingScVolume.setChecked(isVolumeTurnPage);
        binding.moreSettingRlVolume.setOnClickListener((v) -> {
            binding.moreSettingScVolume.setChecked(isVolumeTurnPage = !isVolumeTurnPage);
            mSettingManager.setVolumeTurnPage(isVolumeTurnPage);
        });
    }

    @Nullable
    @Override
    public Toolbar createToolbar() {
        return new AppCommonToolbar(this, "阅读设置");
    }
}
