package com.aptivist.livesapp.di.interfaces

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.View

interface IMessagesDialogs {
    fun showSuccessful(title:String,message:String,icon:Drawable,context: Context)
    fun showError(title:String,message:String,icon: Drawable,context: Context)
    fun showCancel(title:String,message:String,icon: Drawable,context: Context)
    fun showMessageTwoOption(title:String,message:String?,setTitlePositiveButton:String,setTitleNegativeButton:String,icon:Int,context: Context)
    fun showMessageOneOption(title:String,message:String?,setTitlePositiveButton:String,icon:Int,context: Context)
    fun showMessageLocationGM(title:String,message:String?,setTitlePositiveButton:String,icon:Int,context: Context,view: View, messageConfirmation:String, longitudeLocation:Double, latitudeLocation:Double)
    fun showMessagePreview(title:String,message: String?,context: Context)
    fun showToast(context: Context,message: String)
    fun showMessageTransaction(title:String,message:String?,setTitlePositiveButton:String,setTitleNegativeButton:String,icon:Int,context: Context,view:View,messageConfirmation:String,messageCancel:String):Boolean
}