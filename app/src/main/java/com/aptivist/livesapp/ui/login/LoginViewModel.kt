package com.aptivist.livesapp.ui.signin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aptivist.livesapp.di.interfaces.IFirebaseInstance
import com.aptivist.livesapp.model.UserData
import com.aptivist.livesapp.repository.FirebaseRepository
import com.aptivist.livesapp.viewmodel.BaseViewModel
import com.google.firebase.auth.AuthCredential


class LoginViewModel : BaseViewModel() {
    var plataformType:MutableLiveData<String> = MutableLiveData()
    var fireBaseCredencial: IFirebaseInstance? = null
    var isResetPass:MutableLiveData<Boolean>?=null

    private var authRepository = FirebaseRepository(fireBaseCredencial)

    var authenticatedUserLiveData: LiveData<UserData>? = null

    fun validationUser(googleAuthCredential: AuthCredential?) {
        authenticatedUserLiveData = authRepository?.firebaseSignIn(googleAuthCredential)
    }

    fun resetPass(email:String):Boolean?
    {
        isResetPass = authRepository?.resertPass(email)
        return  isResetPass?.value
    }

    fun newresetPass(email: String){
        isResetPass = authRepository?.resertPass(email)
    }

}