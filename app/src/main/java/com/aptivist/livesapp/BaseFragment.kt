package com.aptivist.livesapp

import android.content.Intent
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.fragment.app.Fragment
import com.aptivist.livesapp.helpers.Constants
import com.google.firebase.auth.FirebaseAuth
import org.koin.android.ext.android.inject
import org.koin.java.KoinJavaComponent.inject

open class BaseFragment:Fragment() {
    val mAuth: FirebaseAuth by inject()

    fun pictureImageFromGallery(){
        val intent = Intent().apply {
            type = "image/'"
            action = Intent.ACTION_GET_CONTENT
        }
        startActivityForResult(
            Intent.createChooser(intent,"Select an image from.."),
            Constants.UPLOAD_IMAGE_INCIDENCE
        )
        intent.type = ""
    }
}