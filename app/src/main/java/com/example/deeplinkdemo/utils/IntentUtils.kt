package com.example.deeplinkdemo.utils

import android.content.ClipboardManager
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

    /**
     * 复制link到剪切板
     */
    fun copyData(context: Context, dataString: String) {
        var clip: ClipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
//        clip.getText()   //粘贴
        clip.text = dataString     //复制内容到剪切板
    }

    companion object {
        fun instance() = IntentUtils()
    }
}