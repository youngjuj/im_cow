package com.example.wintopia.view.info

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.wintopia.R
import com.example.wintopia.databinding.ActivityInfoBinding

class InfoActivity : AppCompatActivity() {

    // 데이터 바인딩(1)
    lateinit var binding: ActivityInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        // 데이터 바인딩(2)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_info)

        // 즐겨찾기 별
        var switch: Int = 0
        binding.imgInfoStar.setOnClickListener {
            if (switch == 0) {
                binding.imgInfoStar.setImageResource(R.drawable.filledstar)
                switch = 1
            } else {
                binding.imgInfoStar.setImageResource(R.drawable.star)
                switch = 0
            }

            // 뒤로가기 버튼
            binding.imgInfoBack.setOnClickListener {
                finish()
            }

            // 수정하기 버튼
            binding.btnInfoEdit.setOnClickListener {

                //수정하기 페이지로 이동
                val intent = Intent(this, EditActivity::class.java)
                startActivity(intent)
            }

            // 삭제하기 버튼
            binding.btnInfoDelete.setOnClickListener {
                Toast.makeText(this, "삭제하기", Toast.LENGTH_SHORT).show()
            }
        }
    }}