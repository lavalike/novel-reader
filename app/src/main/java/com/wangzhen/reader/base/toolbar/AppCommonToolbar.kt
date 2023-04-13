package com.wangzhen.reader.base.toolbar

import android.app.Activity
import android.view.View
import com.wangzhen.commons.toolbar.impl.Toolbar
import com.wangzhen.reader.R
import com.wangzhen.reader.databinding.AppToolbarCommonBinding

/**
 * AppCommonToolbar
 * Created by wangzhen on 2023/4/10
 */
class AppCommonToolbar(anchor: Activity, private val text: String) : Toolbar(anchor) {
    override fun layoutRes() = R.layout.app_toolbar_common

    override fun onViewCreated(view: View) {
        AppToolbarCommonBinding.bind(view).apply {
            btnBack.setOnClickListener { activity.finish() }
            title.text = text
        }
    }
}