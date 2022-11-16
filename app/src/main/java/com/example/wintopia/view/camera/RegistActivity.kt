package com.example.wintopia.view.camera

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.example.wintopia.R
import com.example.wintopia.databinding.ActivityRegistBinding

class RegistActivity : AppCompatActivity() {

    lateinit var binding: ActivityRegistBinding
    val viewModel: RegistViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_regist)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_regist)
        binding.vm = viewModel
        binding.lifecycleOwner = this

        observeData()

        binding.btnRegistCancel.setOnClickListener {  }

        binding.btnRegistRegist.setOnClickListener {  }



    }
    fun observeData() {

    }
}