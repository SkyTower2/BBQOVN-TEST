package com.me.ml.webview.helper

import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.me.ml.webview.bean.ActivityResultBean

class PkStartActivityResultContracts(private val requestCode: Int) :
    ActivityResultContract<Intent, ActivityResultBean>() {
    override fun createIntent(context: Context, input: Intent): Intent {
        return input
    }

    override fun parseResult(resultCode: Int, intent: Intent?): ActivityResultBean {
        return ActivityResultBean(resultCode, requestCode, intent)
    }
}