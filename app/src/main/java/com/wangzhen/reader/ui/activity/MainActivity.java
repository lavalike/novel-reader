package com.wangzhen.reader.ui.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.wangzhen.commons.toolbar.impl.IconMenuToolbar;
import com.wangzhen.commons.toolbar.impl.Toolbar;
import com.wangzhen.reader.R;
import com.wangzhen.reader.databinding.ActivityMainBinding;
import com.wangzhen.reader.ui.base.BaseActivity;
import com.wangzhen.reader.utils.PermissionsChecker;
import com.wangzhen.reader.utils.ToastUtils;

/**
 * MainActivity
 * Created by wangzhen on 2023/4/11
 */
public class MainActivity extends BaseActivity {
    private static final int WAIT_INTERVAL = 2000;
    private static final int PERMISSIONS_REQUEST_STORAGE = 1;

    static final String[] PERMISSIONS = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private PermissionsChecker mPermissionsChecker;
    private boolean isPrepareFinish = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findViewById(R.id.btn_open).setOnClickListener(view -> open());
    }

    @Override
    protected int getContentId() {
        return R.layout.activity_main;
    }

    private void open() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            if (mPermissionsChecker == null) {
                mPermissionsChecker = new PermissionsChecker(this);
            }
            //获取读取和写入SD卡的权限
            if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
                //请求权限
                ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSIONS_REQUEST_STORAGE);
            }
        }
        Intent intent = new Intent(this, FileSystemActivity.class);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //跳转到 FileSystemActivity
                Intent intent = new Intent(this, FileSystemActivity.class);
                startActivity(intent);

            } else {
                ToastUtils.show("用户拒绝开启读写权限");
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (!isPrepareFinish) {
            new Handler().postDelayed(() -> isPrepareFinish = false, WAIT_INTERVAL);
            isPrepareFinish = true;
            Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
        } else {
            super.onBackPressed();
        }
    }
}
