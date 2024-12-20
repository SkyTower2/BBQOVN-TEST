package com.me.ml.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.text.TextUtils
import androidx.core.app.NotificationCompat
import com.me.ml.MainActivity
import com.oyml.bluetooth.R


/**
 * @author ml
 * @date 2024-11-28 16:29
 * description: 前台保活服务
 */
class ForegroundNF(private val service: ForegroundCoreService) : ContextWrapper(service) {
    companion object {
        private const val START_ID = 101
        private const val CHANNEL_ID = "app_foreground_service"
        private const val CHANNEL_NAME = "前台保活服务"
    }

    private var mNotificationManager: NotificationManager? = null

    private var mCompatBuilder: NotificationCompat.Builder? = null

    private val compatBuilder: NotificationCompat.Builder?
        get() {
            if (mCompatBuilder == null) {
                val notificationIntent = Intent(this, MainActivity::class.java)
                notificationIntent.action = Intent.ACTION_MAIN
                notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER)
                notificationIntent.flags =
                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED

                // 动作意图
                val pendingIntentFlags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                } else {
                    PendingIntent.FLAG_UPDATE_CURRENT
                }

                val pendingIntent = PendingIntent.getActivity(
                    this,
                    (Math.random() * 10 + 10).toInt(),
                    notificationIntent,
                    pendingIntentFlags
                )

                val notificationBuilder: NotificationCompat.Builder =
                    NotificationCompat.Builder(this, CHANNEL_ID)

                var applicationName: String? = getApplicationName()
                if (TextUtils.isEmpty(applicationName)) {
                    applicationName = packageName
                }

                //标题
                notificationBuilder.setContentTitle(getString(R.string.app_name))
                //通知内容
                notificationBuilder.setContentText(
                    getString(
                        R.string.logcat_notify_content,
                        applicationName
                    )
                )
                //状态栏显示的小图标
                notificationBuilder.setSmallIcon(R.mipmap.ic_launcher)
                //通知内容打开的意图
                notificationBuilder.setContentIntent(pendingIntent)
                mCompatBuilder = notificationBuilder
            }
            return mCompatBuilder
        }

    init {
        createNotificationChannel()
    }

    private fun getApplicationName(): String {
        val packageManager = packageManager
        try {
            return packageManager.getApplicationLabel(
                packageManager
                    .getApplicationInfo(packageName, PackageManager.GET_META_DATA)
            ).toString()
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return ""
    }

    //创建通知渠道
    private fun createNotificationChannel() {
        mNotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        //针对8.0+系统
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW
            )
            channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            channel.setShowBadge(false)
            mNotificationManager?.createNotificationChannel(channel)
        }
    }

    //开启前台通知
    fun startForegroundNotification() {
        service.startForeground(START_ID, compatBuilder?.build())
    }

    //停止前台服务并清除通知
    fun stopForegroundNotification() {
        mNotificationManager?.cancelAll()
        service.stopForeground(true)
        //AppManager.getInstance().exit()
    }
}