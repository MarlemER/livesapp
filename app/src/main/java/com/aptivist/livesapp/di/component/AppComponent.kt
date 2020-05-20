package com.aptivist.livesapp.di.component

import com.aptivist.livesapp.MainApplication
import com.aptivist.livesapp.di.module.AppModule
import com.aptivist.livesapp.di.implementation.ImplementationModule
import dagger.Component
import javax.inject.Singleton

@Singleton //1 vez
@Component (modules= [AppModule::class, ImplementationModule::class])
interface AppComponent {
    fun start(target:MainApplication){

    }
}