package com.aptivist.livesapp.ui.splash

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.aptivist.livesapp.helpers.Constants.Companion.USERS
import com.aptivist.livesapp.model.UserData
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class SplashRepository {

    private val firebaseAuth = FirebaseAuth.getInstance()
   // private lateinit  var  user : UserData
    //private lateinit var user: UserData

     val user = UserData("",
        "","","",
        isAuthenticated = false,
        isNew = false,
        isCreated = true
    )

    private val rootRef = FirebaseFirestore.getInstance()
    private val usersRef = rootRef.collection(USERS)

    fun checkIfUserIsAuthenticatedInFirebase(): MutableLiveData<UserData>? {
        val isUserAuthenticateInFirebaseMutableLiveData: MutableLiveData<UserData> =
            MutableLiveData<UserData>()

        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser == null) {
            user.isAuthenticated = false
            isUserAuthenticateInFirebaseMutableLiveData.setValue(user)
        } else {
            user.uid = firebaseUser.uid
            user.isAuthenticated = true
            user.emailUser = firebaseUser.email
            user.userUser = firebaseUser.displayName
            isUserAuthenticateInFirebaseMutableLiveData.setValue(user)
        }
        return isUserAuthenticateInFirebaseMutableLiveData
    }



    fun addUserToLiveData(uid: String?): MutableLiveData<UserData?>? {
        val userMutableLiveData: MutableLiveData<UserData?> = MutableLiveData<UserData?>()
        usersRef.document(uid!!).get()
            .addOnCompleteListener { userTask: Task<DocumentSnapshot?> ->
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
}