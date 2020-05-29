package com.aptivist.livesapp.repository

import androidx.lifecycle.MutableLiveData
import com.aptivist.livesapp.model.UserData
import com.google.firebase.auth.AuthCredential

class LocalRepository : IRepository {

    override fun createUserIfNotExists(authenticatedUser: UserData): MutableLiveData<UserData>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun firebaseSignIn(authCredential: AuthCredential?): MutableLiveData<UserData>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}