package com.me.ml.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * @author ml
 * @date 2024/10/10 14:26
 */
public class VersionUtils {
    public static String getVersionName(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName != null ? packageInfo.versionName : "Unknown";
        } catch (PackageManager.NameNotFoundException e) {
            return "Unknown";
        }
    }
}
