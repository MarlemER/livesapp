package com.aptivist.livesapp

import android.widget.ImageView
import androidx.lifecycle.ViewModel
import com.squareup.picasso.Picasso

open class BaseViewModel:ViewModel() {

    fun setImage(imageView: ImageView, url:String?){
        Picasso.get().load(url).into(imageView)
    }

}