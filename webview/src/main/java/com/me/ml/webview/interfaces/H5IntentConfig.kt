package com.me.ml.webview.interfaces

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.me.ml.webview.bean.WebViewConfigBean

interface H5IntentConfig {
    fun startActivity(context: Context?, bean: WebViewConfigBean)
    fun startActivityForResult(context: Activity?, bean: WebViewConfigBean, requestCode: Int)
    fun startActivityForResult(context: Fragment?, bean: WebViewConfigBean, requestCode: Int)
    fun startActivityForResult(
        context: FragmentActivity?,
        launcher: ActivityResultLauncher<Intent>?,
        bean: WebViewConfigBean,
    )

    fun startActivityForResult(
        context: Fragment?,
        launcher: ActivityResultLauncher<Intent>?,
        bean: WebViewConfigBean,
    )

    fun startActivity(context: Context?, intent: Intent)
    fun startActivityForResult(context: Context?, intent: Intent, requestCode: Int)
}