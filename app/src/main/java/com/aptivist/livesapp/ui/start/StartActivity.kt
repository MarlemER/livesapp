package com.aptivist.livesapp.ui.start

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.aptivist.livesapp.R
import com.aptivist.livesapp.helpers.Constants
import com.aptivist.livesapp.model.UserData
import com.aptivist.livesapp.ui.login.LoginActivity
import com.aptivist.livesapp.ui.signin.SigninActivity
import com.aptivist.livesapp.ui.splash.SplashViewModel
import kotlinx.android.synthetic.main.activity_start.*

class StartActivity : AppCompatActivity() {

    //private var startViewModel: StartViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //startViewModel = ViewModelProviders.of(this).get(StartViewModel::class.java)
        setContentView(R.layout.activity_start)

       //checkIfUserIsAuthenticated()
        btnStart.setOnClickListener {  goToLoginActivity() }
       // btnSignin.setOnClickListener { goToSignInActivity() }
    }

/*
    private fun checkIfUserIsAuthenticated() {
        startViewModel?.checkIfUserIsAuthenticated()
        startViewModel?.isUserAuthenticatedLiveData?.observe(this, Observer { user ->
            goToLoginActivity()
            finish()
        })
    }*/

    private fun goToLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
    private fun goToSignInActivity() {
        val intent = Intent(this, SigninActivity::class.java)
        startActivity(intent)
    }
}
