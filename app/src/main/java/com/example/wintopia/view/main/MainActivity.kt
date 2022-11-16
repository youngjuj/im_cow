package com.example.wintopia.view.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.Toast
import com.example.wintopia.R
import com.example.wintopia.databinding.ActivityMainBinding
import com.example.wintopia.view.camera.CameraFragment
import com.example.wintopia.view.list.ListFragment
import com.example.wintopia.view.mypage.MyPageFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private var mBinding: ActivityMainBinding? = null
    private val binding get() = mBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mBinding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
        supportFragmentManager.beginTransaction().replace(
            R.id.fl, ListFragment()
        ).commit()
        binding.bnv.setOnItemReselectedListener { item ->
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
        fun onDestroy() {
            mBinding = null
            super.onDestroy()
        }
    }
}