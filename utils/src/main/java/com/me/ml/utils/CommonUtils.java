package com.me.ml.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.widget.Toast;

import androidx.core.content.ContextCompat;


/**
 * @author ml
 * @date 2024/9/7 10:28
 */
public class CommonUtils {

    /**
     * 显示短时间Toast
     */
    public static void toast(String text) {
        Toast.makeText(Utils.getApp(), text, Toast.LENGTH_SHORT).show();
    }

    /**
     * 显示长时间Toast
     */
    public static void toastLong(String text) {
        Toast.makeText(Utils.getApp(), text, Toast.LENGTH_LONG).show();
    }

    /**
     * 获取指定资源ID的图片
     */
    public static Drawable getDrawable(Context context, int resId) {
        return ContextCompat.getDrawable(context, resId);
    }

    /**
     * 获取指定资源ID的颜色值
     */
    public static int getColor(Context context, int resId) {
        return ContextCompat.getColor(context, resId);
    }

    /**
     * 获取Application的Resources对象
     */
    public static Resources getResources() {
        return Utils.getApp().getResources();
    }

    /**
     * 获取字符串
     * 调用 getResources() 方法获取 Resources 对象
     * 使用 resId 从 Resources 对象中获取字符串
     */
    public static String getString(int resId) {
        return getResources().getString(resId);
    }

    /**
     * 获取字符串
     * 使用传入的 Context 对象的 getResources() 方法获取 Resources 对象
     * 使用 resId 从 Resources 对象中获取字符串
     */
    public static String getString(Context context, int resId) {
        return context.getResources().getString(resId);
    }

    /**
     * 获取指定ID的尺寸值
     */
    public static float getDimens(int resId) {
        return getResources().getDimension(resId);
    }
}
