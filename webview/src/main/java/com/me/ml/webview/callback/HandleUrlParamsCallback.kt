package com.me.ml.webview.callback

import android.net.Uri
import com.me.ml.webview.bean.WebViewEvent

interface HandleUrlParamsCallback<T : WebViewEvent> {
    fun handleUrlParamsCallback(uri: Uri?, path: String?): T
}