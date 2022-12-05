package com.example.wintopia.view.regist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.example.wintopia.R
import com.example.wintopia.databinding.ActivityRegistInfoBinding

class RegistInfoActivity : AppCompatActivity() {

    lateinit var binding: ActivityRegistInfoBinding
    val viewModel: RegistViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_regist_info)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_regist_info)
    }
}