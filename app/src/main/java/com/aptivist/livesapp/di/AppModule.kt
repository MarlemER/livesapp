package com.aptivist.livesapp.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

//ayuda a pasar el contexto
@Module
class AppModule (private var app:Application){
    @Provides
    @Singleton
    fun providerContext():Context = app
}