package com.aptivist.livesapp.di.implementation

import com.aptivist.livesapp.repository.dao.FirebaseRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides

@Module
class ImplementationModule {
    //create functions
    @Provides
    fun validationUser(auth:FirebaseAuth): FirebaseRepository = SessionUserImpl(auth)

}