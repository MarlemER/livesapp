package com.aptivist.livesapp.ui.signin

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.aptivist.livesapp.model.UserData
import com.aptivist.livesapp.repository.FirebaseRepository
import com.google.firebase.auth.AuthCredential


class SigninViewModel : ViewModel() {
    private var authRepository = FirebaseRepository()

    var authenticatedUserLiveData: LiveData<UserData>? = null
    var createdUserLiveData: LiveData<UserData>? = null

    fun signInWithGoogle(googleAuthCredential: AuthCredential?) {
        authenticatedUserLiveData = authRepository!!.firebaseSignIn(googleAuthCredential)
    }

    fun createUser(authenticatedUser: UserData?) {
        createdUserLiveData = authRepository!!.createUserIfNotExists(authenticatedUser!!)
    }

}