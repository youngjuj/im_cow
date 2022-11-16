package com.example.wintopia.view.signup

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SignUpViewModel: ViewModel() {

    // Create a LiveData with a String
    val id = MutableLiveData<String>()
    val pw = MutableLiveData<String>()

}