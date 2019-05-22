package com.example.deeplinkdemo.custumView

import android.content.Context
import android.util.AttributeSet
import android.webkit.WebView

class ScrollWebView : WebView {
    private var onPageListener: OnPageListener? = null

    @JvmOverloads
    constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
    ) : super(context, attrs, defStyleAttr)

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)
        if (t + 5 < oldt) {
            onPageListener?.onPageDown()
        } else if (oldt + 5 < t) {
            onPageListener?.onPageUp()
        }
    }

    fun setOnPageListener(listener: OnPageListener) {
        this.onPageListener = listener
    }

    interface OnPageListener {
        fun onPageDown()       //页面向下滑动
        fun onPageUp()         // 页面向上滑动
    }
}