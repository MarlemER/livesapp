package com.aptivist.livesapp

import android.app.Application
import com.aptivist.livesapp.di.implementation.FirebaseImpl
import com.aptivist.livesapp.di.implementation.MessagesDialogsImpl
import com.aptivist.livesapp.di.implementation.UploadIncidenceImpl
import com.aptivist.livesapp.di.implementation.UserSessionImpl
import com.aptivist.livesapp.di.interfaces.IFirebaseInstance
import com.aptivist.livesapp.di.interfaces.IMessagesDialogs
import com.aptivist.livesapp.di.interfaces.ISessionSignin
import com.aptivist.livesapp.di.interfaces.IUploadDataService
import com.aptivist.livesapp.helpers.Utilities
import com.aptivist.livesapp.ui.newIncidence.NewIncidenceViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.android.viewmodel.dsl.viewModel
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
        single { FirebaseAuth.getInstance() }
        single{ FirebaseStorage.getInstance().reference }
        single { Utilities() }
    }

    private val implementationModule = module {
       // factory<ISessionUser> { UserSessionImpl(get()) }
        factory<IFirebaseInstance> { FirebaseImpl() }
        factory<ISessionSignin> { UserSessionImpl() }
        factory<IMessagesDialogs> { MessagesDialogsImpl() }
        factory<IUploadDataService> { UploadIncidenceImpl(get()) }
    }

    private val viewModelModule = module {
       // viewModel { SigninViewModel() }
        viewModel { NewIncidenceViewModel(get()) }
    }
}