package com.aptivist.livesapp.di.implementation

import android.util.Patterns
import com.aptivist.livesapp.di.interfaces.ISessionSignin
import com.aptivist.livesapp.helpers.EnumUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

open class UserSessionImpl(): ISessionSignin {
    override fun getUser(): FirebaseUser? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getUserStatus(): EnumUser {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getEmailUser(email: String): Boolean {
        return ((!email.isNullOrEmpty()&&Patterns.EMAIL_ADDRESS.matcher(email).matches()))
    }

    override fun getPassUser(pass: String): Boolean {
        return (!pass.isNullOrEmpty()&&!(pass.length<7))
    }

}