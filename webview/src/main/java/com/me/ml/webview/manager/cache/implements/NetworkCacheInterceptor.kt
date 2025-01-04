package com.me.ml.webview.manager.cache.implements

import android.content.Context
import com.me.ml.webview.bean.cache.WebResource
import com.me.ml.webview.manager.InterceptRequestManager
import com.me.ml.webview.manager.OKHttpManager
import com.me.ml.webview.manager.cache.interfaces.ICacheInterceptor
import com.me.ml.webview.webutils.LogWebViewUtils
import com.me.ml.webview.webutils.WebViewMLUtils

/**
 * 网络缓存拦截器
 */
class NetworkCacheInterceptor(val context: Context?) : ICacheInterceptor {
    override fun cacheInterceptor(chain: ICacheInterceptor.Chain): WebResource? {
        val request = chain.request()

        val mimeType = request.mimeType
        val isCacheContentType = WebViewMLUtils.instance.isCacheContentType(mimeType)
        return context?.let {
            if (WebViewMLUtils.instance.isImageType(request.mimeType)) {
                InterceptRequestManager.instance.loadImage(context, request)
            } else {
                LogWebViewUtils.i("开始网络缓存:${request.url}")
                OKHttpManager(it).getResource(request, isCacheContentType)
            }

        }
    }
}