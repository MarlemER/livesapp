package com.aptivist.livesapp.di.implementation

import android.app.AlertDialog
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.ContextThemeWrapper
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.aptivist.livesapp.R
import com.aptivist.livesapp.di.interfaces.IMessagesDialogs
import com.aptivist.livesapp.helpers.Utilities

class MessagesDialogsImpl:IMessagesDialogs, Utilities() {
    override fun showSuccessful(title: String, message: String, icon: Drawable, context: Context) {
        AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message)
            .setIcon(R.drawable.logosz)
            .create().show()
    }

    override fun showError(title: String, message: String, icon: Drawable, context: Context) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showCancel(title: String, message: String, icon: Drawable, context: Context) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showMessageOneOption(title: String,message: String?,setTitlePositiveButton: String,icon: Int,context: Context) {
        AlertDialog.Builder(ContextThemeWrapper(context,R.style.AlertDialogCustom))
            .setTitle(title)
            .setMessage(message)
            .setIcon(icon)
            .setPositiveButton(setTitlePositiveButton){_,_->
                if (message != null) {
                    showToast(context,message)
                }else{

                }
            }
            .create().show()
    }

    override fun showMessageLocationGM(
        title: String,
        message: String?,
        setTitlePositiveButton: String,
        icon: Int,
        context: Context,
        view: View,
        messageConfirmation:String,
        longitude:Double,
        latitude: Double
    ) {
        AlertDialog.Builder(ContextThemeWrapper(context,R.style.AlertDialogCustom))
            .setTitle(title)
            .setMessage(message)
            .setIcon(icon)
            .setPositiveButton(setTitlePositiveButton
            ) { _, _ ->

                var bundle = bundleOf("addressLocation" to messageConfirmation)
                            bundleOf("longitudeLocation" to longitude)
                            bundleOf("latitudeLocation" to latitude)
                var navController: NavController = view.findNavController()
                navController.navigate(R.id.goToIncidenceFragment, bundle)
                showToast(context,messageConfirmation)
            }
            .create().show()
    }

    override fun showMessagePreview(title: String, message: String?, context: Context) {
        AlertDialog.Builder(ContextThemeWrapper(context,R.style.AlertDialogCustom))
            .setTitle(title)
            .setMessage(message)
            .create().show()
    }

    override fun showMessageTwoOption(title: String,message: String?,setTitlePositiveButton: String,setTitleNegativeButton: String,icon: Int, context: Context) {
        AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message)
            .setIcon(icon)
            .setPositiveButton(setTitlePositiveButton){_,_->
                if (message != null) {
                    showToast(context,message)
                }
            }
            .setNegativeButton(setTitleNegativeButton){_,_->
                if (message != null) {
                    showToast(context,message)
                }
            }
            .create().show()
    }

    override fun showToast(context: Context,message: String) {
        Toast.makeText(context,message,Toast.LENGTH_LONG).show()
    }

    override fun showMessageTransaction(
        title: String,
        message: String?,
        setTitlePositiveButton: String,
        setTitleNegativeButton:String,
        icon: Int,
        context: Context,
        view:View,
        messageConfirmation:String
    ) {
        AlertDialog.Builder(ContextThemeWrapper(context,R.style.AlertDialogCustom))
            .setTitle(title)
            .setMessage(message)
            .setIcon(icon)
            .setPositiveButton(setTitlePositiveButton
            ) { _, _ ->

                var bundle = bundleOf("dataClassNewIncidence" to messageConfirmation)
                var navController: NavController = view.findNavController()
                navController.navigate(R.id.goToHomeMap, bundle)
                showToast(context,messageConfirmation)
            }
            .setNegativeButton(setTitleNegativeButton){_,_->
                if (message != null) {
                    showToast(context,message)
                }
            }
            .create().show()
    }

}