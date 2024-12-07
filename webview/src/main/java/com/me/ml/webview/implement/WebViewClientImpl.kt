package com.me.ml.webview.implement

import android.webkit.WebView
import com.me.ml.webview.abstracts.AbstractWebViewClient
import com.me.ml.webview.callback.WebViewClientCallback

internal class WebViewClientImpl(webViewClientCallback: WebViewClientCallback?) :
    AbstractWebViewClient(webViewClientCallback) {
    override fun initWebClient(webView: WebView) {
        val webViewClient = WebViewClientImpl(webViewClientCallback)
        webView.webViewClient = webViewClient
    }
}