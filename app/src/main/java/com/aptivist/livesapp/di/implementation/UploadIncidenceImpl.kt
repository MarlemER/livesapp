package com.aptivist.livesapp.di.implementation

import android.net.Uri
import android.util.Log
import com.aptivist.livesapp.di.interfaces.IFirebaseResult
import com.aptivist.livesapp.di.interfaces.IUploadDataService
import com.aptivist.livesapp.helpers.Constants.Companion.INCIDENCE_ENTITY_DB
import com.aptivist.livesapp.model.IncidenceData
import com.google.android.gms.tasks.Tasks.await
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.koin.android.ext.android.inject
import org.koin.java.KoinJavaComponent.inject
import java.lang.Exception
import java.util.*

class UploadIncidenceImpl(private val mStorageReference: StorageReference, private val dbReference: DatabaseReference):IUploadDataService {
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

    override fun uploadData(newIncidenceData: IncidenceData, iFirebaseResult: IFirebaseResult) {
       // var database=FirebaseDatabase.getInstance().reference
        val database = dbReference
        //var reference: DatabaseReference = database.getReference("server/saving-data/fireblog")
        //val reference:DatabaseReference = database.reference.child("Incidence")
                        try {
                        database.child(INCIDENCE_ENTITY_DB)
                            .child(newIncidenceData.idUser.toString())
                            .child(UUID.randomUUID().toString()).setValue(
                            IncidenceData(newIncidenceData.idIncidence,newIncidenceData.nameIncidence,newIncidenceData.pictureUrl,
                                newIncidenceData.locationLongitude,newIncidenceData.locationLatitude,newIncidenceData.dateTime,newIncidenceData.idUser,newIncidenceData.remark
                            )
                        )
                            .addOnSuccessListener {
                                iFirebaseResult.onSuccess()
                            }
                            .addOnFailureListener {
                                iFirebaseResult.onFailed()
                            }
                        Log.d("*****", "Test db")
                        }catch (ex:Exception){
                            iFirebaseResult.onFailed()
                        }
       // return insertIncidence
    }

}