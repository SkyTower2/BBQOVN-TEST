package com.me.ml.webview.manager.cache.interfaces

import com.me.ml.webview.bean.cache.WebResource

interface ICall {
    fun call(): WebResource?
}