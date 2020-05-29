package com.aptivist.livesapp.di.implementation

import com.aptivist.livesapp.helpers.EnumUser
import com.aptivist.livesapp.repository.dao.FirebaseRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.Module
import javax.inject.Inject


@Module
class SessionUserImpl @Inject constructor (private val auth: FirebaseAuth):FirebaseRepository {
    override fun getUser(): FirebaseUser? {
        return auth.currentUser
    }

    override fun getUsrStatus(): EnumUser? {
        val user = getUser()
        return if(user!=null) EnumUser.Signed else EnumUser.Unsigned
    }

}
