package com.aptivist.livesapp.ui.login

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.aptivist.livesapp.ui.main.MainActivity
import com.aptivist.livesapp.R
import com.aptivist.livesapp.databinding.ActivityLoginBinding
import com.aptivist.livesapp.di.implementation.SharePreferencesImpl
import com.aptivist.livesapp.di.interfaces.IFirebaseInstance
import com.aptivist.livesapp.di.interfaces.IMessagesDialogs
import com.aptivist.livesapp.di.interfaces.ISessionSignin
import com.aptivist.livesapp.helpers.Constants
import com.aptivist.livesapp.helpers.Constants.Companion.SHAREPREF_EMAILUSER_FIREBASE
import com.aptivist.livesapp.helpers.Constants.Companion.SHAREPREF_PASSUSER_FIREBASE
import com.aptivist.livesapp.helpers.Constants.Companion.USER
import com.aptivist.livesapp.model.UserData
import com.aptivist.livesapp.ui.signin.LoginViewModel
import com.aptivist.livesapp.ui.signin.SigninActivity
import com.facebook.*
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.edtSigninEmail
import kotlinx.android.synthetic.main.activity_login.edtSigninPassword
import org.koin.android.ext.android.inject


class LoginActivity : AppCompatActivity() {

    private val validationFirebase: IFirebaseInstance by inject()
    private val validationUser: ISessionSignin by inject()
    private val messagesUser: IMessagesDialogs by inject()
    //private lateinit var auth: FirebaseAuth
    var callbackManager = CallbackManager.Factory.create()
    private var auth = validationFirebase.getFirebaseAuth()
    lateinit var loginViewModel: LoginViewModel
    lateinit var dataBinding: ActivityLoginBinding
    private lateinit var pHelper: SharePreferencesImpl

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_login)

        pHelper = SharePreferencesImpl.newInstance(applicationContext)!!

        //intancia del vm
        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        dataBinding = DataBindingUtil.setContentView<ActivityLoginBinding>(this, R.layout.activity_login)
        dataBinding.lifecycleOwner = this
        dataBinding.viewModelLogin = loginViewModel

        observer()

        btnLogin.setOnClickListener { signInEmailPass(edtSigninEmail.text.toString(),edtSigninPassword.text.toString()) }
        btnLoginFacebookLogin.setOnClickListener { loginFacebook() }
        txtResetPassword.setOnClickListener { resetPass() }
        btnCreateAccount.setOnClickListener { goSignin() }

        getInfoPrefHelper()
    }

    private fun getInfoPrefHelper(){
        edtSigninEmail.setText(pHelper.getDataFirebase(SHAREPREF_EMAILUSER_FIREBASE)?:null)
        edtSigninPassword.setText(pHelper.getDataFirebase(SHAREPREF_PASSUSER_FIREBASE)?:null)
    }

    private fun observer(){
        loginViewModel.isResetPass?.observe(this, Observer {
            if(it)
            {
                messagesUser.showMessageOneOption(resources.getString(R.string.verify_email),resources.getString(R.string.email_send),resources.getString(R.string.ok),R.drawable.ic_email,this)
            }else{
                messagesUser.showMessageOneOption(resources.getString(R.string.error),resources.getString(R.string.invalid_email),resources.getString(R.string.ok),R.drawable.ic_error,this)
            }
        })
    }

    private fun signInEmailPass(emailUser: String, passUser: String) {
        //validate if user exist
        if (validationFields())
        {
           // auth = validationFirebase.getFirebaseAuth()
                auth.signInWithEmailAndPassword(edtSigninEmail.text.toString(),edtSigninPassword.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        messagesUser.showToast(this,resources.getString(R.string.login_successful))
                        //Toast.makeText(this,"Login successful",Toast.LENGTH_LONG).show()
                        var userData = UserData("", resources.getString(R.string.livesapp_user), emailUser, passUser, true, false, true, Constants.PHOTO_USER_DEFAULT,"")
                       /* user?.isCreated = true
                        user?.photoUser = Constants.PHOTO_USER_DEFAULT
                        user?.emailUser = edtSigninEmail.text.toString()
                        user?.passwordUser = edtSigninPassword.text.toString()*/
                        pHelper.setDataFirebase(SHAREPREF_EMAILUSER_FIREBASE,emailUser)
                        pHelper.setDataFirebase(SHAREPREF_PASSUSER_FIREBASE,passUser)
                        var intent = Intent(this,MainActivity::class.java)
                        intent.putExtra(USER, userData)
                        startActivity(intent)
                        finish()
                    } else {
                        // If sign in fails, display a message to the user.
                        messagesUser.showToast(this,resources.getString(R.string.valid_account))
                    }
                }
        }
    }


    private fun validationFields():Boolean{
        var valid:Boolean = false
        if (validationUser.getEmailUser(edtSigninEmail.text.toString()) && validationUser.getPassUser(edtSigninPassword.text.toString()))
        {
            valid = true
        }
        else
        {
            if (!Patterns.EMAIL_ADDRESS.matcher(edtSigninEmail.text.toString()).matches()) {
                edtSigninEmail.error = resources.getString(R.string.enter_email_valid)
            }
            if (edtSigninPassword.text.toString().isEmpty()) {
                edtSigninPassword.error = resources.getString(R.string.enter_pass)
            }
            valid = false
        }
        return valid
    }

    private fun loginFacebook() {
        //valid with facebook
            btnLoginFacebookLogin.setReadPermissions("email", "public_profile")
            btnLoginFacebookLogin.registerCallback(callbackManager, object :
                FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {
                    handleFacebookAccessToken(loginResult.accessToken)
                }

                override fun onCancel() {
                    messagesUser.showToast(this@LoginActivity,resources.getString(R.string.login_successful))
                    //Toast.makeText(this@LoginActivity,"Login successful:onCancel",Toast.LENGTH_LONG).show()
                }

                override fun onError(error: FacebookException) {
                    messagesUser.showToast(this@LoginActivity,resources.getString(R.string.login_successful))
                    //Toast.makeText(this@LoginActivity,"Login successful:onError",Toast.LENGTH_LONG).show()
                }
            })
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Pass the activity result back to the Facebook SDK
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        val credential = FacebookAuthProvider.getCredential(token.token)
        //if(pHelperToken == null) {
        pHelper.setDataFirebase(Constants.SHAREPREF_TOKEN_FACEBOOK, token.token.toString())
        // }
        val pHelperToken = pHelper.getDataFirebase(Constants.SHAREPREF_TOKEN_FACEBOOK)
        //Log.d("ACCES TOKEN",pHelperToken)
        //Log.d("ACCES TOKEN",token.token)
        loginViewModel.validationUser(credential)
        loginViewModel.authenticatedUserLiveData?.observe(this, androidx.lifecycle.Observer { authenticatedUser->
            if(authenticatedUser.isAuthenticated!!)
            {
                goToMainActivity(authenticatedUser)
            }
        })
    }

    private fun goToMainActivity(user: UserData) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra(USER, user)
        startActivity(intent)
        finish()
    }

    private fun resetPass(){
        resetValidations()
        if(validationUser.getEmailUser(edtSigninEmail.text.toString()))
            {
                loginViewModel.resetPass(edtSigninEmail.text.toString())
                loginViewModel.isResetPass?.observe(this, Observer { isReset->
                    if(isReset.equals(true))
                    {
                        messagesUser.showMessageOneOption(resources.getString(R.string.verify_email),resources.getString(R.string.email_send),resources.getString(R.string.ok),R.drawable.ic_email,this)
                    }else{
                        messagesUser.showMessageOneOption(resources.getString(R.string.error),resources.getString(R.string.invalid_email),resources.getString(R.string.ok),R.drawable.ic_error,this)
                    }
                })
            }else{
            messagesUser.showToast(this,resources.getString(R.string.enter_email_continue))
            edtSigninEmail.error = resources.getString(R.string.enter_email_valid)
        }

    }

    private fun resetValidations()
    {
        edtSigninEmail.error = null
        edtSigninPassword.error = null
    }

    private fun goSignin(){
        startActivity(Intent(this,SigninActivity::class.java))
        finish()
    }

}
