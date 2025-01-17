package com.me.ml.webview.manager.cache

import com.me.ml.webview.bean.cache.CacheRequest
import com.me.ml.webview.bean.cache.WebResource
import com.me.ml.webview.manager.cache.interfaces.ICacheInterceptor

/**
 * 处理拦截器逻辑的地方
 */
class RealCacheInterfaceChain(
    private val cacheInterfaceList: MutableList<ICacheInterceptor>,//所有的拦截器
    private val index: Int,//当前拦截器的index
    val request: CacheRequest,//请求参数
) : ICacheInterceptor.Chain {
    override fun request(): CacheRequest {
        return request
    }

    override fun process(request: CacheRequest): WebResource? {
        //所有拦截器的中转地
        if (index >= cacheInterfaceList.size) throw AssertionError()
        //获取到当前的拦截器
        val currentCacheInterface = cacheInterfaceList[index]
        //获取下个拦截器
        val nextCacheInterface =
            RealCacheInterfaceChain(cacheInterfaceList, index + 1, request)
        //调用当前拦截器的process，并将下一个拦截器传给当前拦截器
        return currentCacheInterface.cacheInterceptor(nextCacheInterface)
    }

}