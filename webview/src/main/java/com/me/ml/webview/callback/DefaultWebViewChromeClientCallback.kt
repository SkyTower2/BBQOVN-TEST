package com.me.ml.webview.callback

import android.net.Uri
import android.webkit.ValueCallback
import android.webkit.WebView
import com.me.ml.webview.fragment.WebViewFragment
import com.me.ml.webview.utils.LogWebViewUtils

class DefaultWebViewChromeClientCallback : WebViewChromeClientCallback {
    override fun onReceivedTitle(view: WebView?, title: String?, fragment: WebViewFragment?) {
        LogWebViewUtils.e("收到标题:$title")
    }

    override fun openFileInput(
        fileUploadCallbackFirst: ValueCallback<Uri>?,
        fileUploadCallbackSecond: ValueCallback<Array<Uri>>?,
        acceptType: String?
    ) {
    }

    override fun onProgressChanged(view: WebView?, newProgress: Int, fragment: WebViewFragment?) {
        LogWebViewUtils.e("进度条发生变化：$newProgress")
    }
}