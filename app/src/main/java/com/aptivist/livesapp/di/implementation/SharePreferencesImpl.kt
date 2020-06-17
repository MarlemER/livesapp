package com.aptivist.livesapp.di.implementation

import android.content.Context
import android.content.SharedPreferences
import com.aptivist.livesapp.di.interfaces.ISharedPreferences
import com.aptivist.livesapp.helpers.Constants.Companion.SHAREPREF_TOKEN_FACEBOOK
import com.aptivist.livesapp.helpers.Constants.Companion.SHARE_PREFERENCES_MAIN

class SharePreferencesImpl:ISharedPreferences {
    //set the data for the view
    private lateinit var appPref: SharedPreferences
    private lateinit var spEditor: SharedPreferences.Editor

    override fun setDataFirebase(key: String, value: String?) {
            spEditor=appPref.edit()
            spEditor.putString(key,value)
            spEditor.apply()
    }

    override fun getDataFirebase(key: String): String? = appPref.getString(key,null)
    override fun clearDataFirebase(key: String) = setDataFirebase(key,null)
    //singleton
    companion object{
        fun newInstance(context: Context):SharePreferencesImpl?
        {
            var prefHelp=SharePreferencesImpl()
            prefHelp.appPref=context.getSharedPreferences(SHARE_PREFERENCES_MAIN, Context.MODE_PRIVATE)
            return prefHelp
        }
    }

    private fun clearData(){
        var prefHelp=SharePreferencesImpl()
        prefHelp.getDataFirebase(SHAREPREF_TOKEN_FACEBOOK)?:""
    }

}