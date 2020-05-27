package com.aptivist.livesapp

import android.app.Application
import com.aptivist.livesapp.di.component.AppComponent
import com.aptivist.livesapp.di.module.AppModule
import com.aptivist.livesapp.di.component.DaggerAppComponent

//instancerar para no psar el contexto
//build make app para crear dagger
class MainApplication:Application() {
    //inicialize dagger
    lateinit var myComponent: AppComponent
    private fun initDagger(app:MainApplication): AppComponent = DaggerAppComponent.builder().appModule(
        AppModule(app)
    ).build()
    override fun onCreate() {
        super.onCreate()
        //inicializate myComponent
        myComponent = initDagger(this)
    }

}