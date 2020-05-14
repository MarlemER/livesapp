package com.aptivist.livesapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.aptivist.livesapp.R
import kotlinx.android.synthetic.main.activity_start.*
import kotlinx.coroutines.*

class StartActivity : AppCompatActivity() {

    //courtines
    private val viewModelJob = Job()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        //val binding = DataBindingUtil.setContentView<ActivityStartBinding>(this,R.layout.activity_start)
        //val vm = ViewModelProvider(this).get(StartVM::class.java)
        //binding.startVM = vm

        btnStartSignin.setOnClickListener { onSignInClick() }
    }

    fun onSignInClick()
    {
        val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
        uiScope.launch {
            withContext(Dispatchers.Main){
                startActivity(Intent(this@StartActivity, SigninActivity::class.java))
            }
        }
    }
    /*fun startLoginFacebook(){
        FacebookSdk.sdkInitialize(applicationContext)
        AppEventsLogger.activateApp(this)
    }*/
}
