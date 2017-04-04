package com.example.com.zhuangbi.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Administrator on 2017/2/26.
 */

public class ToastUtils {

    /**
     * 长吐司
     * @param context
     * @param text
     */

    public static void showLongToast(Context context,String text){
        Toast.makeText(context,text,Toast.LENGTH_LONG).show();
    }

    /**
     * 显示短吐司
     * @param context
     * @param text
     */
    public static void showShortToast(Context context,String text){
        Toast.makeText(context,text,Toast.LENGTH_SHORT).show();
    }
}
