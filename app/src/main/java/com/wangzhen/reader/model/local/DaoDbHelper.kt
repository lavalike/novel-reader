package com.wangzhen.reader.model.local

import android.database.sqlite.SQLiteDatabase
import com.wangzhen.reader.model.gen.DaoMaster
import com.wangzhen.reader.model.gen.DaoSession
import com.wangzhen.utils.utils.AppUtils

/**
 * DaoDbHelper
 * Created by wangzhen on 2023/4/13
 */
object DaoDbHelper {
    private const val DB_NAME = "reader-db"

    private val database: SQLiteDatabase =
        AppOpenHelper(AppUtils.getContext(), DB_NAME, null).writableDatabase

    private val mDaoMaster: DaoMaster = DaoMaster(database)

    val session: DaoSession = mDaoMaster.newSession()
}