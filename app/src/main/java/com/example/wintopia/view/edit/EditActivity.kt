package com.example.wintopia.view.edit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.example.wintopia.R
import com.example.wintopia.databinding.ActivityEditBinding
import com.example.wintopia.view.info.InfoActivity
import com.example.wintopia.view.utilssd.Constants
import com.google.gson.Gson
import com.google.gson.JsonParser
import org.json.JSONObject

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

        setIntent(intent)


        // 수정하기 버튼
        binding.btnEditEdit.setOnClickListener {
            getText()
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

    fun getText(){
        // 각 텍스트 가져오기
        var etEditName = binding.etEditName.text.toString()
        var etEditId = binding.etEditId.text.toString()
        var etEditBirth = Integer.parseInt(binding.etEditBirth.text.toString())
        var etEditGender = binding.etEditGender.text.toString()

        var milkCowInfoModel = MilkCowInfoModel(etEditName,
            etEditId,etEditBirth,etEditGender)

        Log.d(Constants.TAG, " 수정완료 버튼 클릭, ${milkCowInfoModel}")

//        viewModel.infoOut(jsonString.toString())
        viewModel.infoOut(milkCowInfoModel)

        // 수정 후 상세정보페이지 이동
        val cowInfo = CowInfo(etEditName,
            etEditId,etEditBirth,etEditGender, )
        val intent = Intent(this, InfoActivity::class.java)
        intent.putExtra("where", "edit")
        intent.putExtra("TEXT", cowInfo)
        startActivity(intent)
        finish()

        Toast.makeText(this, "수정하기", Toast.LENGTH_SHORT).show()

    }

    override fun setIntent(intent: Intent) {
            val intent = intent
            val cowInfo = intent.getSerializableExtra("cowInfo") as CowInfo?
            binding.etEditName.hint = (cowInfo?.name.toString())
            binding.etEditBirth.hint = (cowInfo?.birth.toString())
            binding.etEditId.hint = (cowInfo?.id.toString())
            binding.etEditGender.hint = (cowInfo?.gender.toString())

    }
}