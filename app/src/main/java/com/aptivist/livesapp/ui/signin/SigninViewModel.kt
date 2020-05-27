package com.aptivist.livesapp.ui.signin

import android.service.autofill.UserData
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import androidx.databinding.Observable
import androidx.databinding.ObservableChar
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aptivist.livesapp.helpers.EnumUser
import com.aptivist.livesapp.viewmodel.BaseViewModel
import java.util.*


class SigninViewModel:BaseViewModel() {
     var emailUser:MutableLiveData<String> = MutableLiveData()
     var passUser:MutableLiveData<String> = MutableLiveData()

    fun OnClicSignin(emailUser:String,passUser:String) {
        this.emailUser.value = emailUser
        this.passUser.value = passUser
        //Log.d("PRUEBA",emailUser.plus(passUser))
    }

    fun OnClicFacebook(){

    }

}