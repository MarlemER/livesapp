package com.aptivist.livesapp.repository.dao

import com.aptivist.livesapp.helpers.EnumUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

//@Dao
interface FirebaseRepository {
    /*@Insert(onConflict = onConflictStrategy.REPLACE)
    suspend fun insert(authToken:AuthToken):Long
    @Query("SELECT *FROM auth_token where token  = :token")
    suspend fun searchByToken(token:String):AuthToken*/

    fun getUser():FirebaseUser?
    fun getUsrStatus():EnumUser?
}