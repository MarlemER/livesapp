package com.aptivist.livesapp.ui.splash

import androidx.lifecycle.LiveData
import com.aptivist.livesapp.di.interfaces.IFirebaseInstance
import com.aptivist.livesapp.model.UserData
import com.aptivist.livesapp.viewmodel.BaseViewModel
import com.google.firebase.auth.AuthCredential
import org.koin.java.KoinJavaComponent.inject


class SplashViewModel : BaseViewModel() {

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

    fun validationUser(googleAuthCredential: AuthCredential?) {
        isUserAuthenticatedLiveData = splashRepository?.firebaseSignIn(googleAuthCredential)
    }
}