package com.marlem.livesapp.viewmodel

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.ViewModel
import com.marlem.livesapp.ui.SigninActivity
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
                activity.startActivity(Intent(activity, SigninActivity::class.java))
            }
        }
    }
}