package com.me.ml.utils.log;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.me.ml.utils.Utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 日志工具类
 * 存储Log日志文件到本地
 *
 * @author ml
 * @date 2025-01-13 16:49
 */
public class LogUtil {
    /* ========================下面的是本地存储相关的========================== */

    /**
     * 单例对象
     */
    private static volatile LogUtil instance;

    /**
     * 文件路径
     */
    private String mFilePath = Utils.getApp().getExternalFilesDir(null).getPath();

    /**
     * 写日志对象
     */
    private LogWriter logWriter;

    /**
     * 私有构造方法，防止外部直接实例化
     */
    private LogUtil() {
    }

    /**
     * 提供全局访问点
     *
     * @return
     */
    public static LogUtil getInstance() {
        if (instance == null) {
            synchronized (LogUtil.class) {
                if (instance == null) {
                    instance = new LogUtil();
                }
            }
        }
        return instance;
    }

    /**
     * 写入本地日志线程
     */
    private class LogWriter extends Thread {
        /**
         * 文件路径
         */
        private String mFilePath;

        /**
         * 调用这个类的线程
         */
        private int mPid;

        /**
         * 线程运行标志
         */
        private volatile boolean isRunning = true;

        /**
         * 日志队列
         */
        private final BlockingQueue<String> logQueue = new LinkedBlockingQueue<>();


        /**
         * @param filePath 文件路径
         * @param pid
         */
        public LogWriter(String filePath, int pid) {
            this.mPid = pid;
            this.mFilePath = filePath;
        }

        /**
         * 添加日志信息到队列
         */
        public void addLog(String logMessage) {
            if (isRunning) {
                logQueue.offer(logMessage);
            }
        }

        @Override
        public void run() {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.CHINA);//日期格式化对象

            FileWriter writer = null;
            try {
                //创建文件
                File file = new File(mFilePath);
                if (!file.exists()) {
                    file.getParentFile().mkdirs();
                    file.createNewFile();
                }
                writer = new FileWriter(file, true);

                //循环写入文件
                String log = null;
                while (isRunning || !logQueue.isEmpty()) {
                    log = logQueue.poll();
                    if (log != null && log.length() > 0) {
                        String timestamp = sdf.format(new Date(System.currentTimeMillis()));
                        writer.append("PID:" + this.mPid + "\t"
                                + sdf.format(timestamp) + "\t" + log
                                + "\n");
                        writer.flush();
                    } else {
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (writer != null) {
                    try {
                        writer.flush();
                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                writer = null;
            }
        }

        public void end() {
            isRunning = false;
        }
    }

    /**
     * 整个应用只需要调用一次即可:开始本地记录
     *
     * @param filePath 要写入的目的文件路径
     * @param iswrite  是否需要写入
     */
    public void startWriteLogToSdcard(String filePath, boolean iswrite) {

        if (iswrite) {
            if (logWriter == null) {
                try {
                    //LogUtil这个类的pid,必须在类外面得到
                    logWriter = new LogWriter(mFilePath, android.os.Process.myPid());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                logWriter.start();
            }
        }
    }

    /**
     * 整个应用只需要调用一次即可: 停止写日志到本地文件
     */
    public void endWriteLogToSdcard() {
        if (logWriter != null) {
            logWriter.end();
            logWriter = null;
        }
    }

    /**
     * 添加日志到本地文件
     *
     * @param logMessage 日志信息
     */
    public void addLog(String logMessage) {
        if (logWriter != null) {
            logWriter.addLog(logMessage);
        }
    }

    /**
     * 打开日志文件
     *
     * @param context 上下文对象
     */
    public void openLogFile(Context context) {
        try {
            File file = new File(mFilePath);
            if (file.exists()) {
                // 主APP包名
                String authorities = "com.oyml.bluetooth";

                // 使用 FileProvider 生成 URI（适配 Android 7.0 及以上版本）
                Uri fileUri = FileProvider.getUriForFile(context,
                        authorities + ".fileprovider", file);

                // 创建 Intent 打开文件
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(fileUri, "text/plain"); // 文件类型为文本
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                context.startActivity(intent);
            } else {
                Toast.makeText(context, "日志文件不存在: " + mFilePath, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "无法打开日志文件", Toast.LENGTH_SHORT).show();
        }
    }
}
