package com.example.deeplinkdemo.inAPPBrowser

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.webkit.*
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.deeplinkdemo.R
import com.example.deeplinkdemo.custumView.ScrollWebView

/**
 * @author Gene Hans
 */
class InAppBrowserActivity : AppCompatActivity(), ScrollWebView.OnPageListener {
    private var webView: ScrollWebView? = null
    private var imgLeftBlue: ImageView? = null
    private var imgRightBlue: ImageView? = null
    private var imgLeftGray: ImageView? = null
    private var imgRightGray: ImageView? = null
    private var bottomBar: LinearLayout? = null
    private var textProgress: TextView? = null
    private var TAG = "openWeb"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_in_app_browser)
        var url = intent.getStringExtra("Url")
        //获取View
        getView()
        //初始化Client
        setWebViewClient()
        setWebChromeClient()
        //设置web Setting
        setWebSetting()
        //设置所有监听
        setAllListener()
        webView?.loadUrl(url)
    }

    /**
     * 设置所有监听
     */
    private fun setAllListener() {
        webView?.setOnPageListener(this)
        imgLeftBlue?.setOnClickListener {
            if (webView?.canGoBack() == true) {
                webView?.goBack()
            }
        }
        imgRightBlue?.setOnClickListener {
            if (webView?.canGoForward() == true) {
                webView?.canGoForward()
            }
        }
    }

    override fun onStateNotSaved() {
        super.onStateNotSaved()
    }

    /**
     * 设置webSetting
     */
    private fun setWebSetting() {
        val webSettings = webView?.settings
        webSettings?.javaScriptEnabled = true     //支持js
        webSettings?.domStorageEnabled = true
        webSettings?.useWideViewPort = true  //将图片调整到适合webview的大小
        webSettings?.loadWithOverviewMode = true // 缩放至屏幕的大小
        webSettings?.allowFileAccess = true  //设置可以访问文件
        webSettings?.setNeedInitialFocus(true) //当webview调用requestFocus时为webview设置节点
        webSettings?.javaScriptCanOpenWindowsAutomatically = true //支持通过JS打开新窗口
        webSettings?.loadsImagesAutomatically = true  //支持自动加载图片
        webSettings?.defaultTextEncodingName = "utf-8"//设置编码格式
    }

    /**
     * 设置WebChromeClient
     */
    private fun setWebChromeClient() {
        webView?.webChromeClient = object : WebChromeClient() {
            override fun onReceivedTitle(view: WebView?, title: String?) {
                super.onReceivedTitle(view, title)
            }

            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                textProgress?.text = newProgress.toString()
            }
        }
    }

    /**
     * 设置WebView Client
     */
    private fun setWebViewClient() {
        webView?.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)

            }

            override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                super.onReceivedError(view, request, error)
            }
        }
    }

    /**
     * 获取View
     */
    private fun getView() {
        webView = findViewById(R.id.webview)
        imgRightBlue = findViewById(R.id.img_forward_blue)
        imgLeftBlue = findViewById(R.id.img_back_blue)
        imgLeftGray = findViewById(R.id.img_back_gray)
        imgRightGray = findViewById(R.id.img_forward_gray)
        bottomBar = findViewById(R.id.ll_bottom_bar)
        textProgress = findViewById(R.id.text_progress)
    }

    /**
     * 手指上划
     */
    override fun onPageDown() {
        bottomBar?.visibility = View.VISIBLE
    }

    /**
     * 手指下滑
     */
    override fun onPageUp() {
        bottomBar?.visibility = View.GONE
    }

    /**
     * 返回按键监听设置
     */
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        //如果可以回退，则实现网页回退
        if (webView?.canGoBack() == true) {
            webView?.goBack()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    companion object {
        fun getIntent(context: Context, url: String) = Intent(context, InAppBrowserActivity::class.java).apply {
            putExtra("Url", url)
        }
    }
}
