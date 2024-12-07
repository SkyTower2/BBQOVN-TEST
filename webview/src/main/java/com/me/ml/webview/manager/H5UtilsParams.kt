package com.me.ml.webview.manager

import android.view.View
import android.webkit.WebSettings
import com.me.ml.webview.activity.BaseWebViewActivity
import com.me.ml.webview.bean.WebViewEvent
import com.me.ml.webview.callback.HandleUrlParamsCallback
import com.me.ml.webview.fragment.WebViewFragment
import com.me.ml.webview.interfaces.LoadingViewConfig
import com.me.ml.webview.sealed.LoadingWebViewState
import com.peakmain.webview.view.PkWebView

class H5UtilsParams private constructor() {
    var preLoadUrl: String = ""
    var mCacheMode: Int = WebSettings.LOAD_NO_CACHE
    var updateToolBarBar: ((String, BaseWebViewActivity?) -> Unit)? = null
    var isShowToolBar: Boolean = true
    var updateStatusBar: ((String, BaseWebViewActivity?) -> Unit)? = null
    var mLoadingWebViewState: LoadingWebViewState? = null
    var mLoadingViewConfig: LoadingViewConfig? = null
    var mHandleUrlParamsCallback: HandleUrlParamsCallback<out WebViewEvent>? = null
    var mHeadContentView: View? = null
    var mHeadContentViewId: Int = 0
    var mHeadViewBlock: ((View) -> Unit)? = null
    var mExecuteJsPair: Triple<String, String, ((PkWebView?, WebViewFragment?) -> Unit)?>? = null
    var mCommonWeResourceResponsePair: Triple<String, String, ((String) -> Boolean)?>? = null

    companion object {
        val instance by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            H5UtilsParams()
        }
    }

    fun clear() {
        updateToolBarBar = null
        isShowToolBar = true
        updateToolBarBar = null
        mLoadingViewConfig = null
        mLoadingWebViewState = null
        mCacheMode = WebSettings.LOAD_DEFAULT
        mExecuteJsPair = null
    }
}