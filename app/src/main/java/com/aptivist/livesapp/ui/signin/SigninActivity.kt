package com.aptivist.livesapp.ui.signin

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.Observable
import androidx.databinding.Observable.OnPropertyChangedCallback
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.aptivist.livesapp.MainActivity
import com.aptivist.livesapp.databinding.ActivitySigninBinding
import com.aptivist.livesapp.model.SigninModel
import com.facebook.*
import com.facebook.login.LoginResult
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_signin.*
import kotlinx.android.synthetic.main.activity_start.*
import com.aptivist.livesapp.R
import java.util.*
import android.text.TextUtils
import androidx.lifecycle.observe


class SigninActivity : AppCompatActivity() {

    lateinit var callbackManager: CallbackManager
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var authStateListener: FirebaseAuth.AuthStateListener
    lateinit var accessTokenTraker: AccessTokenTracker
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

        validationText()


        //printKeyHash()
        //Instance Firebase
        firebaseAuth = FirebaseAuth.getInstance()
        callbackManager = CallbackManager.Factory.create()

       /* btnSignin.setOnClickListener {
            validationText()
        }*/


        btnLoginFacebook.setReadPermissions("email", "public_profile")
        btnLoginFacebook.setOnClickListener {
            signIn()
        }

    }



fun newValidation(){
        signinViewModel.getLoginFields().observe(this,object:Observer<SigninModel>{
            override fun onChanged(signInModel: SigninModel?) {
                Toast.makeText(this@SigninActivity,
                    "Email " + signInModel?.user + ", Password " + signInModel?.password,
                    Toast.LENGTH_SHORT).show();
            }
        })
}


    fun validationText(){
        signinViewModel.OnClicSignin(edtSigninEmail.text.toString(),edtSigninPassword.text.toString())
        signinViewModel.emailUser.observe(this, object : Observer<String>{

            override fun onChanged(t: String?) {
                Toast.makeText(this@SigninActivity,"Error",Toast.LENGTH_SHORT).show()
                /*if (TextUtils.isEmpty(Objects.requireNonNull(t))) {
                    dataBinding.edtSigninEmail.setError("Enter an E-Mail Address");
                    dataBinding.edtSigninEmail.requestFocus();
                }*/
            }
        })
    }




    fun validationText2(){
        signinViewModel.OnClicSignin(edtSigninEmail.text.toString(),edtSigninPassword.text.toString())
                signinViewModel.emailUser.observe(this, Observer<String>{
                    if(it==""){
                    dataBinding.edtSigninEmail.setError("Enter an E-Mail Address")
                    Log.d("WWWW","null")}
                })
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

        authStateListener = object: FirebaseAuth.AuthStateListener{
            override fun onAuthStateChanged(firebaseAuth: FirebaseAuth) {
                val user:FirebaseUser? = firebaseAuth.getCurrentUser()
                if(user!=null){
                    validateUpdateUI(user)
                }else{
                    validateUpdateUI(null)
                }
            }
        }
        accessTokenTraker = object:AccessTokenTracker(){
            override fun onCurrentAccessTokenChanged(
                oldAccessToken: AccessToken?,
                currentAccessToken: AccessToken?
            ) {
                if(currentAccessToken == null){
                    firebaseAuth.signOut()
                }
            }

        }
    }

    private fun handleFacebookAccessToken(accessToken: AccessToken?) {
        //Get credentials in base the token
        val credential = FacebookAuthProvider.getCredential(accessToken!!.token)
        firebaseAuth?.signInWithCredential(credential).addOnCompleteListener(this, object:OnCompleteListener<AuthResult>{
            override fun onComplete(task: Task<AuthResult>) {
                if(task.isSuccessful){
                    Log.d("SUCCESS_FACE","createUserWithEmail:success")
                    val user: FirebaseUser? = firebaseAuth.getCurrentUser()
                    validateUpdateUI(user)
                }else{
                    Log.w("ERROR_FACE","createUserWithEmail:Failure",task.getException())
                    Toast.makeText(this@SigninActivity,"Authentification failed",Toast.LENGTH_SHORT).show()
                    validateUpdateUI(null)
                }
            }

        })
        /*  ?.addOnFailureListener { e->
              Toast.makeText(this,e.message,Toast.LENGTH_SHORT).show()
              Log.e("ERROR_EDMT",e.message)
          }
          ?.addOnSuccessListener {result->
              //Get Email
              val email = result.user?.email
              val name = result.user?.displayName
              Toast.makeText(this,"You logged with facebook:".plus(email).plus("name: ").plus(name),Toast.LENGTH_SHORT).show()
              goMainScreen()
          }*/
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

    fun goMainScreen(name:String?, email:String?, photo:String?){
        val intent = Intent(this,MainActivity::class.java)
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra("nameFacebook_Profile", name)
        intent.putExtra("emailFacebook_Profile",email)
        intent.putExtra("photoUrlFacebook_Profile",photo + "?type=large")
        startActivity(intent)
        //finish()
    }

    override fun onStart() {
        super.onStart()
        //firebaseAuth.addAuthStateListener(authStateListener)
        val currentUser: FirebaseUser? = firebaseAuth.getCurrentUser()
       // validateUpdateUI(currentUser)
    }

    override fun onStop() {
        super.onStop()
        if(authStateListener!=null){
            firebaseAuth.removeAuthStateListener(authStateListener)
        }
    }

    //validationUser loggueo
    private fun validateUpdateUI(user:FirebaseUser?){
        txtStartName.setText("")
        if(user==null) return

            //Get Email
            val email = user.email
            val name = user.displayName
            val photo = user.photoUrl
            Toast.makeText(this,"You logged with facebook: ".plus(email).plus(" name: ").plus(name),Toast.LENGTH_LONG).show()
            goMainScreen(name,email,photo.toString())
            //txtStartName.setText(currentUser.getDisplayName())
            //if(currentUser.photoUrl!=null){
               // var photoUrl:String = currentUser.photoUrl.toString()
                //photoUrl = photoUrl + "?type=large"
                //Picasso.get().load(photoUrl).into(imgUserFacebook)
    }

    fun createAccount()
    {
        firebaseAuth.createUserWithEmailAndPassword(edtSigninEmail.text.toString(),edtSigninPassword.text.toString())
            .addOnCompleteListener (this,object:OnCompleteListener<AuthResult>
            {
                override fun onComplete(task: Task<AuthResult>) {
                    if(task.isSuccessful())
                    {
                        //Sign in success, update UI with the signed-in user's information
                        Log.d("RegisterAccount","createUserWithEmail:success")
                        val user:FirebaseUser?
                        user = firebaseAuth.getCurrentUser()
                        validateUpdateUI(user)
                    }
                    else{
                        //if sign in fails, display a message to the user
                        Log.w("ACCOUNT","createUserWithEmail:failure",task.getException())
                        Toast.makeText(this@SigninActivity,"Authentification failed",Toast.LENGTH_SHORT).show()
                        validateUpdateUI(null)
                    }
                }
            })
    }

    fun getCurrenUser(){
        var user:FirebaseUser?
        user = FirebaseAuth.getInstance().getCurrentUser()
        if(user!=null){
            //name, email address and profile photo Url
            var name:String? = user.getDisplayName()
            var email:String? = user.getEmail()
            var photoUrl:Uri? = user.getPhotoUrl()
            //check if user's email is verified
            var emailVerified:Boolean = user.isEmailVerified()
            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            var uid:String? = user.getUid()
        }
    }
}

