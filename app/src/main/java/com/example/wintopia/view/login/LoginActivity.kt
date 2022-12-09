package com.example.wintopia.view.login

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.wintopia.R
import com.example.wintopia.data.UserList
import com.example.wintopia.databinding.ActivityLoginBinding
import com.example.wintopia.view.main.MainActivity
import com.example.wintopia.retrofit.RetrofitClient
import com.example.wintopia.retrofit.RetrofitInterface
import com.example.wintopia.view.signup.SignUpActivity
import com.example.wintopia.view.utilssd.API_.BASE_URL
import com.example.wintopia.view.utilssd.Constants.TAG
import com.example.wintopia.view.utilssd.onMyTextChanged
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class
LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding
    val viewModel: LoginViewModel by viewModels()
    lateinit var id: String
    lateinit var pw: String
    var checkBox: Boolean = false

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.vm = viewModel
        binding.lifecycleOwner = this

        // 자동로그인
        autoLogin()

        // 로그인 버튼 눌렀을 때
        loginButtonClick()
        loginEvent()

        // pw 텍스트가 변경이 되었을때 스크롤
        autoScroll()

        // 회원가입
        binding.btnLoginJoin.setOnClickListener(){
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }


    fun loginButtonClick() {
        binding.btnLoginLgin.setOnClickListener(){
            id = binding.etLoginId.text.toString()
            pw = binding.etLoginPw.text.toString()
            checkBox = binding.checkbox.isChecked

            viewModel.login(id, pw)
        }
    }

    fun autoLogin(){
        val pref = getSharedPreferences("userId", 0)

        //1번째는 데이터 키 값이고 2번째는 키 값에 데이터가 존재하지않을때 대체 값 입니다.
        val savedId = pref.getString("userId", "").toString()
        val savedPw = pref.getString("userPw", "").toString()
        val savedChceckBox = pref.getBoolean("checkBox", false)
        if (savedChceckBox != true){

        } else {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            Toast.makeText(this, "${savedId}님 반갑습니다.", Toast.LENGTH_SHORT).show()
            finish()
        }
    }


    // 자동로그인을 위한 아이디 저장 함수
    fun saveData (userId : String, userPw: String, checkBox: Boolean){
        // shared key 설정
        val pref = getSharedPreferences("userId", MODE_PRIVATE)
        val edit = pref.edit() // 수정모드
        // 값 넣기
        edit.putString("userId", userId)
        edit.putString("userPw", userPw)
        edit.putBoolean("checkBox", checkBox)
        edit.apply() //적용하기
    }

    fun loginEvent() {

        viewModel.event.observe(this){
            when(it){
                "success" ->{
                    saveData(id, pw, checkBox)
                    Toast.makeText(this, "${id}님 환영합니다.", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
                "fail" -> {
                    Toast.makeText(this, "ID 혹은 비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show()
                }
                "fail1" ->{
                    Toast.makeText(this, "로그인 통신상태가 불량입니다.", Toast.LENGTH_SHORT).show()
                }
                "fail2" ->{
                    Toast.makeText(this, "로그인 통신 실패", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun autoScroll(){
        binding.etLoginPw.onMyTextChanged {
            // 하나라도 입력된 글자가 있다면
            if (it.toString().count() > 0){
                binding.svLoginRoot.scrollTo(0, 500)
            }
        }
    }

//    참고용
//    fun observeData() {
//        viewModel.id.observe(this){
//            binding.edt.text = it
//        }
//    }
}

