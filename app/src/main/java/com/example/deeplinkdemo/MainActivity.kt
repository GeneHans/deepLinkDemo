package com.example.deeplinkdemo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.deeplinkdemo.utils.IntentUtils

class MainActivity : AppCompatActivity() {

    private var textMain: TextView? = null
    private var url =
        "https://www.baidu.com"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textMain = findViewById(R.id.text_main)
        textMain?.setOnClickListener {
            IntentUtils().openInAppBrowser(this, url)
        }
    }
}