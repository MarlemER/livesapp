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
import com.aptivist.livesapp.MainActivity
import com.aptivist.livesapp.R
import com.aptivist.livesapp.databinding.ActivityLoginBinding
import com.aptivist.livesapp.databinding.ActivitySigninBinding
import com.aptivist.livesapp.di.interfaces.ISessionSignin
import com.aptivist.livesapp.helpers.Constants.Companion.USER
import com.aptivist.livesapp.model.UserData
import com.facebook.*
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_signin.*
import kotlinx.android.synthetic.main.activity_signin.edtSigninEmail
import kotlinx.android.synthetic.main.activity_signin.edtSigninPassword
import org.koin.android.ext.android.inject
import kotlin.math.log


class LoginActivity : AppCompatActivity() {

    lateinit var callbackManager: CallbackManager
    lateinit var loginViewModel: LoginViewModel
    lateinit var dataBinding: ActivityLoginBinding

    private val validationUser: ISessionSignin by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_signin)

        //intancia del vm
        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        dataBinding =
            DataBindingUtil.setContentView<ActivityLoginBinding>(this, R.layout.activity_login)
        dataBinding.lifecycleOwner = this
        dataBinding.viewModelLogin = loginViewModel


        loginViewModel.validationUser()


        loginViewModel.emailLogin.value = "correo@gmail.com"
        loginViewModel.passLogin.value = "******"

        //printKeyHash()
        //Instance Firebase
        callbackManager = CallbackManager.Factory.create()

        btnLogin.setOnClickListener { startActivity(Intent(this,MainActivity::class.java)) }
    }

    private fun login() {
        loginViewModel.validationUser()
    }

    private fun createNewUser(authenticatedUser: UserData) {

        loginViewModel.validationUser()
        loginViewModel.createdUserLiveData?.observe(this, Observer { user ->
            if (user.isCreated!!) {
                Toast.makeText(this, "User register successful", Toast.LENGTH_SHORT).show()
                goToMainActivity(user)
            } else {
                Toast.makeText(this, "Error try again", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun goToMainActivity(user: UserData) {
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        intent.putExtra(USER, user)
        startActivity(intent)
        finish()
    }

}