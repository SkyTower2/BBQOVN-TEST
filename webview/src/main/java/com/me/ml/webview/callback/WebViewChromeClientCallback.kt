package com.me.ml.webview.callback

import android.net.Uri
import android.webkit.ValueCallback
import android.webkit.WebView
import com.me.ml.webview.fragment.WebViewFragment

interface WebViewChromeClientCallback {
    fun onReceivedTitle(view: WebView?, title: String?, fragment: WebViewFragment?)
    fun openFileInput(
        fileUploadCallbackFirst: ValueCallback<Uri>?,
        fileUploadCallbackSecond: ValueCallback<Array<Uri>>?,
        acceptType: String?
    )

    fun onProgressChanged(view: WebView?, newProgress: Int, fragment: WebViewFragment?)
}