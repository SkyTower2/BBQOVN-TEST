package com.me.ml.webview.webutils

import android.util.Log

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