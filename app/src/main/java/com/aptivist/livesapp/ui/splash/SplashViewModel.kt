package com.aptivist.livesapp.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.aptivist.livesapp.model.UserData


class SplashViewModel : ViewModel() {

    var splashRepository = SplashRepository()
    var isUserAuthenticatedLiveData: LiveData<UserData>? = null
    var userLiveData: LiveData<UserData?>? = null

    fun checkIfUserIsAuthenticated() {
        isUserAuthenticatedLiveData = splashRepository!!.checkIfUserIsAuthenticatedInFirebase()
    }

    fun setUid(uid: String?) {
        userLiveData = splashRepository!!.addUserToLiveData(uid)
    }
}