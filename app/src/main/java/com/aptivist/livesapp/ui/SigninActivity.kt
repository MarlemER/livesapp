package com.aptivist.livesapp.ui

import android.content.Intent
import android.content.pm.PackageManager
import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.annotation.NonNull
import com.aptivist.livesapp.MainActivity
import com.aptivist.livesapp.R
import com.facebook.*
import com.google.firebase.auth.FirebaseAuth
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_signin.*

class SigninActivity : AppCompatActivity() {

    lateinit var callbackManager:CallbackManager
    lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)
        //printKeyHash()
        //Instance Firebase
        firebaseAuth = FirebaseAuth.getInstance()
        callbackManager = CallbackManager.Factory.create()

        btnLoginFacebook.setReadPermissions("email", "public_profile")
        btnLoginFacebook.setOnClickListener {
            signIn()
        }
    }

    private fun signIn(){
        btnLoginFacebook.registerCallback(callbackManager,object:FacebookCallback<LoginResult>{
            override fun onSuccess(result: LoginResult?) {
                Log.d("SUCCESS_FACE","facebook:onSuccess:$result")
                handleFacebookAccessToken(result!!.accessToken)
            }

            override fun onCancel() {
                Log.d("CANCEL_FACE", "facebook:onCancel")
            }

            override fun onError(error: FacebookException?) {
                Log.d("ERROR_FACE", "onError", error)
            }
        })
    }

    private fun handleFacebookAccessToken(accessToken: AccessToken?) {
        //Get credentials in base the token
        val credential = FacebookAuthProvider.getCredential(accessToken!!.token)
        firebaseAuth?.signInWithCredential(credential)
            ?.addOnFailureListener { e->
                Toast.makeText(this,e.message,Toast.LENGTH_SHORT).show()
                Log.e("ERROR_EDMT",e.message)
            }
            ?.addOnSuccessListener {result->
                //Get Email
                val email = result.user?.email
                val name = result.user?.displayName
                Toast.makeText(this,"You logged with facebook:".plus(email).plus("name: ").plus(name),Toast.LENGTH_SHORT).show()
                goMainScreen()
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager?.onActivityResult(requestCode,resultCode,data)
    }

  /*  private fun printKeyHash(){
        try {
            val info = packageManager.getPackageInfo("com.aptivist.livesapp",PackageManager.GET_SIGNATURES)
            for(signature in info.signatures)
            {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.e("KEYHASH",Base64.encodeToString(md.digest(),Base64.DEFAULT))
            }
        }catch (ex:PackageManager.NameNotFoundException){
            ex.printStackTrace()
        }
        catch (ex:NoSuchAlgorithmException){
            ex.printStackTrace()
        }
    }*/

    private fun goMainScreen(){
        val intent = Intent(this,MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        //finish()
    }

    override fun onStart() {
        super.onStart()
       // firebaseAuth.addAuthStateListener(firebaseAuthListener)
    }

    override fun onStop() {
        super.onStop()
       // firebaseAuth.removeAuthStateListener(firebaseAuthListener)
    }

}
