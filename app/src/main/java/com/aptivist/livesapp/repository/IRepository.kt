package com.aptivist.livesapp.repository

import androidx.lifecycle.MutableLiveData
import com.aptivist.livesapp.di.interfaces.IFirebaseInstance
import com.aptivist.livesapp.model.UserData
import com.google.firebase.auth.AuthCredential

interface IRepository {
    fun createUserIfNotExists(authenticatedUser: UserData): MutableLiveData<UserData>?
    fun firebaseSignIn(authCredential: AuthCredential?): MutableLiveData<UserData>?

}