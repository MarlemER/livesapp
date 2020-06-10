package com.aptivist.livesapp.ui.main

import androidx.lifecycle.LiveData
import com.aptivist.livesapp.di.interfaces.IFirebaseInstance
import com.aptivist.livesapp.repository.FirebaseRepository
import com.aptivist.livesapp.viewmodel.BaseViewModel

class MainViewModel:BaseViewModel() {
    var fireBaseCredencial: IFirebaseInstance? = null
    private var authRepository = FirebaseRepository(fireBaseCredencial)

    var isLogoutUserLiveData: LiveData<Boolean>? = null

    fun logout(){
        isLogoutUserLiveData = authRepository?.logout()
    }
}