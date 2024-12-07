package com.me.ml.webview.utils

import android.util.Log
import com.peakmain.res.BuildConfig

object LogWebViewUtils {
    private const val TAG = "PKWebView"
    @JvmStatic
    fun e(message: String) {
        if (BuildConfig.DEBUG) {
            Log.e(TAG, message)
        }
    }
    @JvmStatic
    fun i(message: String) {
        if (BuildConfig.DEBUG) {
            Log.e(TAG, message)
        }
    }
}