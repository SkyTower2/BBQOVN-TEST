package com.me.ml.utils.log;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.me.ml.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 日志工具类
 * 输出日志，定位错误代码位置
 * 存储日志到本地
 *
 * @author ml
 * @date 2025-01-13 20:20
 */
public class KLog {

    private static boolean IS_SHOW_LOG = false;

    private static final String DEFAULT_MESSAGE = "execute";
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");
    private static final int JSON_INDENT = 4;

    private static final int V = 0x1;
    private static final int D = 0x2;
    private static final int I = 0x3;
    private static final int W = 0x4;
    private static final int E = 0x5;
    private static final int A = 0x6;
    private static final int JSON = 0x7;

    /**
     * 文件路径
     */
    private static String mFilePath;

    /**
     * 文件名
     */
    private static String mFileName = "appLog.txt";

    /**
     * 写日志对象
     */
    private static LogWriter logWriter;

    public static void init(Context context, boolean isShowLog, boolean isLogFile) {
        IS_SHOW_LOG = isShowLog;

        if (isLogFile) {
            KLog.startWriteLogToSdcard(context, null, null, true);
        }
    }

    public static void v() {
        printLog(V, null, DEFAULT_MESSAGE);
    }

    public static void v(Object msg) {
        printLog(V, null, msg);
    }

    public static void v(String tag, String msg) {
        printLog(V, tag, msg);
    }

    public static void d() {
        printLog(D, null, DEFAULT_MESSAGE);
    }

    public static void d(Object msg) {
        printLog(D, null, msg);
    }

    public static void d(String tag, Object msg) {
        printLog(D, tag, msg);
    }

    public static void i() {
        printLog(I, null, DEFAULT_MESSAGE);
    }

    public static void i(Object msg) {
        printLog(I, null, msg);
    }

    public static void i(String tag, Object msg) {
        printLog(I, tag, msg);
    }

    public static void w() {
        printLog(W, null, DEFAULT_MESSAGE);
    }

    public static void w(Object msg) {
        printLog(W, null, msg);
    }

    public static void w(String tag, Object msg) {
        printLog(W, tag, msg);
    }

    public static void e() {
        printLog(E, null, DEFAULT_MESSAGE);
    }

    public static void e(Object msg) {
        printLog(E, null, msg);
    }

    public static void e(String tag, Object msg) {
        printLog(E, tag, msg);
    }

    public static void a() {
        printLog(A, null, DEFAULT_MESSAGE);
    }

    public static void a(Object msg) {
        printLog(A, null, msg);
    }

    public static void a(String tag, Object msg) {
        printLog(A, tag, msg);
    }


    public static void json(String jsonFormat) {
        printLog(JSON, null, jsonFormat);
    }

    public static void json(String tag, String jsonFormat) {
        printLog(JSON, tag, jsonFormat);
    }


