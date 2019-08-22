package com.example.deeplinkdemo.inAPPBrowser

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import android.text.TextUtils
import android.view.KeyEvent
import android.view.View
import android.webkit.*
import android.widget.*
import com.example.deeplinkdemo.R
import com.example.deeplinkdemo.custumView.ScrollWebView
import com.example.deeplinkdemo.utils.IntentUtils

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
    private var imgBack: ImageView? = null
    private var imgMenu: ImageView? = null
    private var textTitle: TextView? = null
    private var progressBar: ProgressBar? = null
    private var TAG = "openWeb"
    private var currentUrl = ""
    private var cookieManager = CookieManager.getInstance()
    private var cookie = "name=xxxx"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_in_app_browser)
        var url = intent.getStringExtra("Url")
        currentUrl = url
        //获取View
        getView()
        //初始化Client
        setWebViewClient()
        setWebChromeClient()
        //设置web Setting
        setWebSetting()
        //设置所有监听
        setAllListener()
        setCookie(cookie, currentUrl)
        webView?.loadUrl(url)
    }

    /**
     * 设置所有监听
     */
    private fun setAllListener() {
        //设置toolbar menu
        if (imgMenu != null) {
            setToolBarMenu()
        }
        //设置webView滑动监听
        webView?.setOnPageListener(this)

        imgLeftBlue?.setOnClickListener {
            if (webView?.canGoBack() == true) {
                webView?.goBack()
            }
        }
        imgRightBlue?.setOnClickListener {
            if (webView?.canGoForward() == true) {
                webView?.goForward()
            }
        }
        imgBack?.setOnClickListener {
            finish()
        }
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
                textTitle?.text = title ?: ""
            }

            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                progressBar?.progress = newProgress
                if (newProgress == 100)
                    progressBar?.visibility = View.GONE
                super.onProgressChanged(view, newProgress)
            }
        }
    }

    /**
     * 设置toolbar上的menu具体菜单内容
     * 由于popupmenu存在有显示图标的错误，在此采用了反射的方法进行图片显示的设置
     */
    private fun setToolBarMenu() {
        var popupMenu = PopupMenu(this, imgMenu!!)
        popupMenu.menuInflater.inflate(R.menu.browser_menu, popupMenu.menu)
        if (popupMenu.menu.javaClass.simpleName.equals("MenuBuilder", ignoreCase = true)) {
            try {
                val method =
                    popupMenu.menu.javaClass.getDeclaredMethod(
                        "setOptionalIconsVisible",
                        java.lang.Boolean.TYPE
                    )
                method.isAccessible = true
                method.invoke(popupMenu.menu, true)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        popupMenu?.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.item_refresh -> {
                    webView?.reload()
                    true
                }
                R.id.item_copy_link -> {
                    IntentUtils.instance().copyData(this, currentUrl)
                    Toast.makeText(this@InAppBrowserActivity, "复制链接成功", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.item_open_others -> {
                    var intent = Intent(Intent.ACTION_VIEW, Uri.parse(currentUrl))
                    startActivity(intent)
                    true
                }
                else -> {
                    true
                }
            }
        }
        imgMenu?.setOnClickListener {
            popupMenu.show()
        }
    }

    /**
     * 设置cookie
     */
    private fun setCookie(cookie: String, url: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.removeSessionCookies(null)
            cookieManager.flush()
        } else {
            cookieManager.removeSessionCookie()
            CookieSyncManager.getInstance().sync()
        }
        if (isDomainXXX(url)) {
            var cookieString = cookie + "; domain=" + getDomain(url) + "; path=" + "/"
            cookieManager.setAcceptCookie(true)
            cookieManager.setCookie(url, cookieString)
        }
    }

    /**
     * 根据传入的URL获取一级域名
     *
     * @param url
     * @return
     */
    private fun getDomain(url: String): String {
        var domain = ""
        if (!TextUtils.isEmpty(url) && url.startsWith("http")) {
            try {
                val host = Uri.parse(url).host
                if (!TextUtils.isEmpty(host) && host!!.contains(".")) {
                    domain = host.substring(host.indexOf("."), host.length)
                }
            } catch (ex: Exception) {
            }
        }
        return domain
    }

    /**
     * 根据传入的URL判断是否为xxx域名，默认不是
     *
     * @param url
     * @return
     */
    private fun isDomainXXX(url: String): Boolean {
        return getDomain(url) == ".xxx.com"
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
                if (url == null)
                    return
                updateBottomBar()
                currentUrl = url
                setCookie(cookie, currentUrl)
                //标准网址直接加载
                if (url.startsWith("http://") || url.startsWith("https://")) {
                    super.onPageStarted(view, url, favicon)
                } else {
                    try {
                        var intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        startActivity(intent)
                        //跳转之后返回上衣页面
                        if (webView?.canGoBack() == true)
                            webView?.goBack()
                        else
                            this@InAppBrowserActivity.finish()
                    } catch (e: Exception) {        //防止出现特殊域名导致出错
                        if (webView?.canGoBack() == true)
                            webView?.goBack()
                        else
                            this@InAppBrowserActivity.finish()
                    }
                }
            }

            override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                updateBottomBar()
                super.onReceivedError(view, request, error)
            }
        }
    }

    /**
     * 更新底部bar前进后退状态
     */
    private fun updateBottomBar() {
        if (webView?.canGoBack() == true) {
            imgLeftGray?.visibility = View.GONE
            imgLeftBlue?.visibility = View.VISIBLE
        } else {
            imgLeftGray?.visibility = View.VISIBLE
            imgLeftBlue?.visibility = View.GONE
        }
        if (webView?.canGoForward() == true) {
            imgRightGray?.visibility = View.GONE
            imgRightBlue?.visibility = View.VISIBLE
        } else {
            imgRightGray?.visibility = View.VISIBLE
            imgRightBlue?.visibility = View.GONE
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
        imgBack = findViewById(R.id.img_back)
        textTitle = findViewById(R.id.text_title)
        imgMenu = findViewById(R.id.img_menu)
        progressBar = findViewById(R.id.browser_progressbar)
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
