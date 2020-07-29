package com.aptivist.livesapp

import android.content.Intent
import androidx.fragment.app.Fragment
import com.aptivist.livesapp.di.interfaces.IUploadDataService
import com.aptivist.livesapp.helpers.Constants
import com.aptivist.livesapp.model.IncidenceData
import com.google.firebase.auth.FirebaseAuth
import org.koin.android.ext.android.inject

open class BaseFragment:Fragment(){
    val mAuth: FirebaseAuth by inject()
    val uploadIncidence:IUploadDataService by inject()

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
    fun saveIncidenceData(incidenceData: IncidenceData){
        uploadIncidence.uploadData(incidenceData)
    }

}