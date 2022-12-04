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
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Request
import okhttp3.RequestBody
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
        observeData()


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

//        var milkCowInfoModel = UserList().getNum()?.let {
//            MilkCowInfoModel(etEditName,
//                etEditId,etEditBirth,etEditVariety,editGender,editVaccine,editPreg,editMilk,editCas,switch,
//                it
//            )
//        }

        Log.d(Constants.TAG, " 수정완료 버튼 클릭, ${milkCowInfoModel}")

        if (milkCowInfoModel != null) {
            var cow_id = viewModel.id.toString()
            viewModel.infoUpdate(cow_id, milkCowInfoModel)
        }

        // 받아오는건데 데이터는 왜 있을까?
        if (milkCowInfoModel != null) {
            var cow_id = binding.etEditId.text.toString()
            viewModel.cowInfoOne(cow_id, milkCowInfoModel)
        }



//        val cowInfo = CowInfo(etEditId,
//            etEditName,etEditBirth,etEditVariety,editGender,editVaccine,editPreg,editMilk,editCas,switch, userNum)

        // 수정 후 상세정보페이지 이동
//        val cowInfo = UserList().getNum()?.let {
//            CowInfo(etEditName,
//                etEditId,etEditBirth,etEditVariety,editGender,editVaccine,editPreg,editMilk,editCas,switch,
//                it
//            )
//        }

        val intent = Intent(this, InfoActivity::class.java)
        intent.putExtra("where", "edit")
        intent.putExtra("TEXT", milkCowInfoModel)
        startActivity(intent)
        finish()

        Toast.makeText(this, "수정하기", Toast.LENGTH_SHORT).show()
    }

//    override fun setIntent(intent: Intent) {
//            val intent = intent
//            val cowInfo = intent.getSerializableExtra("cowInfo") as MilkCowInfoModel?
//        binding.wvEditPhoto.loadUrl("${API_.BASE_URL}image/cowImgOut?cow_id=${cowInfo?.id.toString()}")
////        Log.d("널?", "${cowInfo?.name.toString()}")
//        binding.etEditName.hint = (cowInfo?.name.toString())
////            binding.etEditId.hint = (cowInfo?.id.toString())
//        viewModel.id.value = (cowInfo?.id.toString())
//            binding.etEditBirth.hint = (cowInfo?.birth.toString())
//            binding.etEditVariety.hint = (cowInfo?.variety.toString())
//            if (cowInfo?.gender!!.equals("수컷")) {
//                binding.rbEditMale.isChecked = true
//            } else {
//                binding.rbEditFemale.isChecked = true
//            }
//
//            if(cowInfo?.vaccine!!.equals("접종")) {
//                binding.rbEditDid.isChecked = true
//            } else {
//                binding.rbEditDidnt.isChecked = true
//
//            }
//            if (cowInfo?.pregnancy!!.equals("유")) {
//                binding.rbEditPreg.isChecked = true
//            } else {
//                binding.rbEditNonP.isChecked = true
//
//            }
//
//            if (cowInfo?.milk!!.equals("유")) {
//                binding.rbEditMilkY.isChecked = true
//            } else {
//                binding.rbEditMilkN.isChecked = true
//
//            }
//
//            if (cowInfo?.castration!!.equals("유")) {
//                binding.rbEditCasY.isChecked = true
//            } else {
//                binding.rbEditCasN.isChecked = true
//
//            }
//
//    }

    override fun setIntent(intent: Intent) {
        val intent = intent
        val cowInfo = intent.getSerializableExtra("cowInfo") as MilkCowInfoModel?
        binding.wvEditPhoto.loadUrl("${API_.BASE_URL}image/cowImgOut?cow_id=${cowInfo?.id.toString()}")
//        Log.d("널?", "${cowInfo?.name.toString()}")
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
        viewModel.apply {
            id.observe(this@EditActivity) {
                binding.etEditId.hint = it
            }
            name.observe(this@EditActivity) {
                binding.etEditName.hint = it
            }
            birth.observe(this@EditActivity){
                binding.etEditBirth.hint = it
                }
            variety.observe(this@EditActivity){
                binding.etEditVariety.hint = it
            }
            gender.observe(this@EditActivity){
                when (binding.rbEditGender.checkedRadioButtonId) {
                    binding.rbEditMale.id -> binding.rbEditMale.text
                    binding.rbEditFemale.id -> binding.rbEditFemale.text
                }
            }
            vaccine.observe(this@EditActivity){
                when (binding.rbEditVaccine.checkedRadioButtonId) {
                    binding.rbEditDid.id -> binding.rbEditDid.text
                    binding.rbEditDidnt.id -> binding.rbEditDidnt.text
                }

            }
            pregnancy.observe(this@EditActivity){
                when (binding.rbEditPregnant.checkedRadioButtonId) {
                    binding.rbEditPreg.id -> binding.rbEditPreg.text
                    binding.rbEditNonP.id -> binding.rbEditNonP.text
                }
            }
            milk.observe(this@EditActivity){
                when (binding.rbEditMilk.checkedRadioButtonId) {
                    binding.rbEditMilkY.id -> binding.rbEditMilkY.text
                    binding.rbEditMilkN.id -> binding.rbEditMilkN.text
                }
            }
            castration.observe(this@EditActivity){
                when (binding.rbEditCas.checkedRadioButtonId) {
                    binding.rbEditCasY.id -> binding.rbEditCasY.text
                    binding.rbEditCasN.id -> binding.rbEditCasN.text
                }
            }
        }

    }
}