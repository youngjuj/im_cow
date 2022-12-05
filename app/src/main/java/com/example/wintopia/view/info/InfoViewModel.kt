package com.example.wintopia.view.info

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.wintopia.retrofit.RetrofitClient
import com.example.wintopia.retrofit.RetrofitInterface
import com.example.wintopia.view.edit.MilkCowInfoModel
import com.example.wintopia.view.utilssd.API_
import com.example.wintopia.view.utilssd.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class InfoViewModel: ViewModel() {

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
    val image = MutableLiveData<String>()
    val wishEvent = MutableLiveData<String>()
    val deleteEvent = MutableLiveData<String>()
    val getEvent = MutableLiveData<String>()
    var resCowinfo: MilkCowInfoModel? = null


    fun cowWish(cow_id: String){
        //Retrofit 인스턴스 생성
        val retrofit = RetrofitClient.getInstnace(API_.BASE_URL)
        val service = retrofit.create(RetrofitInterface::class.java) // 레트로핏 인터페이스 객체 구현

        val call: Call<String>? = service.cowWish(cow_id)
        call!!.enqueue(object : Callback<String?> {
            override fun onResponse(call: Call<String?>?, response: Response<String?>) {
                Log.d(Constants.TAG, "onResponse")
                if (response.isSuccessful()) {
                    Log.e(Constants.TAG, "onResponse success")
//                        val result: UserList? = response.body()

                    // 서버에서 응답받은 데이터
                    val result = "${response.body()}"
                    Log.d(ContentValues.TAG, "$result")
                    if ((result.toInt()) == 0){
                        wishEvent.value = "0"
                    }else if ((result.toInt()) == 1){
                        wishEvent.value = "1"
                    }


                } else {
                    // 서버통신 실패
                    wishEvent.value = "fail1"
                    Log.e(Constants.TAG, "onResponse fail")
                }
            }
            override fun onFailure(call: Call<String?>?, t: Throwable) {
                // 통신 실패
                wishEvent.value = "fail2"
                Log.e(Constants.TAG, "onFailure: " + t.message)
            }
        })
    }




    fun cowInfoDelete(cow_id: String){
        //Retrofit 인스턴스 생성
        val retrofit = RetrofitClient.getInstnace(API_.BASE_URL)
        val service = retrofit.create(RetrofitInterface::class.java) // 레트로핏 인터페이스 객체 구현

        val call: Call<String>? = service.cowInfoDelete(cow_id)
        call!!.enqueue(object : Callback<String?> {
            override fun onResponse(call: Call<String?>?, response: Response<String?>) {
                Log.d(Constants.TAG, "CowInfoDelete onResponse")
                if (response.isSuccessful()) {
                    Log.e(Constants.TAG, "CowInfoDelete onResponse success")
//                        val result: UserList? = response.body()

                    // 서버에서 응답받은 데이터
                    val result = "${response.body()}"
                    Log.d(ContentValues.TAG, "$result")
                    deleteEvent.value = "success"

                } else {
                    // 서버통신 실패
                    deleteEvent.value = "fail1"
                    Log.e(Constants.TAG, "onResponse fail")
                }
            }
            override fun onFailure(call: Call<String?>?, t: Throwable) {
                // 통신 실패
                deleteEvent.value = "fail2"
                Log.e(Constants.TAG, "onFailure: " + t.message)
            }
        })
    }

    fun cowInfoOne(cow_id: String): MilkCowInfoModel?{
        //Retrofit 인스턴스 생성
        val retrofit = RetrofitClient.getInstnace(API_.BASE_URL)
        val service = retrofit.create(RetrofitInterface::class.java) // 레트로핏 인터페이스 객체 구현
//        var cowInfo: MilkCowInfoModel? = null
        val call: Call<MilkCowInfoModel> = service.getData(cow_id)
        call!!.enqueue(object : Callback<MilkCowInfoModel> {
            override fun onResponse(call: Call<MilkCowInfoModel>, response: Response<MilkCowInfoModel>) {
                Log.d("여기는?", "cowInfoOne onResponse")
                if (response.isSuccessful()) {
                    Log.e(Constants.TAG, "cowInfoOne onResponse success")
                    resCowinfo = response.body()
//                        val result: UserList? = response.body()

                    // 서버에서 응답받은 데이터
                    val result = "${response.body()}"
                    Log.d("확인좀..", "${resCowinfo!!.birth}")
                    getEvent.value = "success"


                } else {
                    // 서버통신 실패
                    getEvent.value = "fail1"
                    Log.e(Constants.TAG, "onResponse fail")
                }
            }
            override fun onFailure(call: Call<MilkCowInfoModel>, t: Throwable) {
                // 통신 실패
                getEvent.value = "fail2"
                Log.e(Constants.TAG, "onFailure: " + t.message)
            }
        })

        return resCowinfo as MilkCowInfoModel?
    }




}