package com.wangzhen.reader.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.wangzhen.permission.PermissionManager;
import com.wangzhen.permission.callback.AbsPermissionCallback;
import com.wangzhen.reader.databinding.ActivityMainBinding;
import com.wangzhen.reader.ui.base.BaseActivity;

/**
 * MainActivity
 * Created by wangzhen on 2023/4/11
 */
public class MainActivity extends BaseActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.btnOpen.setOnClickListener(view -> checkPermissions());
    }

    private void checkPermissions() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            checkExternalPermissions();
        } else {
            if (!Environment.isExternalStorageManager()) {
                launcher.launch(new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION));
            } else {
                openFileSystem();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    private final ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (Environment.isExternalStorageManager()) {
            openFileSystem();
        } else {
            Toast.makeText(MainActivity.this, "请授予所有文件访问权限", Toast.LENGTH_SHORT).show();
        }
    });

    private void checkExternalPermissions() {
        PermissionManager.request(this, new AbsPermissionCallback() {
            @Override
            public void onGrant(@NonNull String[] strings) {
                openFileSystem();
            }

            @Override
            public void onDeny(@NonNull String[] strings, @NonNull String[] strings1) {
                Toast.makeText(MainActivity.this, "请授予存储访问权限", Toast.LENGTH_SHORT).show();
            }
        }, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    private void openFileSystem() {
        startActivity(new Intent(this, FileSystemActivity.class));
    }
}
