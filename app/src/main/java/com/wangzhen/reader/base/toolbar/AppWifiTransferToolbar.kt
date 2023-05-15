package com.wangzhen.reader.base.toolbar

import android.app.Activity
import android.view.View
import com.wangzhen.commons.toolbar.impl.Toolbar
import com.wangzhen.reader.R
import com.wangzhen.reader.databinding.AppToolbarCommonBinding
import com.wangzhen.reader.databinding.AppToolbarWifiTransferBinding

/**
 * AppWifiTransferToolbar
 * Created by wangzhen on 2023/5/15
 */
class AppWifiTransferToolbar(anchor: Activity, private val text: String) : Toolbar(anchor) {
    override fun layoutRes() = R.layout.app_toolbar_wifi_transfer

    override fun onViewCreated(view: View) {
        AppToolbarWifiTransferBinding.bind(view).apply {
            btnBack.setOnClickListener { activity.finish() }
            title.text = text
        }
    }
}