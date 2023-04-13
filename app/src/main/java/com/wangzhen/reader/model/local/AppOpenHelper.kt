package com.wangzhen.reader.model.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.wangzhen.reader.model.gen.DaoMaster;

import org.greenrobot.greendao.database.Database;

/**
 * Created by wangzhen on 2017/10/9.
 */
public class MyOpenHelper extends DaoMaster.DevOpenHelper {
    public MyOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {

    }
}
