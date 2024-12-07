package com.me.ml.webview.manager.cache

import android.content.Context
import com.me.ml.webview.bean.cache.CacheRequest
import com.me.ml.webview.bean.cache.WebResource
import com.me.ml.webview.manager.cache.implements.DiskCacheInterceptor
import com.me.ml.webview.manager.cache.implements.MemoryCacheIntercept
import com.me.ml.webview.manager.cache.implements.NetworkCacheInterceptor
import com.me.ml.webview.manager.cache.interfaces.ICacheInterceptor
import com.me.ml.webview.manager.cache.interfaces.ICall

/**
 * 管理拦截器
 */
class RealCacheInterfaceCall(
    private val context: Context?,
    private val cacheRequest: CacheRequest
) : ICall {
    override fun call(): WebResource? {
        val cacheInterceptorList = ArrayList<ICacheInterceptor>()
        cacheInterceptorList.add(MemoryCacheIntercept())
        cacheInterceptorList.add(DiskCacheInterceptor(context))
        cacheInterceptorList.add(NetworkCacheInterceptor(context))
        val realCacheInterfaceChain = RealCacheInterfaceChain(cacheInterceptorList, 0, cacheRequest)
        return realCacheInterfaceChain.process(cacheRequest)
    }

}