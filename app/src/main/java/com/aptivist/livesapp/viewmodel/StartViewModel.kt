package com.aptivist.livesapp.viewmodel

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.ViewModel
import com.aptivist.livesapp.ui.signin.LoginActivity
import kotlinx.coroutines.*

class StartViewModel : ViewModel() {
    //courtines
    private val viewModelJob = Job()
    val uiscope = CoroutineScope(Dispatchers.Main + viewModelJob)
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
    //functions

    fun onSignInClick(activity: Activity) {
        uiscope.launch {
            withContext(Dispatchers.Main) {
                activity.startActivity(Intent(activity, LoginActivity::class.java))
            }
        }
    }
}