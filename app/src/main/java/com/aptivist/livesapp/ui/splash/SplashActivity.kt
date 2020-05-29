package com.aptivist.livesapp.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.aptivist.livesapp.MainActivity
import com.aptivist.livesapp.R
import com.aptivist.livesapp.helpers.Constants.Companion.USER
import com.aptivist.livesapp.model.UserData
import com.aptivist.livesapp.ui.signin.SigninActivity
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {

    var splashViewModel: SplashViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initSplashViewModel()
        setContentView(R.layout.activity_splash)
        animationLoading()
        goMain()
    }

    private fun initSplashViewModel() {
        splashViewModel = ViewModelProviders.of(this).get(SplashViewModel::class.java)
    }

    private fun goMain() {
        Handler().postDelayed({
            checkIfUserIsAuthenticated()
        }, 3000)
    }

    private fun checkIfUserIsAuthenticated() {
        splashViewModel!!.checkIfUserIsAuthenticated()
        splashViewModel!!.isUserAuthenticatedLiveData?.observe(this, Observer { user ->
            if (user.isAuthenticated!!) {
                goToMainActivity(user)
                finish()
            } else {
               // getUserFromDatabase(user.uid!!)
                goToAuthInActivity()
            }
        })
    }

    private fun getUserFromDatabase(uid: String) {
        splashViewModel!!.setUid(uid)
        splashViewModel!!.userLiveData?.observe(this, Observer { user ->
            if (user != null) {
                goToMainActivity(user)
            }
        })
    }

    private fun goToAuthInActivity() {
        val intent = Intent(this@SplashActivity, SigninActivity::class.java)
        startActivity(intent)
    }

    private fun goToMainActivity(user: UserData) {
        val intent = Intent(this@SplashActivity, MainActivity::class.java)
        intent.putExtra(USER, user)
        startActivity(intent)
    }

    private fun animationLoading() {
        lottieAnimationViewLoading.setAnimation(R.raw.loading)
        lottieAnimationViewName.setAnimation(R.raw.titleapp)
    }
}
