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
import com.aptivist.livesapp.databinding.ActivityLoginBinding
import com.aptivist.livesapp.di.interfaces.IFirebaseInstance
import com.aptivist.livesapp.di.interfaces.ISessionSignin
import com.aptivist.livesapp.helpers.Constants.Companion.USER
import com.aptivist.livesapp.model.UserData
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
    //private lateinit var auth: FirebaseAuth
    var callbackManager = CallbackManager.Factory.create()
    private var auth = validationFirebase.getFirebaseAuth()
    lateinit var loginViewModel: LoginViewModel
    lateinit var dataBinding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_login)

        //intancia del vm
        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        dataBinding = DataBindingUtil.setContentView<ActivityLoginBinding>(this, R.layout.activity_login)
        dataBinding.lifecycleOwner = this
        dataBinding.viewModelLogin = loginViewModel

        observer()

        btnLogin.setOnClickListener { signInEmailPass(edtSigninEmail.text.toString(),edtSigninPassword.text.toString()) }
        btnLoginFacebookLogin.setOnClickListener { loginFacebook() }
        txtResetPassword.setOnClickListener { resetPass() }
    }

    private fun observer(){
        loginViewModel.isResetPass?.observe(this, Observer {
            if(it)
            {
                Toast.makeText(this@LoginActivity,"Email send, check your Email for change the password",Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(this@LoginActivity,"Invalid email isn't registered, try again!",Toast.LENGTH_LONG).show()
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
                        Toast.makeText(this,"Login successful",Toast.LENGTH_LONG).show()
                        startActivity(Intent(this,
                            MainActivity::class.java))
                        finish()
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(this,"Enter a valid account",Toast.LENGTH_LONG).show()
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
                edtSigninEmail.error = "Enter an E-Mail Address"
            }
            if (edtSigninPassword.text.toString().isEmpty() || edtSigninPassword.text.toString().length < 7) {
                edtSigninPassword.error = "Enter a Password, minimum 7 characters"
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
                    Toast.makeText(this@LoginActivity,"Login successful:onCancel",Toast.LENGTH_LONG).show()
                }

                override fun onError(error: FacebookException) {
                    Toast.makeText(this@LoginActivity,"Login successful:onError",Toast.LENGTH_LONG).show()
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
        Log.d("ACCES TOKEN",token.token)
        loginViewModel.validationUser(credential)
        loginViewModel.authenticatedUserLiveData?.observe(this, androidx.lifecycle.Observer { authenticatedUser->
            if(authenticatedUser.isAuthenticated!!)
            {
                goToMainActivity(authenticatedUser)
            }
        })
    }

    private fun getTokenFacebook()
    {
        val token = AccessToken.getCurrentAccessToken()
        if(token == null){
            Toast.makeText(this,"Invalid",Toast.LENGTH_LONG).show()
        }else{
            Toast.makeText(this,"Successful",Toast.LENGTH_LONG).show()
        }
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

            }else{
            Toast.makeText(this@LoginActivity,"Introduce a valid email for continue",Toast.LENGTH_LONG).show()
            edtSigninEmail.error = "Enter an E-Mail Address"
        }

    }

    private fun resetValidations()
    {
        edtSigninEmail.error = null
        edtSigninPassword.error = null
    }

}
