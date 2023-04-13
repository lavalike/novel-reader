package com.wangzhen.reader.utils

import android.widget.Toast
import java.io.Closeable

/**
 * api-extensions
 * Created by wangzhen on 2023/4/12
 */
fun String.toast() {
    Toast.makeText(AppUtils.getContext(), this, Toast.LENGTH_SHORT).show()
}

fun Closeable.closeIO() {
    IOUtils.close(this)
}