package com.aptivist.livesapp.ui.signin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.aptivist.livesapp.ui.main.MainActivity
import com.aptivist.livesapp.R
import com.aptivist.livesapp.databinding.ActivitySigninBinding
import com.aptivist.livesapp.di.implementation.SharePreferencesImpl
import com.aptivist.livesapp.di.interfaces.IMessagesDialogs
import com.aptivist.livesapp.di.interfaces.ISessionSignin
import com.aptivist.livesapp.helpers.Constants.Companion.SHAREPREF_EMAILUSER_FIREBASE
import com.aptivist.livesapp.helpers.Constants.Companion.SHAREPREF_PASSUSER_FIREBASE
import com.aptivist.livesapp.helpers.Constants.Companion.SHAREPREF_TOKEN_FACEBOOK
import com.aptivist.livesapp.helpers.Constants.Companion.USER
import com.aptivist.livesapp.model.UserData
import com.facebook.*
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import kotlinx.android.synthetic.main.activity_signin.*
import org.koin.android.ext.android.inject


class SigninActivity : AppCompatActivity() {

    lateinit var callbackManager: CallbackManager
    lateinit var signinViewModel: SigninViewModel
    lateinit var dataBinding: ActivitySigninBinding
    private lateinit var pHelper:SharePreferencesImpl

    private val validationUser: ISessionSignin by inject()
    private val messageUser:IMessagesDialogs by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_signin)

        //intancia del vm
        signinViewModel = ViewModelProviders.of(this).get(SigninViewModel::class.java)
        dataBinding =
            DataBindingUtil.setContentView<ActivitySigninBinding>(this, R.layout.activity_signin)
        dataBinding.lifecycleOwner = this
        dataBinding.viewModelSignin = signinViewModel

        pHelper = SharePreferencesImpl.newInstance(applicationContext)!!

        //printKeyHash()
        //Instance Firebase
        callbackManager = CallbackManager.Factory.create()

        btnLoginFacebook.setReadPermissions("email", "public_profile")
        btnLoginFacebook.setOnClickListener {
            signIn()
        }

        btnSignin.setOnClickListener {
            signInEmailPass(edtSigninEmail.text.toString(), edtSigninPassword.text.toString())
        }
    }

    private fun signIn() {
        btnLoginFacebook.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult?) {
                //Log.d("SUCCESS_FACE", "facebook:onSuccess:$result")
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

    private fun createNewUser(authenticatedUser: UserData) {
        signinViewModel.createUser(authenticatedUser)
        signinViewModel.createdUserLiveData?.observe(this, Observer { user ->
            if (user.isCreated!!) {
                messageUser.showToast(this,resources.getString(R.string.user_register_successful))
                goToMainActivity(user)
            } else {
                messageUser.showMessageOneOption(resources.getString(R.string.error),user.messageResult?:resources.getString(R.string.try_again),resources.getString(R.string.ok),R.drawable.ic_error,this)
            }
        })
    }


    private fun handleFacebookAccessToken(accessToken: AccessToken?) {
        //Get credentials in base the token
        val credential = FacebookAuthProvider.getCredential(accessToken!!.token)

        //if(pHelperToken == null) {
        pHelper.setDataFirebase(SHAREPREF_TOKEN_FACEBOOK, accessToken.token.toString())
       // }
        val pHelperToken = pHelper.getDataFirebase(SHAREPREF_TOKEN_FACEBOOK)

        signinViewModel.signInWithGoogle(credential)
        signinViewModel.authenticatedUserLiveData?.observe(this, Observer { authenticatedUser ->
            if (!authenticatedUser.isCreated!!) {
                createNewUser(authenticatedUser)
                Toast.makeText(this, resources.getString(R.string.user_register_successful), Toast.LENGTH_SHORT).show()
            } else {
                goToMainActivity(authenticatedUser)
            }
        })
    }

    private fun signInEmailPass(emailUser: String, passUser: String) {
        //validate if user exist
        if (validationUser.getEmailUser(emailUser) && validationUser.getPassUser(passUser))
        {
            var userData = UserData("", resources.getString(R.string.livesapp_user), emailUser, passUser, false, false, false, "","")
            createNewUser(userData)

            pHelper.setDataFirebase(SHAREPREF_EMAILUSER_FIREBASE,emailUser)
            pHelper.setDataFirebase(SHAREPREF_PASSUSER_FIREBASE,passUser)
        }
        else
        {
            if (!Patterns.EMAIL_ADDRESS.matcher(edtSigninEmail.text.toString()).matches()) {
                edtSigninEmail.error = resources.getString(R.string.enter_email_valid)
            }
            if (edtSigninPassword.text.toString().isEmpty() || edtSigninPassword.text.toString().length < 7) {
                edtSigninPassword.error = resources.getString(R.string.enter_pass_valid)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager?.onActivityResult(requestCode, resultCode, data)
    }


    private fun goToMainActivity(user: UserData) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra(USER, user)
        startActivity(intent)
        finish()
    }

}