    private static void printLog(int type, String tagStr, Object objectMsg) {
        String msg;
        if (!IS_SHOW_LOG) {
            return;
        }

        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

        int index = 4;
        String className = stackTrace[index].getFileName();
        String methodName = stackTrace[index].getMethodName();
        int lineNumber = stackTrace[index].getLineNumber();

        String tag = (tagStr == null ? className : tagStr);
        methodName = methodName.substring(0, 1).toUpperCase() + methodName.substring(1);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[ (").append(className).append(":").append(lineNumber).append(")#").append(methodName).append(" ] ");

        if (objectMsg == null) {
            msg = "Log with null Object";
        } else {
            msg = objectMsg.toString();
        }
        if (msg != null && type != JSON) {
            stringBuilder.append(msg);
        }

        String logStr = stringBuilder.toString();

        switch (type) {
            case V:
                Log.v(tag, logStr);
                break;
            case D:
                Log.d(tag, logStr);
                break;
            case I:
                Log.i(tag, logStr);
                break;
            case W:
                Log.w(tag, logStr);
                break;
            case E:
                Log.e(tag, logStr);
                break;
            case A:
                Log.wtf(tag, logStr);
                break;
            case JSON: {

                if (TextUtils.isEmpty(msg)) {
                    Log.d(tag, "Empty or Null json content");
                    return;
                }

                String message = null;

                try {
                    if (msg.startsWith("{")) {
                        JSONObject jsonObject = new JSONObject(msg);
                        message = jsonObject.toString(JSON_INDENT);
                    } else if (msg.startsWith("[")) {
                        JSONArray jsonArray = new JSONArray(msg);
                        message = jsonArray.toString(JSON_INDENT);
                    }
                } catch (JSONException e) {
                    e(tag, e.getCause().getMessage() + "\n" + msg);
                    return;
                }

                printLine(tag, true);
                message = logStr + LINE_SEPARATOR + message;
                String[] lines = message.split(LINE_SEPARATOR);
                StringBuilder jsonContent = new StringBuilder();
                for (String line : lines) {
                    jsonContent.append("║ ").append(line).append(LINE_SEPARATOR);
                }

                if (jsonContent.toString().length() > 3200) {
                    Log.w(tag, "jsonContent.length = " + jsonContent.toString().length());
                    int chunkCount = jsonContent.toString().length() / 3200;
                    for (int i = 0; i <= chunkCount; i++) {
                        int max = 3200 * (i + 1);
                        if (max >= jsonContent.toString().length()) {

                            Log.w(tag, jsonContent.toString().substring(3200 * i));

                        } else {

                            Log.w(tag, jsonContent.toString().substring(3200 * i, max));

                        }

                    }

                } else {
                    Log.w(tag, jsonContent.toString());

                }
                printLine(tag, false);
            }
            break;
        }

        // 写入本地日志
        KLog.addLog(logStr);
    }

    private static void printLine(String tag, boolean isTop) {
        if (isTop) {
            Log.w(tag, "╔═══════════════════════════════════════════════════════════════════════════════════════");
        } else {
            Log.w(tag, "╚═══════════════════════════════════════════════════════════════════════════════════════");
        }
    }

    /**
     * 写入本地日志线程
     */
    private static class LogWriter extends Thread {
        /**
         * 文件路径
         */
        private final String filePath;

        /**
         * 文件名
         */
        private final String fileName;

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
         * @param fileName 文件名
         * @param pid
         */
        public LogWriter(String filePath, String fileName, int pid) {
            this.filePath = filePath;
            this.fileName = fileName;
            this.mPid = pid;
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
                File file = new File(filePath, fileName);

                if (!file.exists()) {
                    file.getParentFile().mkdirs();
                    file.createNewFile();
                }
                writer = new FileWriter(file, true);

                //循环写入文件
                String log = null;
                while (isRunning || !logQueue.isEmpty()) {

                    //使用 poll() 不行？需要使用 take() 阻塞获取日志
                    log = logQueue.take();

                    if (log != null && !log.isEmpty()) {
                        String timestamp = sdf.format(new Date(System.currentTimeMillis()));
                        writer.append("PID:" + this.mPid + "\t"
                                + timestamp + "\t" + log
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
     * @param filePath  要写入的目的文件路径
     * @param fileName  要写入的目的文件名
     * @param isShowLog 是否需要写入
     */
    public static void startWriteLogToSdcard(Context context, String filePath, String fileName, boolean isShowLog) {
        if (isShowLog) {
            if (logWriter == null) {
                // 默认文件路径
                if (filePath == null || filePath.isEmpty()) {
                    mFilePath = context.getExternalFilesDir(null).getAbsolutePath() + "/logs/";
                } else {
                    mFilePath = filePath;
                }

                try {
                    //LogUtil这个类的pid,必须在类外面得到
                    logWriter = new LogWriter(mFilePath, fileName == null ? mFileName : fileName, android.os.Process.myPid());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            logWriter.start();
        }
    }

    /**
     * 整个应用只需要调用一次即可: 停止写日志到本地文件
     */
    public static void endWriteLogToSdcard() {
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
    public static void addLog(String logMessage) {
        if (logWriter != null) {
            logWriter.addLog(logMessage);
        }
    }

    /**
     * 打开日志文件
     *
     * @param context 上下文对象
     */
    public static void openLogFile(Context context) {
        try {
            File file = new File(mFilePath, mFileName);
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

    /**
     * 清除日志文件
     *
     * @param context 上下文对象
     */
    public static void clearLogFile(Context context) {
        File logFile = new File(mFilePath, mFileName);

        // 判断文件是否存在且为文件
        if (logFile.exists() && logFile.isFile()) {
            try {
                // 打开文件输出流，设置为追加模式为 false，以清空文件内容
                FileOutputStream fos = new FileOutputStream(logFile, false);
                fos.close();

                Toast.makeText(context, "清理成功", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

}