package com.wangzhen.reader.ui.base

import android.os.Bundle
import com.wangzhen.commons.toolbar.ToolbarActivity
import com.wangzhen.statusbar.DarkStatusBar
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * BaseActivity
 * Created by wangzhen on 2023/4/11
 */
open class BaseActivity : ToolbarActivity() {
    private var disposables = CompositeDisposable()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fitDarkStatusBar()
    }

    protected fun addDisposable(d: Disposable) {
        disposables.add(d)
    }

    protected fun fitDarkStatusBar() {
        DarkStatusBar.get().fitDark(this)
    }

    protected fun fitLightStatusBar() {
        DarkStatusBar.get().fitLight(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }
}