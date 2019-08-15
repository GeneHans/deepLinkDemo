package com.example.deeplinkdemo.deeplink

/**
 *  @author Gene Hans
 */
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.deeplinkdemo.R

class DeepLinkActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deep_link)
        handlerDeepLink()
    }

    private fun handlerDeepLink() {
        var action = intent.action
        var data = intent.data
        if (data?.host != null) {
            when (data.host) {
                getString(R.string.deeplink_host) -> {
                    toTargetPage()
                }
                else -> {
                    finish()
                }
            }
        } else
            finish()
    }

    private fun toTargetPage() {
        var intent = Intent()
        intent.setClass(baseContext, TargetActivity::class.java)
        startActivity(intent)
    }
}