package com.example.wintopia.view.regist

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.example.wintopia.R
import com.example.wintopia.databinding.ActivityRegistInfoBinding
import com.example.wintopia.dialog.Custumdialog
import com.example.wintopia.dialog.MyCustomDialogInterface
import com.example.wintopia.view.edit.MilkCowInfoModel
import com.example.wintopia.view.info.InfoActivity
import com.example.wintopia.view.main.MainActivity

class RegistInfoActivity : AppCompatActivity() {

    lateinit var binding: ActivityRegistInfoBinding
    val viewModel: RegistViewModel by viewModels()
    lateinit var myCustomDialog: Custumdialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_regist_info)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_regist_info)

        binding.vm = viewModel
        binding.lifecycleOwner = this


        val user_id = "test"

        binding.btnRegistInfoRegist.setOnClickListener {
            // 다이얼 달아죠
            binding.btnRegistInfoRegist.isClickable = false
            // 커스텀 다이얼 띄우기
            myCustomDialog = Custumdialog(this, this@RegistInfoActivity)
            // 다이얼로그 밖에 화면 눌러서 끄기 막기
            myCustomDialog.setCancelable(false)
            myCustomDialog.show()

            if (binding.rbRegistMale.isChecked) viewModel.gender.value =  "수컷"
            else viewModel.gender.value = "암컷"

            if (binding.rbRegistDid.isChecked) viewModel.vaccine.value = "접종"
            else viewModel.vaccine.value = "미접종"

            if (binding.rbRegistPreg.isChecked) viewModel.pregnancy.value = "유"
            else viewModel.pregnancy.value = "무"

            if (binding.rbRegistMilkY.isChecked) viewModel.milk.value = "유"
            else viewModel.milk.value = "무"

            if (binding.rbRegistCasY.isChecked) viewModel.castration.value = "유"
            else viewModel.castration.value = "무"

            val etRegistInfoId = "0"
            val etRegistInfoName = viewModel.name.value.toString()
            val etRegistInfoBirth = viewModel.birth.value.toString()
            val etRegistInfoVariety = viewModel.variety.value.toString()
            val etRegistInfoGender = viewModel.gender.value.toString()
            val etRegistInfoVaccine = viewModel.vaccine.value.toString()
            val etRegistInfoPreg = viewModel.pregnancy.value.toString()
            val etRegistInfoMilk = viewModel.milk.value.toString()
            val etRegistInfoCas = viewModel.castration.value.toString()

            val milkCowInfo = MilkCowInfoModel(etRegistInfoId, etRegistInfoName,
                etRegistInfoBirth, etRegistInfoVariety, etRegistInfoGender, etRegistInfoVaccine,
                etRegistInfoPreg, etRegistInfoMilk, etRegistInfoCas, 0, 0)

            viewModel.registCowInfo(user_id, milkCowInfo)
            observeData(milkCowInfo)
        }

        binding.btnRegistInfoCancel.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }



    }


    fun observeData(milkCow: MilkCowInfoModel) {
        viewModel.registEvent.observe(this){
            when(it){
                "sucess"->{
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("where", "registInfo")
                    intent.putExtra("registInfo", milkCow)
                    startActivity(intent)
                    finish()
                    // 개체 등록 성공
                    val alertDialog = AlertDialog.Builder(this).create()
                    alertDialog.setTitle("등록이 완료되었습니다.")

                    alertDialog.setButton(
                        AlertDialog.BUTTON_POSITIVE, "확인"
                    ) { dialog, which -> dialog.dismiss() }
                    alertDialog.show()

                    val btnPositive = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                    btnPositive.setOnClickListener {
                        myCustomDialog.dismiss()
                        alertDialog.dismiss()

                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }

                    val layoutParams = btnPositive.layoutParams as LinearLayout.LayoutParams
                    layoutParams.weight = 10f
                    btnPositive.layoutParams = layoutParams

                }
                "failed" -> {
                    Toast.makeText(this, "실패", Toast.LENGTH_SHORT).show()
                    // 소 아님 (dialog 필요)
                    val alertDialog = AlertDialog.Builder(this).create()
                    alertDialog.setTitle("개체 등록에 실패했습니다.\n통신을 확인해주세요.")

                    alertDialog.setButton(
                        AlertDialog.BUTTON_POSITIVE, "확인"
                    ) { dialog, which -> dialog.dismiss() }
                    alertDialog.show()

                    val btnPositive = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                    btnPositive.setOnClickListener {
                        myCustomDialog.dismiss()
                        alertDialog.dismiss()

                    }

                    val layoutParams = btnPositive.layoutParams as LinearLayout.LayoutParams
                    layoutParams.weight = 10f
                    btnPositive.layoutParams = layoutParams

                }
            }
        }
    }
}

