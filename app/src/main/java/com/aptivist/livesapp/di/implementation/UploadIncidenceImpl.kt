package com.aptivist.livesapp.di.implementation

import android.net.Uri
import android.util.Log
import com.aptivist.livesapp.di.interfaces.IUploadDataService
import com.aptivist.livesapp.helpers.Constants.Companion.INCIDENCE_ENTITY_DB
import com.aptivist.livesapp.model.IncidenceData
import com.google.android.gms.tasks.Tasks.await
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import java.lang.Exception
import java.util.*

class UploadIncidenceImpl(private val mStorageReference: StorageReference):IUploadDataService {
    override fun uploadImage(uri: Uri): String? {
        val imgReference = mStorageReference.child("images/${UUID.randomUUID()}")
        val downloadURL = runBlocking(Dispatchers.IO) {
            try {
                await(imgReference.putFile(uri))
                await(imgReference.downloadUrl).toString()
            }catch (ex:Exception){
                //ex.printStackTrace()
                null
            }
        }
        return  downloadURL
    }

    override fun uploadData(newIncidenceData: IncidenceData): Boolean {
        var database=FirebaseDatabase.getInstance().reference
        var insertIncidence:Boolean = false
        //var reference: DatabaseReference = database.getReference("server/saving-data/fireblog")
        //val reference:DatabaseReference = database.reference.child("Incidence")
         runBlocking(Dispatchers.IO) {
                try {
                    database.child(INCIDENCE_ENTITY_DB).child(newIncidenceData.idUser.toString()).child(UUID.randomUUID().toString()).setValue(IncidenceData(newIncidenceData.idIncidence,newIncidenceData.nameIncidence,newIncidenceData.pictureUrl,newIncidenceData.locationLongitude,newIncidenceData.locationLatitude,newIncidenceData.dateTime,newIncidenceData.idUser))
                        .addOnSuccessListener {
                           insertIncidence = true
                        }
                        .addOnFailureListener {
                           insertIncidence = false
                        }
                    Log.d("*****","Test db")
                }catch (ex:Exception){
                    null
                }
        }
        return insertIncidence
    }

}