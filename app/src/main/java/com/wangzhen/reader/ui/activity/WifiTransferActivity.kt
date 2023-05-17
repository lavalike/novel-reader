package com.wangzhen.reader.ui.activity

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkRequest
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.view.View
import com.wangzhen.commons.toolbar.impl.Toolbar
import com.wangzhen.reader.base.toolbar.AppWifiTransferToolbar
import com.wangzhen.reader.databinding.ActivityWifiTransferBinding
import com.wangzhen.reader.transfer.TransferService
import com.wangzhen.reader.ui.base.BaseActivity
import com.wangzhen.reader.utils.AppConfig
import com.wangzhen.reader.utils.WifiUtils
import com.wangzhen.utils.utils.T

/**
 * WifiTransferActivity
 * Created by wangzhen on 2023/5/15
 */
class WifiTransferActivity : BaseActivity() {
    private lateinit var binding: ActivityWifiTransferBinding
    private lateinit var manager: ConnectivityManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWifiTransferBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fitLightStatusBar()
        initViews()
        updateState()
        registerConnectivityManager()
        TransferService.start(this)
    }

    private fun registerConnectivityManager() {
        manager = (getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).apply {
            registerNetworkCallback(NetworkRequest.Builder().build(), networkCallback)
        }
    }

    private fun initViews() {
        with(binding) {
            btnCopyAddress.setOnClickListener {
                val address = tvAddress.text.toString()
                if (!TextUtils.isEmpty(address)) {
                    (getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).apply {
                        setPrimaryClip(ClipData.newPlainText("wifi-transfer-address", address))
                        T.show("已复制")
                    }
                }
            }
            btnWifiSetting.setOnClickListener {
                startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
            }
        }
    }

    private val networkCallback = object : NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            runOnUiThread { updateState() }
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            runOnUiThread { updateState() }
        }
    }

    private fun updateState() {
        if (WifiUtils.isWiFiConnected(this)) {
            showConnected()
        } else {
            showDisconnected()
        }
    }

    private fun showDisconnected() {
        binding.containerWifi.visibility = View.GONE
        binding.containerSetting.visibility = View.VISIBLE
    }

    private fun showConnected() {
        binding.containerWifi.visibility = View.VISIBLE
        binding.containerSetting.visibility = View.GONE
        binding.tvAddress.text = String.format(
            "http://%s:%d",
            WifiUtils.getIpAddress(this@WifiTransferActivity),
            AppConfig.Transfer.HTTP_PORT
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        TransferService.stop(this)
        manager.unregisterNetworkCallback(networkCallback)
    }

    override fun createToolbar(): Toolbar {
        return AppWifiTransferToolbar(this, "WiFi 传书")
    }

}