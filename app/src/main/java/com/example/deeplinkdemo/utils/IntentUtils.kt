package com.example.deeplinkdemo.utils

import android.content.Context
import com.example.deeplinkdemo.inAPPBrowser.InAppBrowserActivity

class IntentUtils {
    /**
     * 从内置浏览器打开网址
     */
    fun openInAppBrowser(context: Context, url: String) {
        var intent = InAppBrowserActivity.getIntent(context, url)
        context.startActivity(intent)
    }
}