package com.wangzhen.reader.base.toolbar;

import android.app.Activity;
import android.view.View;

import androidx.annotation.NonNull;

import com.wangzhen.commons.toolbar.impl.Toolbar;
import com.wangzhen.reader.R;
import com.wangzhen.reader.databinding.AppToolbarCommonBinding;

/**
 * AppCommonToolbar
 * Created by wangzhen on 2023/4/10
 */
public class AppCommonToolbar extends Toolbar {
    private final Activity activity;
    private final String title;

    public AppCommonToolbar(@NonNull Activity activity, String title) {
        super(activity);
        this.activity = activity;
        this.title = title;
    }

    @Override
    public int layoutRes() {
        return R.layout.app_toolbar_common;
    }

    @Override
    public void onViewCreated(@NonNull View view) {
        AppToolbarCommonBinding binding = AppToolbarCommonBinding.bind(view);
        binding.btnBack.setOnClickListener(v -> activity.finish());
        binding.title.setText(title);
    }
}
