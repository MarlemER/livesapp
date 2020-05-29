package com.aptivist.livesapp.di.implementation

import com.aptivist.livesapp.helpers.EnumUser
import com.aptivist.livesapp.interfaces.ISessionUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

open class UserSessionImpl(private val auth: FirebaseAuth): ISessionUser {
    override fun getUser(): FirebaseUser? {
        return auth.currentUser
    }

    override fun getUserStatus(): EnumUser? {
        val user = getUser()
        return if(user!=null) EnumUser.Signed else EnumUser.Unsigned
    }
}