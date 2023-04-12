package com.wangzhen.reader.ui.base

import com.wangzhen.commons.toolbar.ToolbarActivity
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * BaseActivity
 * Created by wangzhen on 2023/4/11
 */
open class BaseActivity : ToolbarActivity() {
    private var disposables = CompositeDisposable()
    protected fun addDisposable(d: Disposable) {
        disposables.add(d)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }
}