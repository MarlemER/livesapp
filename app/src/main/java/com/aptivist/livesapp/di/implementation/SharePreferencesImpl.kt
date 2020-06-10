package com.aptivist.livesapp.di.implementation

import android.content.Context
import android.content.SharedPreferences
import com.aptivist.livesapp.di.interfaces.ISharedPreferences
import com.aptivist.livesapp.helpers.Constants.Companion.SHARE_PREFERENCES_MAIN

class SharePreferencesImpl:ISharedPreferences {
    //set the data for the view
    private lateinit var appPref: SharedPreferences
    private lateinit var spEditor: SharedPreferences.Editor

    override fun setTokenFacebook(key: String, value: String?) {
            spEditor=appPref.edit()
            spEditor.putString(key,value)
            spEditor.apply()
    }

    override fun getTokenFacebook(key: String): String? = appPref.getString(key,null)

    //singleton
    companion object{
        fun newInstance(context: Context):SharePreferencesImpl?
        {
            var prefHelp=SharePreferencesImpl()
            prefHelp.appPref=context.getSharedPreferences(SHARE_PREFERENCES_MAIN, Context.MODE_PRIVATE)
            return prefHelp
        }
    }

}