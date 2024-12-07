package com.me.ml.webview

import android.view.View
import androidx.annotation.LayoutRes
import com.me.ml.webview.abstracts.AbstractH5IntentConfigDecorator
import com.me.ml.webview.activity.BaseWebViewActivity
import com.me.ml.webview.annotation.CacheMode
import com.me.ml.webview.annotation.CacheModeState
import com.me.ml.webview.bean.WebViewEvent
import com.me.ml.webview.callback.HandleUrlParamsCallback
import com.me.ml.webview.fragment.WebViewFragment
import com.me.ml.webview.implement.DefaultH5IntentConfigImpl
import com.me.ml.webview.interfaces.H5IntentConfig
import com.me.ml.webview.interfaces.LoadingViewConfig
import com.me.ml.webview.sealed.LoadingWebViewState
import com.peakmain.webview.view.PkWebView

class H5Utils(decoratorConfig: H5IntentConfig = DefaultH5IntentConfigImpl()) :
    AbstractH5IntentConfigDecorator(decoratorConfig) {
    fun isShowToolBar(showTitle: Boolean): H5Utils {
        params.isShowToolBar = showTitle
        return this
    }

    fun updateStatusBar(updateStatusBar: ((String, BaseWebViewActivity?) -> Unit)? = null): H5Utils {
        params.updateStatusBar = updateStatusBar
        return this
    }

    fun updateToolBar(updateToolBarBar: ((String, BaseWebViewActivity?) -> Unit)? = null): H5Utils {
        params.updateToolBarBar = updateToolBarBar
        return this
    }

    fun setLoadingView(loadingViewConfig: LoadingViewConfig): H5Utils {
        params.mLoadingViewConfig = loadingViewConfig
        params.mLoadingWebViewState = LoadingWebViewState.CustomLoadingStyle
        return this
    }

    fun setLoadingWebViewState(loadingWebViewState: LoadingWebViewState): H5Utils {
        params.mLoadingWebViewState = loadingWebViewState
        return this
    }

    fun setWebViewCacheMode(@CacheModeState cacheMode: Int = CacheMode.LOAD_NO_CACHE): H5Utils {
        params.mCacheMode = cacheMode
        return this
    }

    fun setHandleUrlParamsCallback(
        handleUrlParamsCallback:
        HandleUrlParamsCallback<out WebViewEvent>,
    ): H5Utils {
        params.mHandleUrlParamsCallback = handleUrlParamsCallback
        return this
    }


    fun setHeadContentView(view: View?, block: ((View) -> Unit)? = null): H5Utils {
        params.mHeadContentView = view
        params.mHeadContentViewId = 0
        params.mHeadViewBlock = block
        return this
    }

    fun setHeadContentView(@LayoutRes viewIdRes: Int, block: ((View) -> Unit)? = null): H5Utils {
        params.mHeadContentView = null
        params.mHeadContentViewId = viewIdRes
        params.mHeadViewBlock = block
        return this
    }

    /**
     * 预置离线包
     */
    fun commonWebResourceResponse(
        fileName: String, mimeType: String,
        isCommonResource: ((String) -> Boolean)? = null,
    ): H5Utils {
        params.mCommonWeResourceResponsePair = Triple(fileName, mimeType, isCommonResource)
        return this
    }

    fun executeJs(
        methodName: String,
        data: String,
        block: ((PkWebView?, WebViewFragment?) -> Unit)? = null
    ): H5Utils {
        params.mExecuteJsPair = Triple(methodName, data, block)
        return this
    }
}