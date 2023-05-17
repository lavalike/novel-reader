package com.wangzhen.reader.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.net.wifi.WifiManager
import android.os.Build

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

    fun isWiFiConnected(ctx: Context): Boolean {
        val manager = ctx.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)?.let { networkInfo ->
                return networkInfo.isAvailable
            }
        } else {
            val network = manager.activeNetwork ?: return false
            val status = manager.getNetworkCapabilities(network) ?: return false
            return status.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
        }

        return false
    }
}