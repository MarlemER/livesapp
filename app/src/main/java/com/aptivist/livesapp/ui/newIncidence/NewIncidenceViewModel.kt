package com.aptivist.livesapp.ui.newIncidence

import android.net.Uri
import android.widget.ImageView
import androidx.lifecycle.MutableLiveData
import bolts.Bolts
import com.aptivist.livesapp.BaseViewModel
import com.aptivist.livesapp.di.interfaces.IFirebaseResult
import com.aptivist.livesapp.di.interfaces.IUploadDataService
import com.aptivist.livesapp.model.IncidenceData

class NewIncidenceViewModel(private val dataService:IUploadDataService) : BaseViewModel() {
    private val saveImage = MutableLiveData<String>()
    private val saveNewIncidence = MutableLiveData<Boolean>()
    private val dataIncidence = MutableLiveData<IncidenceData>()
    val progressVisible:MutableLiveData<Boolean>? = MutableLiveData<Boolean>()

    fun uploadImage(uri: Uri):String?{
       saveImage.value = dataService.uploadImage(uri)
        return saveImage.value
    }
    fun getImageURL(imageView: ImageView){
        if(saveImage.value!=null || saveImage.value==""){
            setImage(imageView,"https://source.unsplash.com/640x480?pets")
        }else{
            setImage(imageView,saveImage.value)
        }
    }
    fun saveDBnewInicidence(newIncidence:IncidenceData,iFirebaseResult: IFirebaseResult){
        progressVisible?.value = true
            dataService.uploadData(newIncidence,iFirebaseResult)
    }
}
