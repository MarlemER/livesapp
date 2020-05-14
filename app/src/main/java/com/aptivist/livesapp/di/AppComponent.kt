package com.aptivist.livesapp.di

import com.aptivist.livesapp.MainApplication
import dagger.Component
import javax.inject.Singleton

@Singleton //1 vez
@Component (modules= [AppModule::class,ImplementationModule::class])
interface AppComponent {
    fun start(target:MainApplication){

    }
}