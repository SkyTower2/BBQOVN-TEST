package com.me.ml.webview.callback

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.MutableContextWrapper
import android.net.Uri
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import com.me.ml.webview.fragment.WebViewFragment
import com.me.ml.webview.manager.H5UtilsParams
import com.me.ml.webview.manager.cache.WebResourceResponseManager
import com.me.ml.webview.webutils.LogWebViewUtils
import com.peakmain.webview.view.PkWebView

class DefaultWebViewClientCallback : WebViewClientCallback {
    val params = H5UtilsParams.instance

    override fun onPageFinished(view: WebView, url: String, fragment: WebViewFragment?) {
        LogWebViewUtils.e("再次來到onPageFinished")
    }

    override fun shouldOverrideUrlLoading(
        view: WebView,
        url: String,
        fragment: WebViewFragment?,
    ): Boolean? {
        val activity = fragment?.activity
        //处理电话功能
        if (url.startsWith("tel")) {
            try {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                activity?.startActivity(intent)
                return true
            } catch (ex: ActivityNotFoundException) {
                ex.printStackTrace()
            }
        }
        if (url.startsWith("mailto:")) {
            if (activity != null) {
                val emailUri = Uri.parse(url)
                val emailIntent = Intent(Intent.ACTION_SENDTO, emailUri)
                activity.startActivity(emailIntent)
                return true
            }
            return true
        }
        // 对支付宝和微信的支付页面点击做特殊处理
        if (url.contains("alipays://platformapi") || url.contains("weixin://wap/pay?")) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            activity?.startActivity(intent)
            return true
        }
        //处理外部链接
        return null
    }

    override fun onReceivedError(
        view: WebView?,
        err: Int,
        des: String?,
        url: String?,
        fragment: WebViewFragment?,
    ) {
        LogWebViewUtils.e("错误码：$err,错误信息:$des,错误url:$url")


    }

    override fun shouldInterceptRequest(
        view: WebView?,
        request: WebResourceRequest,
    ): WebResourceResponse? {
        request.url ?: return null
        if (!request.method.equals("GET", true)) {
            return null
        }
        return WebResourceResponseManager.getResponse(
            (view?.context as MutableContextWrapper?)?.baseContext,
            request,
            (view as PkWebView?)?.getWebViewParams()?.userAgent,
            view
        )
        /*        if (url.scheme != "https" && url.scheme != "http") {
                    return null
                }
                var response: WebResourceResponse? = null
                val commonWeResourceResponsePair = params.mCommonWeResourceResponsePair
                view?.run {
                    commonWeResourceResponsePair?.let {
                        val isCommonResource = it.third ?: return@let
                        if (isCommonResource(url.toString())) {
                            response = InterceptRequestManager.instance.getLocalWebResourceResponse(it.first, it.second)
                        }
                    } ?: if (WebViewUtils.instance.isCacheType(url.toString())) {
                        InterceptRequestManager.instance.getWebResourceResponse(request) {
                            response = it
                        }
                    } else {
                        response = null
                    }
                }
                return response*/
    }


    override fun onPageStarted(view: WebView, url: String, fragment: WebViewFragment?) {
        LogWebViewUtils.e("再次來到onPageStart")
    }

}