package com.aptivist.livesapp.di.interfaces

import com.aptivist.livesapp.helpers.EnumUser
import com.google.firebase.auth.FirebaseUser

interface ISessionSignin {
    fun getUser():FirebaseUser?
    fun getUserStatus(): EnumUser
    fun getEmailUser(email:String):Boolean
    fun getPassUser(pass:String):Boolean
}