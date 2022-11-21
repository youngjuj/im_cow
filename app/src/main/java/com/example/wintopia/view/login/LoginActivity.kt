package com.example.wintopia.view.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.wintopia.R
import com.example.wintopia.databinding.ActivityLoginBinding
import com.example.wintopia.view.main.MainActivity
import com.example.wintopia.view.retrofit.RetrofitClient
import com.example.wintopia.view.signup.SignUpActivity
import com.example.wintopia.view.utils.API_.BASE_URL
import com.example.wintopia.view.utils.Constants.TAG
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding
    val viewModel: LoginViewModel by viewModels()

//    // retrofit
//    lateinit var retrofit : Retrofit
//    lateinit var myAPI : RetrofitService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_login)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.vm = viewModel
        binding.lifecycleOwner = this

//        observeData()

        // 로그인을 위한 예비 아이디 비번
//        val userId = "test"
//        val userPw = "1234"
        var userId = ""
        var userPw = ""

        binding.btnLoginLgin.setOnClickListener(){
            //Retrofit 인스턴스 생성
            val retrofit = RetrofitClient.getInstnace(BASE_URL)
            val service = retrofit.create(RetrofitInterface::class.java) // 레트로핏 인터페이스 객체 구현

            // 서버 연결, id, pw 보내기
            val id = binding.etLoginId.text.toString()
            val pw = binding.etLoginPw.text.toString()

            val call: Call<UserList?>? = service.getName(id, pw)
            call!!.enqueue(object : Callback<UserList?> {
                override fun onResponse(call: Call<UserList?>?, response: Response<UserList?>) {
                    Log.d(TAG, "onResponse")
                    if (response.isSuccessful()) {
                        Log.e(TAG, "onResponse success")
                        val result: UserList? = response.body()

                        // 서버에서 응답받은 데이터를 TextView에 넣어준다.
                        binding.edt.text = "${response.body()?.geId()}"
                        userId = "${response.body()?.geId()}"
                        userPw = "${response.body()?.getPw()}"

                        // id, pw가 서버에서 받아온 userId, userPw와 같다면
                        if (id.equals(userId) && pw.equals(userPw)) {
                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            startActivity(intent)
                        } else {
                            // 로그인 실패
                            Toast.makeText(this@LoginActivity, "ID 혹은 비밀번호를 확인해 주세요11.", Toast.LENGTH_SHORT).show()
                        }

                    } else {
                        // 서버통신 실패
                        Log.e(TAG, "onResponse fail")
                        Toast.makeText(this@LoginActivity, "로그인 통신상태가 불량입니다.", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<UserList?>?, t: Throwable) {
                    // 통신 실패
                    Log.e(TAG, "onFailure: " + t.message)
                }
            })


            // 로그인 성공
//            if (id.equals(id) && pw.equals(pw)) {
//                val intent = Intent(this, MainActivity::class.java)
//                startActivity(intent)
//            } else {
//                // 로그인 실패
//                Toast.makeText(this, "ID 혹은 비밀번호를 확인해 주세요.", Toast.LENGTH_SHORT).show()
//            }
        }





        // 회원가입
        binding.btnLoginJoin.setOnClickListener(){
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    fun observeData() {
        viewModel.id.observe(this){
            binding.edt.text = it
        }
    }
}

