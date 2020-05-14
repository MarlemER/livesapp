package com.aptivist.livesapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.aptivist.livesapp.R
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        animationLoading()
        goMain()
    }

    private fun goMain() {
        Handler().postDelayed({
            startActivity(Intent(this, StartActivity::class.java))
            finish()
        }, 3000)
    }

    private fun animationLoading() {
        lottieAnimationViewLoading.setAnimation(R.raw.loading)
        lottieAnimationViewName.setAnimation(R.raw.titleapp)
    }
}
