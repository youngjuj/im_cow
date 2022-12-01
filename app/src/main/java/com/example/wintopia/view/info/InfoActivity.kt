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
    var switch: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_info)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_info)
        binding.vm = viewModel
        binding.lifecycleOwner = this

        // webView에 띄울 이미지 관련 설정들
        binding.wvInfoPhto.settings.useWideViewPort = true
        binding.wvInfoPhto.settings.loadWithOverviewMode = true
        binding.wvInfoPhto.settings.builtInZoomControls = true
        binding.wvInfoPhto.settings.setSupportZoom(true)

        setIntent(intent)
        observeData()


        // 즐겨찾기 별
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
            binding.wvInfoPhto.loadUrl("${API_.BASE_URL}image/cowImgOut?cow_id=${cowInfo?.id.toString()}")
            binding.tvInfoName.text = (cowInfo?.name.toString())
            binding.tvInfoBirth.text = (cowInfo?.birth.toString())
            binding.tvInfoId.text = (cowInfo?.id.toString())
            Log.d("로그ㅜ", "${cowInfo?.milk.toString()}")
            if (cowInfo?.gender.toString().equals("수컷")) {
                binding.rbInfoMale.isChecked = true
            } else if(cowInfo?.gender.toString().equals("암컷")) {
                binding.rbInfoFemale.isChecked = true
            }
            if (cowInfo?.vaccine.toString().equals("접종")) {
                binding.rbInfoDid.isChecked = true
            } else if (cowInfo?.vaccine.toString().equals("미접종")) {
                binding.rbInfoDidnt.isChecked = true
            }
            if (cowInfo?.pregnancy.toString().equals("유")) {
                binding.rbInfoPreg.isChecked = true
            } else if (cowInfo?.pregnancy.toString().equals("무")){
                binding.rbInfoNonP.isChecked = true
            }
            if (cowInfo?.milk.toString().equals("유")) {
                binding.rbInfoMilkY.isChecked = true
            } else if (cowInfo?.milk.toString().equals("무")){
                binding.rbInfoMilkN.isChecked = true
            }
            if (cowInfo?.castration.toString().equals("유")) {
                binding.rbInfoCasY.isChecked = true
            } else if (cowInfo?.castration.toString().equals("무")){
                binding.rbInfoCasN.isChecked = true
            }
            switch = Integer.parseInt(cowInfo?.list.toString())
            if (switch == 1) {
                binding.imgInfoStar.setImageResource(R.drawable.filledstar)
            }

        } else if(intent.getStringExtra("where").equals("edit")) {
            cowInfo = intent.getSerializableExtra("TEXT") as CowInfo
            binding.wvInfoPhto.loadUrl("${API_.BASE_URL}image/getImages?user_id=test&cow_id=${cowInfo?.id.toString()}")
            binding.tvInfoName.text = (cowInfo?.name.toString())
            binding.tvInfoBirth.text = (cowInfo?.birth.toString())
            binding.tvInfoId.text = (cowInfo?.id.toString())
            if (cowInfo?.gender.toString().equals("수컷")) {
                binding.rbInfoMale.isChecked = true
            } else if(cowInfo?.gender.toString().equals("암컷")) {
                binding.rbInfoFemale.isChecked = true
            }
            if (cowInfo?.vaccine.toString().equals("접종")) {
                binding.rbInfoDid.isChecked = true
            } else if (cowInfo?.vaccine.toString().equals("미접종")) {
                binding.rbInfoDidnt.isChecked = true
            }
            if (cowInfo?.pregnancy.toString().equals("유")) {
                binding.rbInfoPreg.isChecked = true
            } else if (cowInfo?.pregnancy.toString().equals("무")){
                binding.rbInfoNonP.isChecked = true
            }
            if (cowInfo?.milk.toString().equals("유")) {
                binding.rbInfoMilkY.isChecked = true
            } else if (cowInfo?.milk.toString().equals("무")){
                binding.rbInfoMilkN.isChecked = true
            }
            if (cowInfo?.castration.toString().equals("유")) {
                binding.rbInfoCasY.isChecked = true
            } else if (cowInfo?.castration.toString().equals("무")){
                binding.rbInfoCasN.isChecked = true
            }
            switch = Integer.parseInt(cowInfo?.list.toString())
            if (switch == 1) {
                binding.imgInfoStar.setImageResource(R.drawable.filledstar)
            }

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
        viewModel.variety.observe(this){
            binding.tvInfoVariety.text = it
        }
        viewModel.gender.observe(this){
            binding.rbInfoGender.checkedRadioButtonId
        }
        viewModel.vaccine.observe(this){
            binding.rbInfoVaccine.checkedRadioButtonId
        }
        viewModel.pregnancy.observe(this){
            binding.rbInfoPregnant.checkedRadioButtonId
        }
        viewModel.milk.observe(this){
            binding.rbInfoMilk.checkedRadioButtonId
        }
        viewModel.castration.observe(this){
            binding.rbInfoCas.checkedRadioButtonId
        }
    }

}