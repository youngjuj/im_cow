package com.example.wintopia.view.mypage

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.example.wintopia.R
import com.example.wintopia.data.UserList
import com.example.wintopia.databinding.FragmentMyPageBinding
import com.example.wintopia.view.login.LoginActivity
import com.example.wintopia.view.login.LoginViewModel
import com.example.wintopia.view.main.MainActivity
import com.example.wintopia.view.utilssd.Constants.TAG

class MyPageFragment : Fragment() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    lateinit var binding: FragmentMyPageBinding
    val viewModel: MyPageViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_page, container, false)
        // Inflate the layout for this fragment

        val pref = context?.getSharedPreferences("userId", 0)
        val edit = pref?.edit() // 수정모드
        val savedCheckBox = pref?.getBoolean("checkBox", false)

        val user_id = UserList().getId().toString()
        Log.d(TAG, "$user_id")
        viewModel.userPageInfo("test")
//        myPageEvent()

        binding.btnMyPageLogout.setOnClickListener(){
            edit?.remove("userId")
            edit?.clear()
            edit?.commit()
            Log.d(TAG, "MyPage check 버튼클릭")

            val intent = Intent(requireActivity(), LoginActivity::class.java)
            startActivity(intent)
        }



        return binding.root
    }

    fun myPageEvent() {
        viewModel.myPageCountEvent.observe(requireActivity()){
            when(it){
                "success" ->{
                    Toast.makeText(context, "마이페이지에 오신걸 환영합니다..", Toast.LENGTH_SHORT).show()
                    val intent = Intent(context, MainActivity::class.java)
                    startActivity(intent)
                }
                "fail" -> {
                    Toast.makeText(context, "아이디를 확인해주세요.", Toast.LENGTH_SHORT).show()
                }
                "fail1" ->{
                    Toast.makeText(context, "통신상태가 불량입니다.", Toast.LENGTH_SHORT).show()
                }
                "fail2" ->{
                    Toast.makeText(context, "통신 실패", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


}