package com.example.wintopia.view.mypage

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.wintopia.data.MyPageInfo
import com.example.wintopia.retrofit.RetrofitClient
import com.example.wintopia.retrofit.RetrofitInterface
import com.example.wintopia.view.utilssd.API_
import com.example.wintopia.view.utilssd.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyPageViewModel: ViewModel() {

    val myfarm = MutableLiveData<String>()
    val myPhone = MutableLiveData<String>()
    val myCowCount = MutableLiveData<String>()
    val cowBaby = MutableLiveData<String>()
    val myCowBull = MutableLiveData<String>()

    val myPageCountEvent = MutableLiveData<String>()

    fun userPageInfo(user_id: String){
        //Retrofit 인스턴스 생성
        val retrofit = RetrofitClient.getInstnace(API_.BASE_URL)
        val service = retrofit.create(RetrofitInterface::class.java) // 레트로핏 인터페이스 객체 구현

        val call: Call<MyPageInfo>? = service.mypageInfo(user_id)
        call!!.enqueue(object : Callback<MyPageInfo?> {
            override fun onResponse(call: Call<MyPageInfo?>?, response: Response<MyPageInfo?>) {
                if (response.isSuccessful()) {
                    myPageCountEvent.value = "success"
                    // 서버에서 응답받은 데이터
                    val result = "${response.body()}"
                    myfarm.value = response.body()?.getFarmName().toString()
                    myPhone.value = response.body()?.getPhone().toString()
                    myCowCount.value = response.body()?.getTotalCow().toString()
                    cowBaby.value = response.body()?.getBaby().toString()
                    myCowBull.value = "${response.body()?.getCow().toString()}" + "/" + "${response.body()?.getBull().toString()}"
                } else {
                    // 서버통신 실패
                    myPageCountEvent.value = "fail1"
                }
            }
            override fun onFailure(call: Call<MyPageInfo?>?, t: Throwable) {
                // 통신 실패
                myPageCountEvent.value = "fail2"
            }
        })
    }






}