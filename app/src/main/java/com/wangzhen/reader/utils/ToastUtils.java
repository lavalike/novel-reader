package com.wangzhen.reader.utils;

import android.widget.Toast;

/**
 * Created by wangzhen on 17-5-11.
 */

public class ToastUtils {

    public static void show(String msg) {
        Toast.makeText(AppUtils.getContext(), msg, Toast.LENGTH_SHORT).show();
    }
}
