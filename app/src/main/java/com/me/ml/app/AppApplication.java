package com.me.ml.app;

import android.app.Application;
import android.content.Intent;
import android.os.Build;

import com.me.ml.MainActivity;
import com.me.ml.bluetooth_kit.BluetoothContext;
import com.me.ml.mvvm.base.BaseApplication;

import com.me.ml.mvvm.crash.CaocConfig;
import com.me.ml.service.BbqovnService;
import com.me.ml.utils.CrashHandler;
import com.me.ml.utils.log.KLog;
import com.me.ml.utils.mmkv.MMKVUtils;
import com.oyml.bluetooth.R;


/**
 * @author ml
 * @date 2024/9/7 10:28
 */
public class AppApplication extends BaseApplication {

    private static AppApplication instance;

    public static Application getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        //初始化MMKV
        MMKVUtils.init(this);

        //初始化蓝牙库
        BluetoothContext.set(this);

        //是否开启打印日志
        KLog.init(true);

        initCrash();

        //初始化崩溃异常捕获
        CrashHandler.getInstance(this);

        startService();
        //内存泄漏检测
//        if (!LeakCanary.isInAnalyzerProcess(this)) {
//            LeakCanary.install(this);
//        }
    }

    /**
     * 初始化前台服务
     */
    public void startService() {
        //启动前台服务
        Intent integer = new Intent(this, BbqovnService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(integer);
        } else {
            startService(integer);
        }
    }

    /**
     * 初始化崩溃异常捕获
     */
    private void initCrash() {
        CaocConfig.Builder.create().backgroundMode(CaocConfig.BACKGROUND_MODE_SILENT) //背景模式,开启沉浸式
                .enabled(true) //是否启动全局异常捕获
                .showErrorDetails(true) //是否显示错误详细信息
                .showRestartButton(true) //是否显示重启按钮
                .trackActivities(true) //是否跟踪Activity
                .minTimeBetweenCrashesMs(2000) //崩溃的间隔时间(毫秒)
                .errorDrawable(R.mipmap.ic_launcher) //错误图标
                .restartActivity(MainActivity.class) //重新启动后的activity
//                .errorActivity(YourCustomErrorActivity.class) //崩溃后的错误activity
//                .eventListener(new YourCustomEventListener()) //崩溃后的错误监听
                .apply();
    }
}
