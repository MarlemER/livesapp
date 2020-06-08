package com.aptivist.livesapp.di.interfaces

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

interface IFirebaseInstance {
    fun getFirebaseAuth():FirebaseAuth
    fun getFirebaseFirestore():FirebaseFirestore
    fun getFirebaseUser():FirebaseUser?
}