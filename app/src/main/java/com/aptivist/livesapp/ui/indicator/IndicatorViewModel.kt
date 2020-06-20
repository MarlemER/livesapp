package com.aptivist.livesapp.ui.indicator

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class IndicatorViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Indicator"
    }
    val text: LiveData<String> = _text
}