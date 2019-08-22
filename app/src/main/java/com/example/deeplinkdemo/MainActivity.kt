package com.example.deeplinkdemo

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.deeplinkdemo.deeplink.TargetActivity

class MainActivity : AppCompatActivity() {

    private var textMain: TextView? = null
    private var url =
        "https://www.baidu.com"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textMain = findViewById(R.id.text_main)
        textMain?.setOnClickListener {
            startActivity(TargetActivity.getIntent(this))
//            IntentUtils().openInAppBrowser(this, url)
        }
    }
}