package com.aptivist.livesapp.di.interfaces

import android.net.Uri

interface IUploadImageService {
    fun uploadImage(uri: Uri):String?
}