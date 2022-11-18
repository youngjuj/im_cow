package com.example.wintopia.view.info

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.wintopia.R
import com.example.wintopia.databinding.ActivityEditBinding

class EditActivity : AppCompatActivity() {

    // 데이터 바인딩(1)
    lateinit var binding: ActivityEditBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        // 데이터 바인딩(2)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit)

        // 수정하기 버튼
        binding.btnEditEdit.setOnClickListener {
            Toast.makeText(this, "수정하기", Toast.LENGTH_SHORT).show()
        }

        // 취소버튼
        binding.btnEditCancel.setOnClickListener {
            finish()
        }

        // 즐겨찾기 별
        var switch: Int = 0
        binding.imgEditStar.setOnClickListener {
            if (switch == 0)
            {binding.imgEditStar.setImageResource(R.drawable.filledstar)
            switch = 1}
            else {binding.imgEditStar.setImageResource(R.drawable.star)
            switch = 0}
        }
    }
}