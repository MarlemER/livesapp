package com.aptivist.livesapp.di.implementation

import com.aptivist.livesapp.di.interfaces.IFirebaseInstance
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseImpl:IFirebaseInstance {
    override fun getFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    override fun getFirebaseFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    override fun getFirebaseUser(): FirebaseUser? {
        return FirebaseAuth.getInstance().currentUser
    }

}