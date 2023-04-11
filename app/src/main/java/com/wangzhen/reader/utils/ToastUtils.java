package com.wangzhen.reader.utils;

import android.widget.Toast;

import com.wangzhen.reader.MainApplication;

/**
 * Created by wangzhen on 17-5-11.
 */

public class ToastUtils {

    public static void show(String msg){
        Toast.makeText(MainApplication.getContext(), msg, Toast.LENGTH_SHORT).show();
    }
}
