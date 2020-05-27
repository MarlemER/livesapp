package com.aptivist.livesapp.di.component

import com.aptivist.livesapp.MainApplication
import com.aptivist.livesapp.di.module.AppModule
import com.aptivist.livesapp.di.implementation.ImplementationModule
import com.aptivist.livesapp.di.implementation.SessionUserImpl
import com.aptivist.livesapp.di.module.SingletonModule
import dagger.Component
import javax.inject.Singleton

@Singleton //1 vez
@Component (modules= [AppModule::class, SingletonModule::class, ImplementationModule::class, SessionUserImpl::class])
interface AppComponent {
    fun start(target:MainApplication){

    }
}
