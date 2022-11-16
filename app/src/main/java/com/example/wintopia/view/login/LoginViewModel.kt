package com.example.wintopia.view.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel: ViewModel() {

    // Create a LiveData with a String
    val id = MutableLiveData<String>()
    val pw = MutableLiveData<String>()

}