package com.aptivist.livesapp.ui.signin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.aptivist.livesapp.MainActivity
import com.aptivist.livesapp.R
import com.aptivist.livesapp.databinding.ActivitySigninBinding
import com.aptivist.livesapp.helpers.Constants.Companion.USER
import com.aptivist.livesapp.model.UserData
import com.facebook.*
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.firestore.auth.User
import kotlinx.android.synthetic.main.activity_signin.*


class SigninActivity : AppCompatActivity() {

    lateinit var callbackManager: CallbackManager
    lateinit var signinViewModel: SigninViewModel
    lateinit var dataBinding: ActivitySigninBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_signin)

        //intancia del vm
        signinViewModel = ViewModelProviders.of(this).get(SigninViewModel::class.java)
        dataBinding = DataBindingUtil.setContentView<ActivitySigninBinding>(this,R.layout.activity_signin)
        dataBinding.lifecycleOwner = this
        dataBinding.viewModelSignin = signinViewModel

        //printKeyHash()
        //Instance Firebase
        callbackManager = CallbackManager.Factory.create()

        btnLoginFacebook.setReadPermissions("email", "public_profile")
        btnLoginFacebook.setOnClickListener {
            signIn()
        }

        btnSignin.setOnClickListener {
            signInEmailPass(edtSigninEmail.text.toString(),edtSigninPassword.text.toString())
        }


    }

    private fun signIn(){
        btnLoginFacebook.registerCallback(callbackManager,object: FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult?) {
                Log.d("SUCCESS_FACE","facebook:onSuccess:$result")
                handleFacebookAccessToken(result?.accessToken)
            }

            override fun onCancel() {
                Log.d("CANCEL_FACE", "facebook:onCancel")
            }

            override fun onError(error: FacebookException?) {
                Log.d("ERROR_FACE", "onError", error)
            }
        })


    }

    private fun createNewUser( authenticatedUser : UserData){
        signinViewModel.createUser(authenticatedUser)
        signinViewModel.createdUserLiveData?.observe(this, Observer { user ->
            if(user.isCreated!!){
                //El uusaior se creo con exito
            }
            goToMainActivity(user)
        })
    }



    private fun handleFacebookAccessToken(accessToken: AccessToken?) {
        //Get credentials in base the token
        val credential = FacebookAuthProvider.getCredential(accessToken!!.token)
        signinViewModel.signInWithGoogle(credential)
        signinViewModel.authenticatedUserLiveData?.observe(this, Observer { authenticatedUser ->
            if(!authenticatedUser.isCreated!!){
                createNewUser(authenticatedUser)
            }else{
                goToMainActivity(authenticatedUser)
            }
        })
    }

    private fun signInEmailPass(emailUser:String,passUser:String){
        //validate if user exist
        var userData = UserData("",passUser,emailUser,passUser,false,false,false)
        createNewUser(userData)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager?.onActivityResult(requestCode,resultCode,data)
    }



    private fun goToMainActivity(user: UserData) {
        val intent = Intent(this@SigninActivity, MainActivity::class.java)
        intent.putExtra(USER, user)
        startActivity(intent)
        finish()
    }

}