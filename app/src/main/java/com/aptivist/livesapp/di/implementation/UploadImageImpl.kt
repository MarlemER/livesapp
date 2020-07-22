package com.aptivist.livesapp.di.implementation

import android.net.Uri
import com.aptivist.livesapp.di.interfaces.IUploadImageService
import com.google.android.gms.tasks.Tasks.await
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import java.lang.Exception
import java.util.*

class UploadImageImpl(private val mStorageReference: StorageReference):IUploadImageService {
    override fun uploadImage(uri: Uri): String? {
        val imgReference = mStorageReference.child("images/${UUID.randomUUID()}")
        val downloadURL = runBlocking(Dispatchers.IO) {
            try {
                await(imgReference.putFile(uri))
                await(imgReference.downloadUrl).toString()
            }catch (ex:Exception){
                ex.printStackTrace()
                null
            }
        }
        return  downloadURL
    }
}