package com.aptivist.livesapp

import android.app.Application
import com.aptivist.livesapp.di.AppComponent
import com.aptivist.livesapp.di.AppModule
import com.aptivist.livesapp.di.DaggerAppComponent

//instancerar para no psar el contexto
//build make app para crear dagger
class MainApplication:Application() {
    //inicialize dagger
    lateinit var myComponent:AppComponent
    private fun initDagger(app:MainApplication):AppComponent = DaggerAppComponent.builder().appModule(AppModule(app)).build()
    override fun onCreate() {
        super.onCreate()
        myComponent = initDagger(this)
    }
}