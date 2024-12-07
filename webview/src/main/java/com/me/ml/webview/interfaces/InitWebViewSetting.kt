package com.me.ml.webview.interfaces

import android.webkit.WebView

interface InitWebViewSetting {
    fun initWebViewSetting(webView: WebView, userAgent: String? = null)
}