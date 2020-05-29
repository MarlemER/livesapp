package com.aptivist.livesapp.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.aptivist.livesapp.helpers.Constants.Companion.USERS
import com.aptivist.livesapp.model.UserData
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseRepository : IRepository {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val rootRef = FirebaseFirestore.getInstance()
    private val usersRef = rootRef.collection(USERS)

    constructor()

    override fun firebaseSignIn(authCredential: AuthCredential?): MutableLiveData<UserData>? {
        val authenticatedUserMutableLiveData: MutableLiveData<UserData> = MutableLiveData<UserData>()

        firebaseAuth.signInWithCredential(authCredential!!)
            .addOnCompleteListener { authTask: Task<AuthResult> ->
                if (authTask.isSuccessful) {
                    val isNewUser =
                        authTask.result!!.additionalUserInfo!!.isNewUser
                    val firebaseUser = firebaseAuth.currentUser
                    if (firebaseUser != null) {
                        val uid = firebaseUser.uid
                        val name = firebaseUser.displayName
                        val email = firebaseUser.email
                        val user = UserData(uid,
                            name,email,"",
                            isAuthenticated = true,
                            isNew = false,
                            isCreated = true
                        )
                        user.isNew = isNewUser
                        authenticatedUserMutableLiveData.value = user
                    }
                } else {
                    Log.d("*****",authTask.exception!!.message)
                }
            }
        return authenticatedUserMutableLiveData
    }

    override fun createUserIfNotExists(authenticatedUser: UserData): MutableLiveData<UserData>? {
        val newUserMutableLiveData: MutableLiveData<UserData> = MutableLiveData<UserData>()
        firebaseAuth.createUserWithEmailAndPassword(authenticatedUser.emailUser.toString(), authenticatedUser.userUser.toString())
            .addOnCompleteListener{ uidTask ->
                if (uidTask.isSuccessful){
                    authenticatedUser.isCreated = true
                    newUserMutableLiveData.setValue(authenticatedUser)
                }
                else {
                    Log.d("*****",uidTask.exception!!.message)
                }

            }

        return newUserMutableLiveData
    }
/*
    override fun createUserInFirestoreIfNotExists(authenticatedUser: UserData): MutableLiveData<UserData>? {
        val newUserMutableLiveData: MutableLiveData<UserData> = MutableLiveData<UserData>()
        val uidRef: DocumentReference =
            usersRef.document(authenticatedUser.uid!!)
        uidRef.get()
            .addOnCompleteListener { uidTask: Task<DocumentSnapshot?> ->
                if (uidTask.isSuccessful) {
                    val document = uidTask.result
                    if (!document!!.exists()) {
                        uidRef.set(authenticatedUser)
                            .addOnCompleteListener { userCreationTask: Task<Void?> ->
                                if (userCreationTask.isSuccessful) {
                                    authenticatedUser.isCreated = true
                                    newUserMutableLiveData.setValue(authenticatedUser)
                                } else {
                                    Log.d("*****",uidTask.exception!!.message)
                                }
                            }
                    } else {
                        newUserMutableLiveData.setValue(authenticatedUser)
                    }
                } else {
                    Log.d("*****",uidTask.exception!!.message)
                }
            }
        return newUserMutableLiveData
    }
*/
}