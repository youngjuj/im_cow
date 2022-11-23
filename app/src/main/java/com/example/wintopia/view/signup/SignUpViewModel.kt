package com.example.wintopia.view.signup

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
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

class SignUpViewModel: ViewModel() {

    // Create a LiveData with a String
    val signUpId = MutableLiveData<String>()
    val signUpPw1 = MutableLiveData<String>()
    val signUpPw2 = MutableLiveData<String>()
    val event = MutableLiveData<String>()


    fun signUp(id:String, pw:String) {
        //Retrofit 인스턴스 생성
        val retrofit = RetrofitClient.getInstnace(API_.BASE_URL)
        val service = retrofit.create(RetrofitInterface::class.java) // 레트로핏 인터페이스 객체 구현

        // UserList 형태로 값 받아오기
        val call: Call<UserList?>? = service.getSignUp(id, pw)
        call!!.enqueue(object : Callback<UserList?> {
            override fun onResponse(call: Call<UserList?>?, response: Response<UserList?>) {
                Log.d(Constants.TAG, "SignUp onResponse")
                if (response.isSuccessful()) {
                    Log.e(Constants.TAG, "SignUp onResponse success")
//                    val result: UserList? = response.body()

                    // 서버에서 응답받은 데이터를 TextView에 넣어준다.
                    var userId = "${response.body()?.getId()}"
                    var userPw = "${response.body()?.getPw()}"

                    // id, pw가 서버에서 받아온 userId, userPw와 같다면
                    if (userId.equals(id) and userPw.equals(pw)) {
                        event.value = "success"
                    } else {
                        // 회원가입 실패
                        event.value = "fail"
                    }
                } else {
                    // 서버통신 실패
                    event.value = "fail1"
                    Log.e(Constants.TAG, "SignUp onResponse fail")
                }
            }

            override fun onFailure(call: Call<UserList?>?, t: Throwable) {
                // 통신 실패
                event.value = "fail2"
                Log.e(Constants.TAG, "SignUp onFailure: " + t.message)
            }
        })

    }

}