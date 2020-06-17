package com.aptivist.livesapp.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.aptivist.livesapp.ui.main.MainActivity
import com.aptivist.livesapp.R
import com.aptivist.livesapp.di.implementation.SharePreferencesImpl
import com.aptivist.livesapp.helpers.Constants
import com.aptivist.livesapp.helpers.Constants.Companion.USER
import com.aptivist.livesapp.model.UserData
import com.aptivist.livesapp.ui.start.StartActivity
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.GraphRequest
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import kotlinx.android.synthetic.main.activity_splash.*
import org.json.JSONObject
import java.lang.Exception

class SplashActivity : AppCompatActivity() {

    private var splashViewModel: SplashViewModel? = null
    private var callbackManager: CallbackManager? = null
    private lateinit var pHelper: SharePreferencesImpl

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initSplashViewModel()
        setContentView(R.layout.activity_splash)
        pHelper = SharePreferencesImpl.newInstance(applicationContext)!!
        animationLoading()
        goMain()
    }

    override fun onStart() {
        super.onStart()
        //goMain()
    }

    private fun initSplashViewModel() {
        splashViewModel = ViewModelProviders.of(this).get(SplashViewModel::class.java)
    }

    private fun goMain() {
        Handler().postDelayed({
            checkIfUserIsAuthenticated()
        }, 3000)
    }

    private fun handleSignInResultFacebook(jsonObject: JSONObject?) {

    }

    private fun checkIfUserIsAuthenticated() {
        //splashViewModel?.checkIfUserIsAuthenticated()
        val pHelperToken = pHelper.getDataFirebase(Constants.SHAREPREF_TOKEN_FACEBOOK)?:""
        //Log.d("ACCES TOKEN",token.token)
        if(!pHelperToken.isNullOrEmpty())
        {
            val credential = FacebookAuthProvider.getCredential(pHelperToken)
            splashViewModel?.validationUser(credential)
        }
        else{
            splashViewModel?.checkIfUserIsAuthenticated()
        }
        splashViewModel?.isUserAuthenticatedLiveData?.observe(this, Observer { user ->
            if (user.isAuthenticated!!) {
                goToMainActivity(user)
                finish()
            } else {
                // getUserFromDatabase(user.uid!!)
                goToAuthInActivity()
                finish()
            }
        })
    }

    private fun getUserFromDatabase(uid: String) {
        splashViewModel?.setUid(uid)
        splashViewModel?.userLiveData?.observe(this, Observer { user ->
            if (user != null) {
                goToMainActivity(user)
            }
        })
    }

    private fun goToAuthInActivity() {
        val intent = Intent(this@SplashActivity, StartActivity::class.java)
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
