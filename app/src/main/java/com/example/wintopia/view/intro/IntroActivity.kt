package com.example.wintopia.view.intro

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.core.content.ContextCompat
import com.example.wintopia.R
import com.example.wintopia.view.login.LoginActivity

class IntroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        // SplashActivity에서 IntroActivity로 3초 뒤에 넘어가자
        // 3초 지연 --> SubThread --> *Handler --> MainThread
        Handler().postDelayed({
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)

            finish() // 처음실행 한 번만 필요한거라 finish
        }, 5000)

        window.statusBarColor = ContextCompat.getColor(this, R.color.intorStatus)

    }
}