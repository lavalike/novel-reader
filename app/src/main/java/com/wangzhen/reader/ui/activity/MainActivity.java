package com.wangzhen.reader.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.wangzhen.reader.R;
import com.wangzhen.reader.ui.base.BaseActivity;
import com.wangzhen.reader.utils.PermissionsChecker;
import com.wangzhen.reader.utils.ToastUtils;

public class MainActivity extends BaseActivity {
    private static final int WAIT_INTERVAL = 2000;
    private static final int PERMISSIONS_REQUEST_STORAGE = 1;

    static final String[] PERMISSIONS = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private PermissionsChecker mPermissionsChecker;
    private boolean isPrepareFinish = false;

    @Override
    protected int getContentId() {
        return R.layout.activity_main;
    }

    @Override
    protected void setUpToolbar(Toolbar toolbar) {
        super.setUpToolbar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setTitle(getString(R.string.app_name));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Class<?> activityCls = null;
        if (id == R.id.action_scan_local_book) {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                if (mPermissionsChecker == null) {
                    mPermissionsChecker = new PermissionsChecker(this);
                }
                //获取读取和写入SD卡的权限
                if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
                    //请求权限
                    ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSIONS_REQUEST_STORAGE);
                    return super.onOptionsItemSelected(item);
                }
            }
            activityCls = FileSystemActivity.class;
        }
        if (activityCls != null) {
            Intent intent = new Intent(this, activityCls);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
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
