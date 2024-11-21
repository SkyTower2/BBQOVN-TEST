package com.me.ml.logcat;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.TypedValue;

/**
 * Logcat 悬浮窗局部显示派发
 */
final class LogcatLocalDispatcher implements Application.ActivityLifecycleCallbacks {

    static void launch(Application application) {
        application.registerActivityLifecycleCallbacks(new LogcatLocalDispatcher());
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        if (activity instanceof LogcatActivity) {
            return;
        }
        if (isActivityTranslucent(activity)) {
            // Activity是半透明的，则不展示悬浮窗
            return;
        }
        // 在每个 Activity 创建的时候派发
        new LogcatWindow(activity)
                .show();
    }

    @Override
    public void onActivityStarted(Activity activity) {
    }

    @Override
    public void onActivityResumed(Activity activity) {
    }

    @Override
    public void onActivityPaused(Activity activity) {
    }

    @Override
    public void onActivityStopped(Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
    }

    /**
     * 判断当前 Activity 窗口是否为半透明的
     */
    public boolean isActivityTranslucent(Activity activity) {
        TypedValue typedValue = new TypedValue();
        activity.getTheme().resolveAttribute(android.R.attr.windowIsTranslucent, typedValue, true);
        // 当前 android:windowIsTranslucent 为 true 时，typedValue.data 的值为 - 1
        // 如果不设置或者设置为 false，则 typedValue.data 的值为 0
        return typedValue.data != 0;
    }
}