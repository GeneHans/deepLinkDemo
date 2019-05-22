package com.example.deeplinkdemo

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.LinearLayout
import com.example.deeplinkdemo.custumView.ScrollWebView
import kotlinx.android.synthetic.main.activity_in_app_browser.*

/**
 * @author Gene Hans
 */
class InAppBrowserActivity : AppCompatActivity(), ScrollWebView.OnPageListener {
    private var webView: ScrollWebView? = null
    private var btnForward: Button? = null
    private var btnBack: Button? = null
    private var bottomBar: LinearLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_in_app_browser)
        webView = findViewById(R.id.webview)
        btnForward = findViewById(R.id.btn_forward)
        btnBack = findViewById(R.id.btn_back)
        bottomBar = findViewById(R.id.ll_bottom_bar)
        var url = intent.getStringExtra("url")
        webView?.webViewClient  = WebViewClient()
        webView?.setOnPageListener(this)
        btnBack?.setOnClickListener {
            if (webView?.canGoBack() == true) {
                webView?.goBack()
            }
        }
        btnForward?.setOnClickListener {
            if (webView?.canGoForward() == true) {
                webView?.canGoForward()
            }
        }
    }

    override fun onPageDown() {
        bottomBar?.visibility = View.GONE
    }

    override fun onPageUp() {
        bottomBar?.visibility = View.VISIBLE
    }

    companion object {
        fun getIntent(url: String) = InAppBrowserActivity().apply {
            intent.putExtra("Url",url)
        }
    }

}
