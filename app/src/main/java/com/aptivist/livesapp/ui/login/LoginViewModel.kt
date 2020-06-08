package com.aptivist.livesapp.ui.signin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aptivist.livesapp.di.interfaces.IFirebaseInstance
import com.aptivist.livesapp.model.UserData
import com.aptivist.livesapp.repository.FirebaseRepository
import com.aptivist.livesapp.viewmodel.BaseViewModel
import com.google.firebase.auth.AuthCredential
import com.google.firebase.database.DatabaseReference


class LoginViewModel : BaseViewModel() {
    var emailLogin:MutableLiveData<String> = MutableLiveData()
    var passLogin:MutableLiveData<String> = MutableLiveData()
    var plataformType:MutableLiveData<String> = MutableLiveData()

    var fireBaseCredencial: IFirebaseInstance? = null
    private var authRepository = FirebaseRepository(fireBaseCredencial)

    var authenticatedUserLiveData: LiveData<UserData>? = null
    var createdUserLiveData: LiveData<UserData>? = null

    fun validationUser() {

     val user =  fireBaseCredencial?.getFirebaseAuth()?.currentUser

        this.emailLogin.value = authenticatedUserLiveData?.value?.emailUser
        this.passLogin.value = authenticatedUserLiveData?.value?.passwordUser
    }

    fun signInWithGoogle(googleAuthCredential: AuthCredential?) {
        authenticatedUserLiveData = authRepository?.firebaseSignIn(googleAuthCredential)
    }



}