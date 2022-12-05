package com.example.wintopia.view.edit

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.wintopia.retrofit.RetrofitClient
import com.example.wintopia.retrofit.RetrofitInterface
import com.example.wintopia.view.utilssd.API_
import com.example.wintopia.view.utilssd.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditViewModel: ViewModel() {

    // Create a LiveData with a String
    val name = MutableLiveData<String>()
    val id = MutableLiveData<String>()
    val birth = MutableLiveData<String>()
    val variety = MutableLiveData<String>()
    val gender = MutableLiveData<String>()
    val vaccine = MutableLiveData<String>()
    val pregnancy = MutableLiveData<String>()
    val milk = MutableLiveData<String>()
    val castration = MutableLiveData<String>()
    val wish = MutableLiveData<String>()
    val event = MutableLiveData<String>()


    fun cowInfoOne(cow_id: String, milkCowInfoModel: MilkCowInfoModel){
        //Retrofit 인스턴스 생성
        val retrofit = RetrofitClient.getInstnace(API_.BASE_URL)
        val service = retrofit.create(RetrofitInterface::class.java) // 레트로핏 인터페이스 객체 구현

        val call: Call<String>? = service.getData(cow_id, milkCowInfoModel)
        call!!.enqueue(object : Callback<String?> {
            override fun onResponse(call: Call<String?>?, response: Response<String?>) {
                Log.d(Constants.TAG, "onResponse")
                if (response.isSuccessful()) {
                    Log.e(Constants.TAG, "onResponse success")
//                        val result: UserList? = response.body()

                    // 서버에서 응답받은 데이터
                    val result = "${response.body()}"
                    Log.d(TAG, "$result")
                    event.value = "success"

                } else {
                    // 서버통신 실패
                    event.value = "fail1"
                    Log.e(Constants.TAG, "onResponse fail")
                }
            }
            override fun onFailure(call: Call<String?>?, t: Throwable) {
                // 통신 실패
                event.value = "fail2"
                Log.e(Constants.TAG, "onFailure: " + t.message)
            }
        })
    }

    fun cowInfoUpdate(cow_id: String, milkCowInfoModel: MilkCowInfoModel) {
        //Retrofit 인스턴스 생성
        val retrofit = RetrofitClient.getInstnace(API_.BASE_URL)
        val service = retrofit.create(RetrofitInterface::class.java) // 레트로핏 인터페이스 객체 구현

        val call: Call<String>? = service.infoUpdate(cow_id, milkCowInfoModel)
        call!!.enqueue(object : Callback<String?> {
            override fun onResponse(call: Call<String?>?, response: Response<String?>) {
                Log.d(Constants.TAG, "InfoUpdate onResponse")
                if (response.isSuccessful()) {
                    Log.e(Constants.TAG, "InfoUpdate onResponse success")
//                        val result: UserList? = response.body()

                    // 서버에서 응답받은 데이터
                    val result = response.body()
                    Log.d(TAG, "$result")
                    event.value = "success"

                } else {
                    // 서버통신 실패
                    event.value = "fail1"
                    Log.e(Constants.TAG, "onResponse fail")
                }
            }
            override fun onFailure(call: Call<String?>?, t: Throwable) {
                // 통신 실패
                event.value = "fail2"
                Log.e(Constants.TAG, "onFailure: " + t.message)
            }
        })
    }

}