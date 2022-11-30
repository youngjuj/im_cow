package com.example.wintopia.view.edit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.example.wintopia.R
import com.example.wintopia.data.UserList
import com.example.wintopia.databinding.ActivityEditBinding
import com.example.wintopia.view.info.InfoActivity
import com.example.wintopia.view.utilssd.API_
import com.example.wintopia.view.utilssd.Constants
import com.google.gson.Gson
import com.google.gson.JsonParser
import org.json.JSONObject

class EditActivity : AppCompatActivity() {

    // 데이터 바인딩(1)
    lateinit var binding: ActivityEditBinding
    val viewModel: EditViewModel by viewModels()
    var switch: Int = 0
    var userNum: Int = 0

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
        var etEditBirth = binding.etEditBirth.text.toString()
        var etEditVariety = binding.etEditVariety.text.toString()
        var editGender: String = ""
        binding.rbEditGender.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                binding.rbEditMale.id -> editGender = "수컷"
                binding.rbEditFemale.id -> editGender = "암컷"
            }
        }
        var editVaccine: String = ""
        binding.rbEditVaccine.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                binding.rbEditDid.id -> editVaccine = "접종"
                binding.rbEditDidnt.id -> editVaccine = "미접종"
            }
        }
        var editPreg: String = ""
        binding.rbEditPregnant.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                binding.rbEditPreg.id -> editPreg = "유"
                binding.rbEditNonP.id -> editPreg = "무"
            }
        }
        var editMilk: String = ""
        binding.rbEditMilk.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                binding.rbEditMilkY.id -> editMilk = "유"
                binding.rbEditMilkN.id -> editMilk = "무"
            }
        }
        var editCas: String = ""
        binding.rbEditCas.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                binding.rbEditCasY.id -> editCas = "유"
                binding.rbEditCasN.id -> editCas = "무"
            }
        }


        var milkCowInfoModel = MilkCowInfoModel(etEditId,
                etEditName,etEditBirth,etEditVariety,editGender,editVaccine,editPreg,editMilk,editCas,switch, userNum)

//        var milkCowInfoModel = UserList().getNum()?.let {
//            MilkCowInfoModel(etEditName,
//                etEditId,etEditBirth,etEditVariety,editGender,editVaccine,editPreg,editMilk,editCas,switch,
//                it
//            )
//        }

        Log.d(Constants.TAG, " 수정완료 버튼 클릭, ${milkCowInfoModel}")

//        viewModel.infoOut(jsonString.toString())
        if (milkCowInfoModel != null) {
            viewModel.infoOut(milkCowInfoModel)
        }

        val cowInfo = CowInfo(etEditId,
            etEditName,etEditBirth,etEditVariety,editGender,editVaccine,editPreg,editMilk,editCas,switch, userNum)

        // 수정 후 상세정보페이지 이동
//        val cowInfo = UserList().getNum()?.let {
//            CowInfo(etEditName,
//                etEditId,etEditBirth,etEditVariety,editGender,editVaccine,editPreg,editMilk,editCas,switch,
//                it
//            )
//        }

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
        binding.wvEditPhoto.loadUrl("${API_.BASE_URL}image/getImages?user_id=test&cow_id=${cowInfo?.id.toString()}")
        binding.etEditName.hint = (cowInfo?.name.toString())
            binding.etEditId.hint = (cowInfo?.id.toString())
            binding.etEditBirth.hint = (cowInfo?.birth.toString())
            binding.etEditVariety.hint = (cowInfo?.variety.toString())
            if (cowInfo?.gender!!.equals("수컷")) {
                binding.rbEditMale.isChecked = true
            } else {
                binding.rbEditFemale.isChecked = true
            }

            if(cowInfo?.vaccine!!.equals("접종")) {
                binding.rbEditDid.isChecked = true
            } else {
                binding.rbEditDidnt.isChecked = true

            }
            if (cowInfo?.pregnancy!!.equals("유")) {
                binding.rbEditPreg.isChecked = true
            } else {
                binding.rbEditNonP.isChecked = true

            }

            if (cowInfo?.milk!!.equals("유")) {
                binding.rbEditMilkY.isChecked = true
            } else {
                binding.rbEditMilkN.isChecked = true

            }

            if (cowInfo?.castration!!.equals("유")) {
                binding.rbEditCasY.isChecked = true
            } else {
                binding.rbEditCasN.isChecked = true

            }

    }
}