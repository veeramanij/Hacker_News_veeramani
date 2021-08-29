package com.example.Hacker_News.activity

import android.os.Bundle
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import com.example.Hacker_News.R


class WebviewActivity : AppCompatActivity() {

    lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview)

        webView= findViewById(R.id.webview)

        webView.loadUrl(intent.getStringExtra("getData").toString())
    }
}

