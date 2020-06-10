package com.aptivist.livesapp.ui.splash

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.aptivist.livesapp.di.interfaces.IFirebaseInstance
import com.aptivist.livesapp.helpers.Constants.Companion.PHOTO_USER_DEFAULT
import com.aptivist.livesapp.helpers.Constants.Companion.USERS
import com.aptivist.livesapp.model.UserData
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User

class SplashRepository(private val firebaseAccess:IFirebaseInstance?) {

    private val firebaseAuth = FirebaseAuth.getInstance()
   // private lateinit  var  user : UserData
    //private lateinit var user: UserData

     val user = UserData("",
        "","","",
        isAuthenticated = false,
        isNew = false,
        isCreated = true,
         photoUser = ""
    )

   // private val rootRef = FirebaseFirestore.getInstance()
    private val usersRef = firebaseAccess?.getFirebaseFirestore()?.collection(USERS)

    fun checkIfUserIsAuthenticatedInFirebase(): MutableLiveData<UserData>? {
        val isUserAuthenticateInFirebaseMutableLiveData: MutableLiveData<UserData> =
            MutableLiveData<UserData>()

        var firebaseUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser

       // val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser == null) {
            user.isAuthenticated = false
            isUserAuthenticateInFirebaseMutableLiveData.setValue(user)
        } else {
            user.uid = firebaseUser.uid
            user.isAuthenticated = true
            user.emailUser = firebaseUser.email ?: "livesap@support.com"
            //data with facebook
           val returnData = FirebaseAuth.getInstance().currentUser
            returnData?.let {
                for (profile in it.providerData) {
                    if (profile.providerId == "facebook.com") {
                        user.userUser = firebaseUser.displayName
                        user.photoUser = (firebaseUser.photoUrl.toString() + "?type=large")
                    }
                    //data with Email and Pass
                    else {
                        user.userUser = "livesapp"
                        user.photoUser = PHOTO_USER_DEFAULT
                    }
                }
            }

            isUserAuthenticateInFirebaseMutableLiveData.setValue(user)
        }
        return isUserAuthenticateInFirebaseMutableLiveData
    }

    fun addUserToLiveData(uid: String?): MutableLiveData<UserData?>? {
        val userMutableLiveData: MutableLiveData<UserData?> = MutableLiveData<UserData?>()
        usersRef?.document(uid!!)?.get()
            ?.addOnCompleteListener { userTask: Task<DocumentSnapshot?> ->
                if (userTask.isSuccessful) {
                    val document = userTask.result
                    if (document!!.exists()) {
                        val user: UserData = document.toObject(UserData::class.java)!!
                        userMutableLiveData.setValue(user)
                    }
                } else {
                    Log.d("*****",userTask.exception!!.message)
                }
            }
        return userMutableLiveData
    }

    fun firebaseSignIn(authCredential: AuthCredential?): MutableLiveData<UserData>? {
        val authenticatedUserMutableLiveData: MutableLiveData<UserData> = MutableLiveData<UserData>()

        firebaseAuth.signInWithCredential(authCredential!!)
            ?.addOnCompleteListener { authTask: Task<AuthResult> ->
                if (authTask.isSuccessful) {
                    val isNewUser =
                        authTask.result?.additionalUserInfo?.isNewUser
                    val firebaseUser = firebaseAuth.currentUser
                    if (firebaseUser != null) {
                        val uid = firebaseUser.uid
                        val name = firebaseUser.displayName
                        val email = firebaseUser.email
                        val photo = firebaseUser.photoUrl
                        val user = UserData(uid,
                            name,email,"",
                            isAuthenticated = true,
                            isNew = false,
                            isCreated = true,
                            photoUser = "$photo?type=large"
                        )
                        user.isNew = isNewUser

                        authenticatedUserMutableLiveData.value = user
                    }
                } else {
                    Log.d("*****",authTask.exception?.message)
                }
            }
        return authenticatedUserMutableLiveData
    }

}