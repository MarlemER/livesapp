package com.aptivist.livesapp.ui.signin

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aptivist.livesapp.model.SigninModel


class SigninViewModel:ViewModel() {
     var emailUser:MutableLiveData<String> = MutableLiveData()
     var passUser:MutableLiveData<String> = MutableLiveData()
     var userMutableLiveData: MutableLiveData<SigninModel>? = null

    fun OnClicSignin(emailUser:String,passUser:String) {
        /*if (emailUser != null && passUser != null) {
            var userValidation = SigninModel(emailUser, passUser)
        } else {

        }*/
        Log.d("PRUEBA",emailUser.plus(passUser))
        //getLoginFields()
       // isInputValid(emailUser,passUser)
    }

    fun OnClicFacebook(){

    }


   /* fun isInputValid(emailUser: String,passUser: String):Boolean{
        return emailUser.isValid(emailUser, emailUser.length == 0)
    }*/

    /*fun getLoginFields():MutableLiveData<Boolean> {
        return MutableLiveData(true)

    }*/

    fun getLoginFields(): MutableLiveData<SigninModel> {
        if (userMutableLiveData == null) {
            userMutableLiveData = MutableLiveData()
        }
        return userMutableLiveData as MutableLiveData<SigninModel>
        //return getLoginFields()
    }

    /*private fun isInputValid(emailUser:String,passUser:String): Boolean {

        if(emailUser.isEmpty()&&passUser.isEmpty()){

        }

    }*/

}