package com.aptivist.livesapp.di.interfaces

import android.net.Uri
import com.aptivist.livesapp.model.IncidenceData

interface IUploadDataService {
    fun uploadImage(uri: Uri):String?
    fun uploadData(newIncidenceData: IncidenceData):Boolean
}