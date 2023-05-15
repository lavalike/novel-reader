package com.wangzhen.reader.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.wifi.WifiManager

/**
 * WifiUtils
 * Created by wangzhen on 2023/5/15
 */
object WifiUtils {

    private fun getWifiManager(context: Context) =
        context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

    fun getIpAddress(context: Context): String? = getWifiManager(context).connectionInfo?.let {
        intToIp(it.ipAddress)
    }

    private fun intToIp(i: Int): String {
        return (i and 0xFF).toString() + "." + (i shr 8 and 0xFF) + "." + (i shr 16 and 0xFF) + "." + (i shr 24 and 0xFF)
    }

    fun getWifiState(context: Context): Int {
        return getWifiManager(context).wifiState
    }

    fun getConnectState(context: Context): NetworkInfo.State {
        val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)?.state
            ?: NetworkInfo.State.DISCONNECTED
    }
}