package com.aptivist.livesapp.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aptivist.livesapp.MainApplication
import com.aptivist.livesapp.di.interfaces.IFirebaseInstance
import com.aptivist.livesapp.di.interfaces.IMessagesDialogs
import com.aptivist.livesapp.helpers.Constants.Companion.PHOTO_USER_DEFAULT
import com.aptivist.livesapp.helpers.Constants.Companion.USERS
import com.aptivist.livesapp.model.UserData
import com.facebook.login.LoginManager
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.android.ext.android.inject
import org.koin.java.KoinJavaComponent.inject

class FirebaseRepository(private val firebaseAccess:IFirebaseInstance?) : IRepository {


    private val firebaseAuth = FirebaseAuth.getInstance()
   // private val rootRef = FirebaseFirestore.getInstance()
    private val usersRef = firebaseAccess?.getFirebaseFirestore()?.collection(USERS)

    //constructor()


    override fun firebaseSignIn(authCredential: AuthCredential?): MutableLiveData<UserData>? {
        val authenticatedUserMutableLiveData: MutableLiveData<UserData> = MutableLiveData<UserData>()
        val authenticatedUserErrorMutableLiveData: MutableLiveData<String> = MutableLiveData<String>()
        val user:UserData? = null
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
                            photoUser = "$photo?type=large",
                            messageResult = ""
                        )
                        user.isNew = isNewUser

                        authenticatedUserMutableLiveData.value = user
                    }
                } else {
                    Log.d("*****",authTask.exception?.message)
                    authenticatedUserMutableLiveData.value = UserData("","","","",false,false,false,"",authTask.exception?.message)
                }
            }
        return authenticatedUserMutableLiveData
    }

    override fun logout():MutableLiveData<Boolean>? {
        val issucces: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
        LoginManager.getInstance().logOut()
        firebaseAuth.signOut()
        issucces.value = true
        return issucces
    }

    override fun resertPass(email: String): MutableLiveData<Boolean>? {
        val isSucces: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
        firebaseAuth.sendPasswordResetEmail(email)

            .addOnCompleteListener { task ->
                isSucces.value = task.isSuccessful
               // isSucces.value = task.isSuccessful
            }
/*
            .addOnSuccessListener { task->
                //Log.d("*****","VALIDAOK")
                isSucces.value = true
            }
            .addOnFailureListener{task->
                //Log.d("*****","VALIDACANCEL"+task.message)
                isSucces.value = false
            }*/
        return  isSucces
    }

    override fun tokenFacebook(): MutableLiveData<String> {
        val string:MutableLiveData<String> = MutableLiveData<String>()
        return string
    }


    override fun createUserIfNotExists(authenticatedUser: UserData): MutableLiveData<UserData>? {
        val newUserMutableLiveData: MutableLiveData<UserData> = MutableLiveData<UserData>()
        firebaseAuth.createUserWithEmailAndPassword(authenticatedUser.emailUser.toString(), authenticatedUser.userUser.toString())
            ?.addOnCompleteListener{ uidTask ->
                if (uidTask.isSuccessful){
                    authenticatedUser.isCreated = true
                    authenticatedUser.photoUser = PHOTO_USER_DEFAULT
                    firebaseAuth.currentUser?.sendEmailVerification()
                    newUserMutableLiveData.setValue(authenticatedUser)
                }
                else {
                   // Log.d("*****",uidTask.exception?.message)

                    authenticatedUser.isCreated = false
                    authenticatedUser.messageResult = uidTask.exception?.message
                    newUserMutableLiveData.setValue(authenticatedUser)
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