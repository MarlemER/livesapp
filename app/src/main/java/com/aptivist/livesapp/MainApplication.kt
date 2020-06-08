package com.aptivist.livesapp

import android.app.Application
import com.aptivist.livesapp.di.implementation.FirebaseImpl
import com.aptivist.livesapp.di.implementation.UserSessionImpl
import com.aptivist.livesapp.di.interfaces.IFirebaseInstance
import com.aptivist.livesapp.di.interfaces.ISessionSignin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.dsl.module

class MainApplication:Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@MainApplication)
            modules(singletonModule, implementationModule, viewModelModule)
        }
    }

    private val singletonModule = module {
        single {
            //FirebaseAuth.getInstance()
        }
    }

    private val implementationModule = module {
       // factory<ISessionUser> { UserSessionImpl(get()) }
        factory<IFirebaseInstance> { FirebaseImpl() }
        factory<ISessionSignin> { UserSessionImpl() }
    }

    private val viewModelModule = module {
       // viewModel { SigninViewModel() }
    }
}