package com.example.wintopia.view.info

import androidx.lifecycle.MutableLiveData


class InfoViewModel {

    // Create a LiveData with a String
    val name = MutableLiveData<String>()
    val id = MutableLiveData<String>()
    val birth = MutableLiveData<String>()
    val gender = MutableLiveData<String>()
    val vaccine = MutableLiveData<String>()
    val kind = MutableLiveData<String>()
    val image = MutableLiveData<String>()
    val event = MutableLiveData<String>()



}