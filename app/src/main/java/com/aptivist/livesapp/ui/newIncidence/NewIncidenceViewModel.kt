package com.aptivist.livesapp.ui.newIncidence

import android.net.Uri
import android.widget.ImageView
import androidx.lifecycle.MutableLiveData
import com.aptivist.livesapp.BaseViewModel
import com.aptivist.livesapp.di.interfaces.IUploadImageService

class NewIncidenceViewModel(private val imageService:IUploadImageService) : BaseViewModel() {
    private val saveImage = MutableLiveData<String>()

    fun uploadImage(uri: Uri){
        saveImage.value = imageService.uploadImage(uri)
    }
    fun getImageURL(imageView: ImageView){
        if(saveImage.value!=null || saveImage.value==""){
            setImage(imageView,"https://source.unsplash.com/640x480?pets")
        }else{
            setImage(imageView,saveImage.value)
        }
    }
}
