package com.me.ml.webview.manager.cache.interfaces

import com.me.ml.webview.bean.cache.CacheRequest
import com.me.ml.webview.bean.cache.WebResource

interface ICacheInterceptor {
    //每一层需要处理的逻辑
    fun cacheInterceptor(chain: Chain): WebResource?
    interface Chain {
        //获取上一层请求的请求体
        fun request(): CacheRequest

        //交给下层处理
        fun process(request: CacheRequest): WebResource?
    }
}