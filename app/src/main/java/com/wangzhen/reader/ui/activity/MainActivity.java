package com.wangzhen.reader.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.wangzhen.permission.PermissionManager;
import com.wangzhen.permission.callback.AbsPermissionCallback;
import com.wangzhen.reader.databinding.ActivityMainBinding;
import com.wangzhen.reader.ui.base.NewBaseActivity;

/**
 * MainActivity
 * Created by wangzhen on 2023/4/11
 */
public class MainActivity extends NewBaseActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.btnOpen.setOnClickListener(view -> open());
    }

    private void open() {
        PermissionManager.request(this, new AbsPermissionCallback() {
            @Override
            public void onGrant(@NonNull String[] strings) {
                startActivity(new Intent(MainActivity.this, FileSystemActivity.class));
            }

            @Override
            public void onDeny(@NonNull String[] strings, @NonNull String[] strings1) {
                Toast.makeText(MainActivity.this, "请授予存储权限", Toast.LENGTH_SHORT).show();
            }
        }, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }
}
