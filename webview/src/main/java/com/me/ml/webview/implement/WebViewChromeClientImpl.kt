package com.me.ml.webview.implement

import android.webkit.WebView
import com.me.ml.webview.abstracts.AbstractWebViewChromeClient
import com.me.ml.webview.callback.WebViewChromeClientCallback

internal class WebViewChromeClientImpl(webViewChromeClientCallback: WebViewChromeClientCallback?) :
    AbstractWebViewChromeClient(webViewChromeClientCallback) {
    override fun initWebChromeClient(webView: WebView) {
        webView.webChromeClient = WebViewChromeClientImpl(webViewChromeClientCallback)
    }

}