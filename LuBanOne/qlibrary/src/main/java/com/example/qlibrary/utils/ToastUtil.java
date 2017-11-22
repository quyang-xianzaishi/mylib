package com.example.qlibrary.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Administrator on 2017/5/28.
 */
public class ToastUtil {

    private static Toast mToast;

    /**
     *
     * @param msg
     * @param context 这里的context最好用context.getApplicationContext
     */
    public static void showShort(String msg, Context context) {
        if (null == mToast) {
            mToast = Toast.makeText(context.getApplicationContext(), msg, Toast.LENGTH_LONG);
        } else {
            mToast.setText(msg);
        }
        mToast.show();
    }

}
