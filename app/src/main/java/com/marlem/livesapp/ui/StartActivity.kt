package com.marlem.livesapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.marlem.livesapp.R
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

class StartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
    }
    fun startLoginFacebook(){
        FacebookSdk.sdkInitialize(applicationContext)
        AppEventsLogger.activateApp(this)
    }
}
