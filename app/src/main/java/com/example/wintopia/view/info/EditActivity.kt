package com.example.wintopia.view.info

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.example.wintopia.R
import com.example.wintopia.databinding.ActivityEditBinding
import com.example.wintopia.view.utils.Constants

class EditActivity : AppCompatActivity() {

    // 데이터 바인딩(1)
    lateinit var binding: ActivityEditBinding
    val viewModel: EditViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_edit)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit)
        binding.vm = viewModel
        binding.lifecycleOwner = this


        // 데이터 바인딩(2)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit)

        // 수정하기 버튼
        binding.btnEditEdit.setOnClickListener {
            Log.d(Constants.TAG, " 수정완료 버튼 클릭")
            // 각 텍스트 가져오기
            val etEditName = binding.etEditName.text.toString()
            val etEditId = binding.etEditId.text.toString()
            val etEditBirth = binding.etEditBirth.text.toString()
            val etEditStatus = binding.etEditStatus.text.toString()
            val etEditTel = binding.etEditTel.text.toString()
            val etEditFax = binding.etEditFax.text.toString()

            // 수정 후 상세정보페이지 이동
            val intent1 = Intent(this, InfoActivity::class.java)
            startActivity(intent1)
            finish()

            Toast.makeText(this, "수정하기", Toast.LENGTH_SHORT).show()
        }

        // 취소버튼
        binding.btnEditCancel.setOnClickListener {
            Log.d(Constants.TAG, " 취소하기 버튼 클릭")
            // 이전 상세페이지로 이동
            val intent2 = Intent(this, InfoActivity::class.java)
            startActivity(intent2)
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