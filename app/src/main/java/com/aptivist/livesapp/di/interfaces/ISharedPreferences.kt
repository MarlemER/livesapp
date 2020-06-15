package com.aptivist.livesapp.di.interfaces

import android.content.SharedPreferences

interface ISharedPreferences {
    //set the data for the view
    //save
    fun setTokenFacebook(key:String,value: String?)
    //load
    fun getTokenFacebook(key: String):String?
    //clear
    fun clearTokenFacebook(vararg keys:String)
}