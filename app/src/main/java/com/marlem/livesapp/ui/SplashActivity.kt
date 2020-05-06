package com.marlem.livesapp.ui

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import com.marlem.livesapp.MainActivity
import com.marlem.livesapp.R
import kotlinx.android.synthetic.main.activity_splash.*
import kotlinx.android.synthetic.main.app_bar_main.*

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        //supportActionBar?.hide()
        animationLoading()
        goMain()
    }

    private fun goMain() {
        Handler().postDelayed({
            startActivity(Intent(this, StartActivity::class.java))
            finish()
        }, 3000)
    }

    private fun animationLoading(){
        lottieAnimationViewLoading.setAnimation(R.raw.loading)
        lottieAnimationViewName.setAnimation(R.raw.titleapp)
        //txtTitleLive.startAnimation(AnimationUtils.loadAnimation(this,R.anim.animation_end))
        //txtTitleSafe.startAnimation(AnimationUtils.loadAnimation(this,R.anim.animation_end))
        //imgLogo.startAnimation(AnimationUtils.loadAnimation(this,R.anim.animation_start))
    }

   /* private fun animation(){
        val loadingImage = findViewById<ImageView>(R.id.imgLogo).apply {
            setBackgroundResource(R.drawable.animations)
            rocketAnimation = background as AnimationDrawable
        }
        loadingImage.setOnClickListener({ rocketAnimation.start() })
    }*/
}
