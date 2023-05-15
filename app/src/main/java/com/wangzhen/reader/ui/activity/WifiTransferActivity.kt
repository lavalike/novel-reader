package com.wangzhen.reader.ui.activity

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import com.wangzhen.commons.toolbar.impl.Toolbar
import com.wangzhen.reader.base.toolbar.AppWifiTransferToolbar
import com.wangzhen.reader.databinding.ActivityWifiTransferBinding
import com.wangzhen.reader.ui.base.BaseActivity
import com.wangzhen.utils.utils.T

/**
 * WifiTransferActivity
 * Created by wangzhen on 2023/5/15
 */
class WifiTransferActivity : BaseActivity() {
    private lateinit var binding: ActivityWifiTransferBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWifiTransferBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fitLightStatusBar()
        initViews()
    }

    private fun initViews() {
        with(binding) {
            tvAddress.text = "http://192.168.1.102:12345"
            btnCopyAddress.setOnClickListener {
                val address = tvAddress.text.toString()
                if (!TextUtils.isEmpty(address)) {
                    (getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).apply {
                        setPrimaryClip(ClipData.newPlainText("wifi-transfer-address", address))
                        T.show("已复制")
                    }
                }
            }
        }
    }

    override fun createToolbar(): Toolbar {
        return AppWifiTransferToolbar(this, "WiFi 传书")
    }
}