package com.example.wintopia.view.info

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.wintopia.R
import com.example.wintopia.databinding.ActivityInfoBinding
import com.example.wintopia.retrofit.RetrofitClient
import com.example.wintopia.retrofit.RetrofitInterface
import com.example.wintopia.view.edit.CowInfo
import com.example.wintopia.view.edit.EditActivity
import com.example.wintopia.view.edit.EditViewModel
import com.example.wintopia.view.edit.MilkCowInfoModel
import com.example.wintopia.view.list.ListVOAdapter

import com.example.wintopia.view.utilssd.Constants.TAG
import com.example.wintopia.view.main.MainActivity
import com.example.wintopia.view.utilssd.API_
import com.example.wintopia.view.utilssd.Constants
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


class InfoActivity : AppCompatActivity() {

    // 데이터 바인딩(1)
    lateinit var binding: ActivityInfoBinding
    val viewModel: EditViewModel by viewModels()
    lateinit var cowInfo: CowInfo


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_info)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_info)
        binding.vm = viewModel
        binding.lifecycleOwner = this


        setIntent(intent)
        observeData()


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
            intent.putExtra("cowInfo", cowInfo)
            startActivity(intent)
            finish()
        }

        // 삭제하기 버튼
        binding.btnInfoDelete.setOnClickListener {
            Log.d(TAG, " 삭제하기 버튼 클릭")
            // 전체 리스트 페이지 이동
//            val intent = Intent(this, MainActivity::class.java)
//            startActivity(intent)
//            finish()

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
        if(intent.getStringExtra("where").equals("list")) {
            cowInfo = intent.getSerializableExtra("infos") as CowInfo
            binding.tvInfoName.text = (cowInfo?.name.toString())
            binding.tvInfoBirth.text = (cowInfo?.birth.toString())
            binding.tvInfoId.text = (cowInfo?.id.toString())
            binding.tvInfoGender.text = (cowInfo?.gender.toString())
            binding.tvInfoVaccine.text = (cowInfo?.vaccine.toString())
            binding.tvInfoKind.text = (cowInfo?.kind.toString())

        } else if(intent.getStringExtra("where").equals("edit")) {
            cowInfo = intent.getSerializableExtra("TEXT") as CowInfo
            binding.tvInfoName.text = (cowInfo?.name.toString())
            binding.tvInfoBirth.text = (cowInfo?.birth.toString())
            binding.tvInfoId.text = (cowInfo?.id.toString())
            binding.tvInfoGender.text = (cowInfo?.gender.toString())
            binding.tvInfoVaccine.text = (cowInfo?.vaccine.toString())
            binding.tvInfoKind.text = (cowInfo?.kind.toString())
        }
    }

//    참고용
    fun observeData() {
        viewModel.id.observe(this){
            binding.tvInfoId.text = it
        }
        viewModel.name.observe(this){
        binding.tvInfoName.text = it
        }
        viewModel.birth.observe(this){
            binding.tvInfoBirth.text = it
        }
        viewModel.gender.observe(this){
            binding.tvInfoGender.text = it
        }
        viewModel.vaccine.observe(this){
            binding.tvInfoVaccine.text = it
        }
        viewModel.kind.observe(this){
            binding.tvInfoKind.text = it
        }
    }

}