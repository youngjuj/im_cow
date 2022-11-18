package com.example.wintopia.view.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.example.wintopia.R
import com.example.wintopia.databinding.ActivityMainBinding
import com.example.wintopia.view.camera.CameraFragment
import com.example.wintopia.view.camera.RegistViewModel
import com.example.wintopia.view.list.ListFragment
import com.example.wintopia.view.mypage.MyPageFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this

        supportFragmentManager.beginTransaction().replace(
            R.id.fl, ListFragment()
        ).commit()
        binding.bnv.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.tab1 -> {
                    Toast.makeText(this, "List화면", Toast.LENGTH_SHORT).show()

                    supportFragmentManager.beginTransaction().replace(
                        R.id.fl, ListFragment()
                    ).commit()
                }
                R.id.tab2 -> {
                    Toast.makeText(this, "카메라 화면", Toast.LENGTH_SHORT).show()

                    supportFragmentManager.beginTransaction().replace(
                        R.id.fl, CameraFragment()
                    ).commit()
                }
                R.id.tab3 -> {
                    Toast.makeText(this, "내농장 화면", Toast.LENGTH_SHORT).show()

                    supportFragmentManager.beginTransaction().replace(
                        R.id.fl, MyPageFragment()
                    ).commit()
                }
            }


            true
        }

    }
}