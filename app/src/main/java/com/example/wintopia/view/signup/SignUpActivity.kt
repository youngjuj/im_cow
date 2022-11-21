package com.example.wintopia.view.signup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.example.wintopia.R
import com.example.wintopia.databinding.ActivitySignUpBinding
import com.example.wintopia.view.login.LoginActivity
import com.example.wintopia.data.UserList
import com.example.wintopia.view.main.MainActivity
import com.example.wintopia.retrofit.RetrofitClient
import com.example.wintopia.retrofit.RetrofitInterface
import com.example.wintopia.view.utilssd.API_
import com.example.wintopia.view.utilssd.Constants
import com.example.wintopia.view.utilssd.onMyTextChanged
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpActivity : AppCompatActivity() {

    lateinit var binding: ActivitySignUpBinding
    val viewModel: SignUpViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_sign_up)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up)
        binding.signUpvm = viewModel
        binding.lifecycleOwner = this

        // pw 텍스트가 변경이 되었을때
        binding.etJoinPw2.onMyTextChanged {
            // 하나라도 입력된 글자가 있다면
            if (it.toString().count() > 0){
                binding.svSignUpRoot.scrollTo(0, 500)
            }
        }

        // signUpPw2를 관찰해서
        viewModel.signUpPw2.observe(this){
            binding.edt.text = it
            // helper text 변경
            if (it == binding.etJoinPw1.toString()){
                binding.etJoinPw2Layout.helperText = ""
            }else{
                binding.etJoinPw2Layout.helperText = "비밀번호를 확인해주세요"
            }
        }

        // 회원 가입을 위한 서버에서 받아온 값
        var userId = ""
        var userPw = ""
        // 회원가입 누를 경우
        binding.btnJoinJoin.setOnClickListener(){

            //Retrofit 인스턴스 생성
            val retrofit = RetrofitClient.getInstnace(API_.BASE_URL)
            val service = retrofit.create(RetrofitInterface::class.java) // 레트로핏 인터페이스 객체 구현

            // 서버 연결, id, pw 보내기
            val id = binding.etJoinId.text.toString()
            val pw = binding.etJoinPw2.text.toString()

            val call: Call<UserList?>? = service.getSignUp(id, pw)
            call!!.enqueue(object : Callback<UserList?> {
                override fun onResponse(call: Call<UserList?>?, response: Response<UserList?>) {
                    Log.d(Constants.TAG, "SignUp onResponse")
                    if (response.isSuccessful()) {
                        Log.e(Constants.TAG, "SignUp onResponse success")
                        val result: UserList? = response.body()

                        // 서버에서 응답받은 데이터를 TextView에 넣어준다.
                        binding.edt.text = "${response.body()?.geId()}"
                        userId = "${response.body()?.geId()}"
                        userPw = "${response.body()?.getPw()}"

                        // id, pw가 서버에서 받아온 userId, userPw와 같다면
                        if (userId.equals(id)) {
                            Toast.makeText(this@SignUpActivity, "${userId}님 환영합니다.", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@SignUpActivity, MainActivity::class.java)
                            startActivity(intent)
                        } else {
                            // 회원가입 실패
                            Toast.makeText(this@SignUpActivity, "사용중인 ID입니다.", Toast.LENGTH_SHORT).show()
                        }


                    } else {
                        // 서버통신 실패
                        Log.e(Constants.TAG, "SignUp onResponse fail")
                        Toast.makeText(this@SignUpActivity, "회원가입 통신상태가 불량입니다.", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<UserList?>?, t: Throwable) {
                    // 통신 실패
                    Log.e(Constants.TAG, "SignUp onFailure: " + t.message)
                }
            })

        }

        // 로그인페이지로 돌아가기
        binding.btnJoinback.setOnClickListener(){
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

}