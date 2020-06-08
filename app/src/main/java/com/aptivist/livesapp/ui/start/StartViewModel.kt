package com.aptivist.livesapp.ui.start

import androidx.lifecycle.LiveData
import com.aptivist.livesapp.di.interfaces.IFirebaseInstance
import com.aptivist.livesapp.model.UserData
import com.aptivist.livesapp.ui.splash.SplashRepository
import com.aptivist.livesapp.viewmodel.BaseViewModel
import org.koin.java.KoinJavaComponent.inject


class StartViewModel : BaseViewModel() {

    var firebaseAccess:IFirebaseInstance? = null
    var splashRepository = SplashRepository(firebaseAccess)
    var isUserAuthenticatedLiveData: LiveData<UserData>? = null
    var userLiveData: LiveData<UserData?>? = null

    fun checkIfUserIsAuthenticated() {
        isUserAuthenticatedLiveData = splashRepository?.checkIfUserIsAuthenticatedInFirebase()
    }

    fun setUid(uid: String?) {
        userLiveData = splashRepository?.addUserToLiveData(uid)
    }
}