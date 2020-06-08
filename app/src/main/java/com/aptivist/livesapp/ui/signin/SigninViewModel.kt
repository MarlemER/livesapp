package com.aptivist.livesapp.ui.signin

import androidx.lifecycle.LiveData
import com.aptivist.livesapp.di.interfaces.IFirebaseInstance
import com.aptivist.livesapp.model.UserData
import com.aptivist.livesapp.repository.FirebaseRepository
import com.aptivist.livesapp.viewmodel.BaseViewModel
import com.google.firebase.auth.AuthCredential


class SigninViewModel : BaseViewModel() {
    var fireBaseCredencial: IFirebaseInstance? = null
    private var authRepository = FirebaseRepository(fireBaseCredencial)

    var authenticatedUserLiveData: LiveData<UserData>? = null
    var createdUserLiveData: LiveData<UserData>? = null

    fun signInWithGoogle(googleAuthCredential: AuthCredential?) {
        authenticatedUserLiveData = authRepository?.firebaseSignIn(googleAuthCredential)
    }

    fun createUser(authenticatedUser: UserData?) {
        createdUserLiveData = authRepository?.createUserIfNotExists(authenticatedUser!!)
    }

}