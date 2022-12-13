package com.example.wintopia.view.edit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.wintopia.R
import com.example.wintopia.data.UserList
import com.example.wintopia.databinding.ActivityEditBinding
import com.example.wintopia.view.info.InfoActivity
import com.example.wintopia.view.utilssd.API_
import com.example.wintopia.view.utilssd.Constants
import com.google.gson.Gson
import com.google.gson.JsonParser
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONObject
import java.lang.Exception

class EditActivity : AppCompatActivity() {

    // 데이터 바인딩(1)
    lateinit var binding: ActivityEditBinding
    val viewModel: EditViewModel by viewModels()
    var switch: Int = 0
    var userNum: Int = 0
    var cowInfo: MilkCowInfoModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = ContextCompat.getColor(this, R.color.grey)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit)
        binding.vm = viewModel
        binding.lifecycleOwner = this

        setIntent(intent)
        observeData()

        // 수정하기 버튼
        binding.btnEditEdit.setOnClickListener {
        if (binding.rbEditMale.isChecked) viewModel.gender.value = "수컷"
        else viewModel.gender.value = "암컷"

        if (binding.rbEditDid.isChecked) viewModel.vaccine.value = "접종"
        else viewModel.vaccine.value = "미접종"

        if (binding.rbEditPreg.isChecked) viewModel.pregnancy.value = "유"
        else viewModel.pregnancy.value = "무"

        if (binding.rbEditMilkY.isChecked) viewModel.milk.value = "유"
        else viewModel.milk.value = "무"

        if (binding.rbEditCasY.isChecked) viewModel.castration.value = "유"
        else viewModel.castration.value = "무"

            getText()
        }

        // 취소버튼
        binding.btnEditBack.setOnClickListener {
            getText()
            finish()
        }

        // 즐겨찾기 별
        binding.imgEditStar.setOnClickListener {
            if (switch == 0)
            {binding.imgEditStar.setImageResource(R.drawable.filledheart)
            switch = 1}
            else {binding.imgEditStar.setImageResource(R.drawable.emptyheart)
            switch = 0}
        }

    }

    fun getText(){
        // 각 텍스트 가져오기
        var etEditName = viewModel.name.value.toString()
        var etEditId = viewModel.id.value.toString()
        var etEditBirth = viewModel.birth.value.toString()
        var etEditVariety = viewModel.variety.value.toString()
        var editGender = viewModel.gender.value.toString()
        var editVaccine = viewModel.vaccine.value.toString()
        var editPreg = viewModel.pregnancy.value.toString()
        var editMilk = viewModel.milk.value.toString()
        var editCas = viewModel.castration.value.toString()
        var editWish = Integer.parseInt(viewModel.wish.value)


        val milkCowInfoModel = MilkCowInfoModel(etEditId,
                etEditName,etEditBirth,etEditVariety,editGender,editVaccine,editPreg,editMilk,editCas,editWish, userNum)


        Log.d(Constants.TAG, " 수정완료 버튼 클릭, ${milkCowInfoModel}")

        if (milkCowInfoModel != null) {
            var cow_id = milkCowInfoModel.id
            Log.d("있니", milkCowInfoModel.id)
            viewModel.cowInfoUpdate(cow_id, milkCowInfoModel)
            Log.d("있니", milkCowInfoModel.name)
            cowInfo = milkCowInfoModel

        }




        val intent = Intent(this, InfoActivity::class.java)
        intent.putExtra("where", "edit")
        Log.d("tqfa", cowInfo?.id.toString())
        intent.putExtra("TEXT", cowInfo)
        startActivity(intent)
        finish()

        Toast.makeText(this, "수정이 완료되었습니다.", Toast.LENGTH_SHORT).show()
    }


    override fun setIntent(intent: Intent) {
        val intent = intent
        val cowInfo = intent.getSerializableExtra("cowInfo") as MilkCowInfoModel?
        Glide.with(this)
            .load("${API_.BASE_URL}image/cowImgOut?cow_id=${cowInfo?.id.toString()}")
            .override(340, 200)
            .fitCenter()
            .centerCrop()
            .into(binding.imgEditPhoto)
        viewModel.name.value = cowInfo?.name.toString()
        viewModel.id.value = cowInfo?.id.toString()
        viewModel.birth.value = cowInfo?.birth.toString()
        viewModel.variety.value = cowInfo?.variety.toString()
        viewModel.gender.value = cowInfo?.gender.toString()
        viewModel.vaccine.value = cowInfo?.vaccine.toString()
        viewModel.pregnancy.value = cowInfo?.pregnancy.toString()
        viewModel.milk.value = cowInfo?.milk.toString()
        viewModel.castration.value = cowInfo?.castration.toString()
        viewModel.wish.value = cowInfo?.list.toString()

    }


    fun observeData() {

    }
}