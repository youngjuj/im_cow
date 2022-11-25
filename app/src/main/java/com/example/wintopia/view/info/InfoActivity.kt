package com.example.wintopia.view.info

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.wintopia.R
import com.example.wintopia.databinding.ActivityInfoBinding
import com.example.wintopia.view.edit.CowInfo
import com.example.wintopia.view.edit.EditActivity
import com.example.wintopia.view.edit.EditViewModel
import com.example.wintopia.view.edit.MilkCowInfoModel

import com.example.wintopia.view.utilssd.Constants.TAG
import com.example.wintopia.view.main.MainActivity



class InfoActivity : AppCompatActivity() {

    // 데이터 바인딩(1)
    lateinit var binding: ActivityInfoBinding
    val viewModel: EditViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_info)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_info)
        binding.vm = viewModel
        binding.lifecycleOwner = this


        setIntent(intent)


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
        }
        // 뒤로가기 버튼
        binding.imgInfoBack.setOnClickListener {
            finish()
        }

        // 수정하기 버튼
        binding.btnInfoEdit.setOnClickListener {
            Log.d(TAG, " 수정하기 버튼 클릭")
            //수정하기 페이지로 이동
            val intent = Intent(this, EditActivity::class.java)
            startActivity(intent)
            finish()
        }

        // 삭제하기 버튼
        binding.btnInfoDelete.setOnClickListener {
            Log.d(TAG, " 삭제하기 버튼 클릭")
            // 전체 리스트 페이지 이동
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()

            Toast.makeText(this, "삭제하기", Toast.LENGTH_SHORT).show()
        }
    }
    fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(fragment.id, fragment)
        fragmentTransaction.commit()
    }

    override fun setIntent(intent: Intent) {
        val intent = intent
        val cowInfo = intent.getSerializableExtra("TEXT") as CowInfo?
        Log.d(TAG, "user 객체: $cowInfo")

        binding.tvInfoName.text = (cowInfo?.name.toString())
        binding.tvInfoBirth.text = (cowInfo?.birth.toString())
        binding.tvInfoId.text = (cowInfo?.id.toString())
        binding.tvInfoGender.text = (cowInfo?.gender.toString())
        binding.tvInfoVaccine.text = (cowInfo?.vaccine.toString())
        binding.tvInfoKind.text = (cowInfo?.kind.toString())

    }

}