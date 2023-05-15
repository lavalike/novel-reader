package com.wangzhen.reader.utils

import android.content.Context
import android.content.SharedPreferences
import com.wangzhen.utils.utils.AppUtils

/**
 * SharedPreUtils
 * Created by wangzhen on 2023/4/13
 */
object SharedPreUtils {
    private const val SHARED_NAME = "IReader_pref"

    private val sharedReadable =
        AppUtils.getContext().getSharedPreferences(SHARED_NAME, Context.MODE_PRIVATE)

    private val editor: SharedPreferences.Editor = sharedReadable.edit()

    fun getString(key: String) = sharedReadable.getString(key, "")

    fun putString(key: String, value: String) {
        editor.putString(key, value)
        editor.commit()
    }

    fun putInt(key: String, value: Int) {
        editor.putInt(key, value)
        editor.commit()
    }

    fun putBoolean(key: String, value: Boolean) {
        editor.putBoolean(key, value)
        editor.commit()
    }

    fun getInt(key: String, def: Int) = sharedReadable.getInt(key, def)

    fun getBoolean(key: String, def: Boolean) = sharedReadable.getBoolean(key, def)
}