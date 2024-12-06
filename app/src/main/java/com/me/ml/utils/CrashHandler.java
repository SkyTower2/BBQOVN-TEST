package com.me.ml.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Looper;

import com.me.ml.MainActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 全局异常处理
 *
 * @author ml
 * @date 2024-12-06 10:24
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {

    /***
     * 在Application中使用
     * 初始化全局异常处理，崩溃自启
     * CrashHandler.getInstance(this);
     */
    private static CrashHandler INSTANCE;
    private Context mContext;
    private Thread.UncaughtExceptionHandler mDefaultUncaughtExceptionHandler;    // 用来存储设备信息和异常信息
    private Map<String, String> mErrorInfoMap = new LinkedHashMap<>();    // 用于格式化日期,作为日志文件名的一部分
    private SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
    private String mDivider = "==============divider==============";

    private CrashHandler() {
    }

    public static CrashHandler getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new CrashHandler();
            INSTANCE.mContext = context;
            INSTANCE.mDefaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
            Thread.setDefaultUncaughtExceptionHandler(INSTANCE);
        }
        return INSTANCE;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultUncaughtExceptionHandler != null) {
            mDefaultUncaughtExceptionHandler.uncaughtException(thread, ex);
        } else {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                // DLog.e("error : ", e);
            }

            // 退出程序,注释下面的重启启动程序代码
            //            android.os.Process.killProcess(android.os.Process.myPid());
            //            System.exit(1);
            ex.printStackTrace();

            // 重新启动程序，注释上面的退出程序
            restartApp();
        }
    }

    //异常崩溃  重新启动程序
    private void restartApp() {
        Intent intent = new Intent();
        intent.setClass(mContext, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }

    public boolean handleException(Throwable ex) {
        if (null == ex) {
            return false;
        }        // 使用 Toast 来显示异常信息
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                // ToastUtils.show(mContext, "出现未知错误");
                Looper.loop();
            }
        }.start();
        //记录设备参数信息
        // collectDeviceInfo(mContext);
        // 保存日志文件
        // saveCrashInfo2File(ex);
        return true;
    }

    /**
     * 收集设备参数信息
     *
     * @param ctx
     */
    public void collectDeviceInfo(Context ctx) {
        try {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName == null ? "null" : pi.versionName;
                String versionCode = pi.versionCode + "";
                mErrorInfoMap.put("versionName", versionName);
                mErrorInfoMap.put("versionCode", versionCode);
            }
        } catch (
                PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            //  DLog.e(e);
        }

        mErrorInfoMap.put("=", mDivider);

        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                mErrorInfoMap.put(field.getName(), field.get(null).toString());
                // DLog.v(field.getName() + " : " + field.get(null));
            } catch (Exception e) {
                //  DLog.e("an error occured when collect crash info ", e);
            }
        }
    }

    /**
     * 保存错误信息到文件中
     *
     * @param ex
     * @return 返回文件名称, 便于将文件传送到服务器
     */
    private String saveCrashInfo2File(Throwable ex) {
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : mErrorInfoMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key + "=" + value + "\n");
        }
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        sb.append(mDivider).append("\n");
        sb.append(result);
        try {
            long timestamp = System.currentTimeMillis();
            String time = mSimpleDateFormat.format(new Date());
            String fileName = "crash-" + time + "-" + timestamp + ".log";
            String logDir = "log";
            File path = new File(mContext.getExternalCacheDir() + File.separator + logDir + File.separator + fileName);
            if (!path.getParentFile().exists()) {
                path.getParentFile().mkdirs();
            }
            FileOutputStream fos = new FileOutputStream(path);
            fos.write(sb.toString().getBytes());
            fos.close();
            return fileName;
        } catch (Exception e) {
            //  DLog.e("an error occured while writing file... ", e);
        }
        return null;
    }
}
