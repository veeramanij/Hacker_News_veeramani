package com.example.Hacker_News.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.Hacker_News.R


class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Handler().postDelayed(Runnable {
            val i = Intent(this@SplashActivity, HomeActivity::class.java)
            startActivity(i)
            finish()
        }, 3000)

    }
}

