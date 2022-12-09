package com.example.wintopia.view.signup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
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
    lateinit var id: String
    lateinit var pw: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up)
        binding.signUpvm = viewModel
        binding.lifecycleOwner = this

        // 회원가입 누를 경우
        signUpButtonClick()
        signUpEvent()

        // 로그인페이지로 돌아가기
        signUpBackButtonClick()

        // pw 텍스트가 변경이 되었을때
        binding.etJoinPw2.onMyTextChanged {
            // 하나라도 입력된 글자가 있다면
            if (it.toString().count() > 0){
                binding.svSignUpRoot.scrollTo(0, 500)
            }
        }
    }


    fun signUpEvent() {

        observeData()

        viewModel.event.observe(this){
            when(it){
                "success" ->{
                    Toast.makeText(this, "${id}님 환영합니다.", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@SignUpActivity, MainActivity::class.java)
                    startActivity(intent)
                }
                "fail" -> {
                    Toast.makeText(this@SignUpActivity, "사용중인 ID입니다.", Toast.LENGTH_SHORT).show()
                }
                "fail1" ->{
                    Toast.makeText(this@SignUpActivity, "회원가입 통신상태가 불량입니다.", Toast.LENGTH_SHORT).show()
                }
                "fail2" ->{
                    Toast.makeText(this@SignUpActivity, "회원가입 통신 실패", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun signUpButtonClick() {
        binding.btnJoinJoin.setOnClickListener(){
            // 서버 연결, id, pw 보내기
            id = binding.etJoinId.text.toString()
            pw = binding.etJoinPw2.text.toString()

            viewModel.signUp(id, pw)
        }
    }

    fun signUpBackButtonClick(){
        binding.btnJoinback.setOnClickListener(){
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    fun observeData() {
        viewModel.signUpPw2.observe(this){
            if (viewModel.signUpPw1.toString().equals(it)){
                binding.etJoinPw2Layout.helperText = ""
            }else{
                binding.etJoinPw2Layout.helperText = "비밀번호를 확인해주세요"
            }

        }
    }
}