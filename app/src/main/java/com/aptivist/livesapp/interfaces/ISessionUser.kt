package com.aptivist.livesapp.interfaces

import com.aptivist.livesapp.helpers.EnumUser
import com.google.firebase.auth.FirebaseUser

interface ISessionUser {

    fun getUser(): FirebaseUser?
    fun getUserStatus(): EnumUser?

}