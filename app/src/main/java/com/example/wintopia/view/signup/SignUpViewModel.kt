package com.example.wintopia.view.signup

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SignUpViewModel: ViewModel() {

    // Create a LiveData with a String
    val signUpId = MutableLiveData<String>()
    val signUpPw1 = MutableLiveData<String>()
    val signUpPw2 = MutableLiveData<String>()
}