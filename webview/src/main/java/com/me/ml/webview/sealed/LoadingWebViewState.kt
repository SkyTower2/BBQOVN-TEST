package com.me.ml.webview.sealed

sealed class LoadingWebViewState {
    object NotLoading : LoadingWebViewState()
    object HorizontalProgressBarLoadingStyle : LoadingWebViewState()
    object ProgressBarLoadingStyle : LoadingWebViewState()
    object CustomLoadingStyle : LoadingWebViewState()
}