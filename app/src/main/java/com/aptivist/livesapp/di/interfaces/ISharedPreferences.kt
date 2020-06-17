package com.aptivist.livesapp.di.interfaces

import android.content.SharedPreferences

interface ISharedPreferences {
    //set the data for the view
    //save
    fun setDataFirebase(key:String,value: String?)
    //load
    fun getDataFirebase(key: String):String?
    //clear
    fun clearDataFirebase(key:String)
}