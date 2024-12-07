package com.me.ml.webview.manager

import com.me.ml.webview.fragment.WebViewFragment

internal class WebViewManager private constructor() {
    companion object {
        val instance: WebViewManager by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            WebViewManager()
        }
    }

    private var mWebViewFragment: WebViewFragment? = null
    fun register(webViewFragment: WebViewFragment?) {
        mWebViewFragment = webViewFragment
    }

    fun getFragment(): WebViewFragment? {
        return mWebViewFragment
    }

    fun unRegister() {
        mWebViewFragment = null
    }
}