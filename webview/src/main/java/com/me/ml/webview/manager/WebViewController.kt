package com.me.ml.webview.manager

import android.app.Application
import android.view.View
import com.me.ml.webview.R
import com.me.ml.webview.bean.WebViewEvent
import com.me.ml.webview.callback.DefaultWebViewChromeClientCallback
import com.me.ml.webview.callback.DefaultWebViewClientCallback
import com.me.ml.webview.callback.HandleUrlParamsCallback
import com.me.ml.webview.callback.WebViewChromeClientCallback
import com.me.ml.webview.callback.WebViewClientCallback
import com.me.ml.webview.implement.init.DefaultInitWebViewSetting
import com.me.ml.webview.implement.loading.ProgressLoadingConfigImpl
import com.me.ml.webview.interfaces.InitWebViewSetting
import com.me.ml.webview.interfaces.LoadingViewConfig
import com.me.ml.webview.sealed.LoadingWebViewState

class WebViewController {
    var P: WebViewParams? = null
        private set


    class WebViewParams(val application: Application) {


        var notCacheUrlArray: Array<String>? = null
        var mHandleUrlParamsCallback: HandleUrlParamsCallback<out WebViewEvent>? = null
        var mWebViewCount: Int = 3
        var userAgent: String = ""
        var mWebViewSetting: InitWebViewSetting = DefaultInitWebViewSetting()

        var mWebViewClientCallback: WebViewClientCallback = DefaultWebViewClientCallback()
        var mWebViewChromeClientCallback: WebViewChromeClientCallback =
            DefaultWebViewChromeClientCallback()

        //默认不显示Loading
        var mLoadingWebViewState: LoadingWebViewState = LoadingWebViewState.NotLoading
        var mLoadingViewConfig: LoadingViewConfig = ProgressLoadingConfigImpl()
        var mNoNetWorkView: View? = null
        var mNoNetWorkViewId: Int = R.layout.webview_no_network
        var mNetWorkViewBlock: ((View?, View?, String?) -> Unit)? = null
        var mEntities: Array<out Class<*>>? = null
        fun apply(controller: WebViewController, P: WebViewParams) {
            controller.P = P
        }
    }

}