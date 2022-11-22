package com.example.wintopia.view.login

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.wintopia.data.UserList
import com.example.wintopia.retrofit.RetrofitClient
import com.example.wintopia.retrofit.RetrofitInterface
import com.example.wintopia.view.main.MainActivity
import com.example.wintopia.view.utilssd.API_
import com.example.wintopia.view.utilssd.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel: ViewModel() {

    // Create a LiveData with a String
    val id = MutableLiveData<String>()
    val pw = MutableLiveData<String>()
    val event = MutableLiveData<String>()

    fun login(id:String, pw:String){
        //Retrofit 인스턴스 생성
        val retrofit = RetrofitClient.getInstnace(API_.BASE_URL)
        val service = retrofit.create(RetrofitInterface::class.java) // 레트로핏 인터페이스 객체 구현

        val call: Call<UserList?>? = service.getName(id, pw)
        call!!.enqueue(object : Callback<UserList?> {
            override fun onResponse(call: Call<UserList?>?, response: Response<UserList?>) {
                Log.d(Constants.TAG, "onResponse")
                if (response.isSuccessful()) {
                    Log.e(Constants.TAG, "onResponse success")
//                        val result: UserList? = response.body()

                    // 서버에서 응답받은 데이터
                    val userId = "${response.body()?.geId()}"
                    val userPw = "${response.body()?.getPw()}"

                    // id, pw가 서버에서 받아온 userId, userPw와 같다면
                    if (id.equals(userId) and pw.equals(userPw)) {
                        event.value = "success"
                    } else {
                        // 로그인 실패
                        event.value = "fail"
                    }
                } else {
                    // 서버통신 실패
                    event.value = "fail1"
                    Log.e(Constants.TAG, "onResponse fail")
                }
            }
            override fun onFailure(call: Call<UserList?>?, t: Throwable) {
                // 통신 실패
                event.value = "fail2"
                Log.e(Constants.TAG, "onFailure: " + t.message)
            }
        })
    }

}