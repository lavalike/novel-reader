package com.wangzhen.reader.model.local

import android.content.Context
import android.database.sqlite.SQLiteDatabase.CursorFactory
import com.wangzhen.reader.model.gen.DaoMaster.DevOpenHelper
import org.greenrobot.greendao.database.Database

/**
 * MyOpenHelper
 * Created by wangzhen on 2023/4/13
 */
class AppOpenHelper(context: Context, name: String, factory: CursorFactory?) :
    DevOpenHelper(context, name, factory) {
    override fun onUpgrade(db: Database, oldVersion: Int, newVersion: Int) {}
}