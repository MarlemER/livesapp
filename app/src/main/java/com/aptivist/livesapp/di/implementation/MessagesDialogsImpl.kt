package com.aptivist.livesapp.di.implementation

import android.app.AlertDialog
import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.Toast
import com.aptivist.livesapp.R
import com.aptivist.livesapp.di.interfaces.IMessagesDialogs

class MessagesDialogsImpl:IMessagesDialogs {
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

    override fun showMessage(title: String,message: String,setTitlePositiveButton: String,setTitleNegativeButton: String,icon: Drawable, context: Context) {
        AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message)
            .setIcon(icon)
            .setPositiveButton(setTitlePositiveButton){_,_->
                showToast(context,message)
            }
            .setNegativeButton(setTitleNegativeButton){_,_->
                showToast(context,message)
            }
            .create().show()
    }

    override fun showToast(context: Context,message: String) {
        Toast.makeText(context,message,Toast.LENGTH_LONG).show()
    }
}