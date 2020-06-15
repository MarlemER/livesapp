package com.aptivist.livesapp.di.interfaces

import android.content.Context
import android.graphics.drawable.Drawable

interface IMessagesDialogs {
    fun showSuccessful(title:String,message:String,icon:Drawable,context: Context)
    fun showError(title:String,message:String,icon: Drawable,context: Context)
    fun showCancel(title:String,message:String,icon: Drawable,context: Context)
    fun showMessage(title:String,message:String,setTitlePositiveButton:String,setTitleNegativeButton:String,icon: Drawable,context: Context)
    fun showToast(context: Context,message: String)
}