package com.me.ml.webview.callback

import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import com.me.ml.webview.fragment.WebViewFragment

interface WebViewClientCallback {
    fun onPageStarted(view: WebView, url: String, fragment: WebViewFragment?)
    fun onPageFinished(view: WebView, url: String, fragment: WebViewFragment?)
    fun shouldOverrideUrlLoading(view: WebView, url: String, fragment: WebViewFragment?): Boolean?
    fun onReceivedError(
        view: WebView?,
        err: Int,
        des: String?,
        url: String?,
        fragment: WebViewFragment?
    )

    fun shouldInterceptRequest(view: WebView?, request: WebResourceRequest): WebResourceResponse?
}