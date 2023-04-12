package com.wangzhen.reader.ui.activity

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import com.wangzhen.permission.PermissionManager.request
import com.wangzhen.permission.callback.AbsPermissionCallback
import com.wangzhen.reader.databinding.ActivityMainBinding
import com.wangzhen.reader.ui.base.BaseActivity
import com.wangzhen.reader.utils.toast

/**
 * MainActivity
 * Created by wangzhen on 2023/4/11
 */
class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnOpen.setOnClickListener { checkPermissions() }
    }

    private fun checkPermissions() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            checkExternalPermissions()
        } else {
            if (!Environment.isExternalStorageManager()) {
                launcher.launch(Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION))
            } else {
                openFileSystem()
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (Environment.isExternalStorageManager()) {
                openFileSystem()
            } else {
                "请授予所有文件访问权限".toast()
            }
        }

    private fun checkExternalPermissions() {
        request(this, object : AbsPermissionCallback() {
            override fun onGrant(permissions: Array<String>) {
                openFileSystem()
            }

            override fun onDeny(
                deniedPermissions: Array<String>, neverAskPermissions: Array<String>
            ) {
                "请授予存储访问权限".toast()
            }
        }, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }

    private fun openFileSystem() {
        startActivity(Intent(this, FileSystemActivity::class.java))
    }
}