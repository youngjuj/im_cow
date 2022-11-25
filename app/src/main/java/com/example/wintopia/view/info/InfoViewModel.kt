package com.example.wintopia.view.info

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.wintopia.data.MilkCowPhoto
import com.example.wintopia.retrofit.RetrofitClient
import com.example.wintopia.retrofit.RetrofitInterface
import com.example.wintopia.view.utilssd.API_
import com.example.wintopia.view.utilssd.Constants
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


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