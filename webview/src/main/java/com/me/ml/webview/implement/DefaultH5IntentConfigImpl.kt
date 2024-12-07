package com.me.ml.webview.implement

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.me.ml.webview.activity.WebViewActivity
import com.me.ml.webview.bean.WebViewConfigBean
import com.me.ml.webview.constants.WebViewConstants
import com.me.ml.webview.interfaces.H5IntentConfig

class DefaultH5IntentConfigImpl : H5IntentConfig {
    override fun startActivity(context: Context?, bean: WebViewConfigBean) {
        context?.startActivity(
            Intent(context, WebViewActivity::class.java)
                .putExtra(WebViewConstants.LIBRARY_WEB_VIEW, bean)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )
    }

    override fun startActivity(context: Context?, intent: Intent) {
        context?.startActivity(intent)
    }


    override fun startActivityForResult(
        context: Activity?,
        bean: WebViewConfigBean,
        requestCode: Int,
    ) {
        val intent = Intent(context, WebViewActivity::class.java)
            .putExtra(WebViewConstants.LIBRARY_WEB_VIEW, bean)
        context?.startActivityForResult(intent, requestCode)
    }

    override fun startActivityForResult(
        context: Fragment?,
        bean: WebViewConfigBean,
        requestCode: Int,
    ) {
        if (context == null || context.context == null) return
        val intent = Intent(context.context, WebViewActivity::class.java)
            .putExtra(WebViewConstants.LIBRARY_WEB_VIEW, bean)
        context.startActivityForResult(intent, requestCode)
    }


    override fun startActivityForResult(
        context: FragmentActivity?,
        launcher: ActivityResultLauncher<Intent>?,
        bean: WebViewConfigBean,
    ) {
        val intent = Intent(context, WebViewActivity::class.java)
            .putExtra(WebViewConstants.LIBRARY_WEB_VIEW, bean)
        launcher?.launch(intent)
    }

    override fun startActivityForResult(
        context: Fragment?,
        launcher: ActivityResultLauncher<Intent>?,
        bean: WebViewConfigBean,
    ) {
        if (context == null || context.context == null) return
        val intent = Intent(context.context, WebViewActivity::class.java)
            .putExtra(WebViewConstants.LIBRARY_WEB_VIEW, bean)
        launcher?.launch(intent)
    }

    override fun startActivityForResult(context: Context?, intent: Intent, requestCode: Int) {
        if (context == null) return
        if (context is FragmentActivity) {
            context.startActivityForResult(intent, requestCode)
        } else if (context is Fragment) {
            context.startActivityForResult(intent, requestCode)
        }
    }
}