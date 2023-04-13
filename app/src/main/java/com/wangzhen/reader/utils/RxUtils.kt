package com.wangzhen.reader.utils

import io.reactivex.Single
import io.reactivex.SingleSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * RxUtils
 * Created by wangzhen on 2023/4/13
 */
object RxUtils {
    @JvmStatic
    fun <T> toSimpleSingle(upstream: Single<T>): SingleSource<T> =
        upstream.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
}