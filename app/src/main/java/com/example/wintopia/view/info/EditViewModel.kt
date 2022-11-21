package com.example.wintopia.view.info

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class EditViewModel: ViewModel() {

    // Create a LiveData with a String
    val name = MutableLiveData<String>()
    val id = MutableLiveData<String>()
    val birth = MutableLiveData<String>()
    val status = MutableLiveData<String>()
    val tel = MutableLiveData<String>()
    val fax = MutableLiveData<String>()
}