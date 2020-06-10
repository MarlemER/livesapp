package com.aptivist.livesapp.ui.signin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.airbnb.lottie.model.layer.NullLayer
import com.aptivist.livesapp.di.interfaces.IFirebaseInstance
import com.aptivist.livesapp.di.interfaces.ISharedPreferences
import com.aptivist.livesapp.model.UserData
import com.aptivist.livesapp.repository.FirebaseRepository
import com.aptivist.livesapp.viewmodel.BaseViewModel
import com.google.firebase.auth.AuthCredential


class SigninViewModel : BaseViewModel() {
    var fireBaseCredencial: IFirebaseInstance? = null
    var sharedPreferences:ISharedPreferences? = null

    private var authRepository = FirebaseRepository(fireBaseCredencial)

    var authenticatedUserLiveData: LiveData<UserData>? = null
    var createdUserLiveData: LiveData<UserData>? = null
    var keyFacebook:MutableLiveData<String>? = null

    fun signInWithGoogle(googleAuthCredential: AuthCredential?) {
        authenticatedUserLiveData = authRepository?.firebaseSignIn(googleAuthCredential)
    }

    fun createUser(authenticatedUser: UserData?) {
        createdUserLiveData = authRepository?.createUserIfNotExists(authenticatedUser!!)
    }

    fun preferencesKeyFacebook(){
        keyFacebook = authRepository?.tokenFacebook()
    }

}