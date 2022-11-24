package com.example.wintopia.view.mypage

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.wintopia.R
import com.example.wintopia.databinding.FragmentMyPageBinding
import com.example.wintopia.view.login.LoginActivity
import com.example.wintopia.view.utilssd.Constants.TAG

class MyPageFragment : Fragment() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    lateinit var binding: FragmentMyPageBinding
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
}