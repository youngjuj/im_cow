package com.example.wintopia.view.regist

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import android.provider.MediaStore
import android.util.Log
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wintopia.dialog.MyCustomDialogInterface
import com.example.wintopia.R
import com.example.wintopia.databinding.ActivityRegistBinding
import com.example.wintopia.dialog.Custumdialog
import com.example.wintopia.view.adapter.RegistAdapter
import com.example.wintopia.view.edit.MilkCowInfoModel
import com.example.wintopia.view.info.InfoActivity
import com.example.wintopia.view.utilssd.Constants
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import kotlin.math.log

class RegistActivity : AppCompatActivity(), MyCustomDialogInterface {

    // databinding
    lateinit var binding: ActivityRegistBinding
    var resultNum: Int = 0
    val viewModel: RegistViewModel by viewModels()
    // 바인딩
    private var mBinding: ActivityRegistBinding? = null
    // 변수
    val res = "true"

    // 카메라 및 갤러리 연동 변수들
    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_GALLERY = 2
    lateinit var currentPhotoPath: String

    lateinit var registAdapter: RegistAdapter

    var user_id = "test"
    var cow_id = "1"

    lateinit var myCustomDialog: Custumdialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_regist)

        binding.vm = viewModel
        binding.lifecycleOwner = this
        // 뷰바인딩
        mBinding = ActivityRegistBinding.inflate(layoutInflater)


        observeData()

        binding.btnRegistBack.setOnClickListener {
            finish()
        }

        registAdapter = RegistAdapter(viewModel.imgList)

        binding.rvRegistPhoto.apply {

            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = registAdapter
            binding.rvRegistPhoto.adapter = adapter
        }
        // 사진찾기 버튼
        binding.btnRegisPhoto.setOnClickListener {
            if (checkPermission()) dispatchSelectPictureIntent() else requestPermission()
            binding.flRegistCaution.isVisible = false
        }
        // 등록하기 버튼 클릭시
        binding.btnRegistRegist.setOnClickListener {
            if ((viewModel.imgFileList).size < 5){
                binding.flRegistCaution.isVisible = true
                Toast.makeText(this, "사진을 5장 추가해 주세요.", Toast.LENGTH_SHORT).show()
            } else {
                // 커스텀 다이얼 띄우기
                myCustomDialog = Custumdialog(this, this)
                // 다이얼로그 밖에 화면 눌러서 끄기 막기
                myCustomDialog.setCancelable(false)
                myCustomDialog.show()

                // 이미지 통신
                viewModel.sendImage(cow_id, viewModel.imgFileList)
            }
            listImgEvent()
        }
    }


    // data변경 실시간 반영
    fun observeData() {
        viewModel.event.observe(this){
            when(it){
                "sucess"->{
                    Toast.makeText(this, "성공", Toast.LENGTH_SHORT).show()
                }
                "failed" -> {
                    Toast.makeText(this, "실패", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    // camera 접근 권한 요청
    fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA),
            REQUEST_IMAGE_CAPTURE)
    }

    // camera 접근 권한 체크
    fun checkPermission(): Boolean {

        return (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
            android.Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED)
    }

    // 권한 요청 결과
    lateinit var resultPer: String
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "권한 설정 완료", Toast.LENGTH_SHORT).show()
            resultPer = "success"
        }
        else {
            Toast.makeText(this, "권한 설정 실패", Toast.LENGTH_SHORT).show()
            resultPer = "fail"
        }
    }

    // gallery에서 사진 선택
    fun dispatchSelectPictureIntent() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        startActivityForResult(intent, REQUEST_GALLERY)
    }


    // camera로 찍어서 저장한 파일 imageview로 띄워주기
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            REQUEST_GALLERY -> {
            }
            else -> {
            }
        }
            // 사진 선택 개수 제한
            if(data?.clipData != null) {
                val count = data.clipData!!.itemCount
                if(count < 5) {
                    binding.flRegistCaution.isVisible = true
                    Toast.makeText(applicationContext, "사진을 5장 선택해주세요.", Toast.LENGTH_SHORT).show()
                } else {

                // 선택된 사진 리스트에 추가하기
                viewModel.imgList.clear()
                viewModel.imgFileList.clear()
                for (i in 0 until count) {
                    val imageUri = data.clipData!!.getItemAt(i).uri
                    viewModel.imgList.add(imageUri)
                    val path = absolutelyPath(this, imageUri)
                    val file = File(path)
                    val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
                    viewModel.imgFileList.add(MultipartBody.Part.createFormData("files", file.name, requestFile))
                }
                }
            }
        registAdapter.notifyDataSetChanged()
        }



    fun dialogEvent(myCustomDialog: Custumdialog){
        Handler(Looper.getMainLooper()).postDelayed({
            if (viewModel.eventCowId.value.toString() == "false"){
                // 소 아님 (dialog 필요)
                val alertDialog = AlertDialog.Builder(this).create()
                alertDialog.setTitle("소 사진이 아닙니다.")

                alertDialog.setButton(
                    AlertDialog.BUTTON_POSITIVE, "확인"
                ) { dialog, which -> dialog.dismiss() }
                alertDialog.show()

                val btnPositive = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                btnPositive.setOnClickListener {
                    myCustomDialog.dismiss()
                    alertDialog.dismiss()
                    viewModel.eventCowId.value = ""
                    viewModel.event.value = ""
                    viewModel.imgFileList.clear()

                }

                val layoutParams = btnPositive.layoutParams as LinearLayout.LayoutParams
                layoutParams.weight = 10f
                btnPositive.layoutParams = layoutParams


            }else if (viewModel.eventCowId.value.toString() == "true"){
                // 소 맞음(dialog 필요)
                val alertDialog = AlertDialog.Builder(this).create()
                alertDialog.setTitle("미등록 개체입니다. \n소를 등록하시겠습니까?")

                alertDialog.setButton(
                    AlertDialog.BUTTON_POSITIVE, "취소 하기"
                ) { dialog, which -> dialog.dismiss() }

                alertDialog.setButton(
                    AlertDialog.BUTTON_NEGATIVE, "등록 하기"
                ) { dialog, which -> dialog.dismiss() }
                alertDialog.show()

                val btnPositive = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                btnPositive.setOnClickListener {
                    myCustomDialog.dismiss()
                    alertDialog.dismiss()
                    viewModel.eventCowId.value = ""
                    viewModel.event.value = ""
                    viewModel.imgFileList.clear()
                }
                val btnNegative = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                btnNegative.setOnClickListener {
                    val intent = Intent(this, RegistInfoActivity::class.java)
                    startActivity(intent)
                    finish()
                }


                val layoutParams = btnPositive.layoutParams as LinearLayout.LayoutParams
                layoutParams.weight = 10f
                btnPositive.layoutParams = layoutParams
                btnNegative.layoutParams = layoutParams

            }
            else {
                // 해당 개체 조회 후 인텐트
                var cow_id = viewModel.eventCowId.value.toString()
                viewModel.cowInfoOne(cow_id)
                val alertDialog = AlertDialog.Builder(this).create()
                alertDialog.setTitle("등록된 객체입니다!")

                alertDialog.setButton(
                    AlertDialog.BUTTON_POSITIVE, "확인"
                ) { dialog, which -> dialog.dismiss() }
                alertDialog.show()

                val btnPositive = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                btnPositive.setOnClickListener {
                    myCustomDialog.dismiss()
                    alertDialog.dismiss()

                    viewModel.eventCowId.value = ""
                    viewModel.event.value = ""
                    viewModel.imgFileList.clear()

                    val intent = Intent(this, InfoActivity::class.java)
                    intent.putExtra("where", "regist")
                    val dataId = viewModel.cowInfoEvent?.id.toString()
                    val dataName = viewModel.cowInfoEvent?.name.toString()
                    val dataBirth = viewModel.cowInfoEvent?.birth.toString()
                    val dataVariety = viewModel.cowInfoEvent?.variety.toString()
                    val dataGender = viewModel.cowInfoEvent?.gender.toString()
                    val dataVaccine = viewModel.cowInfoEvent?.vaccine.toString()
                    val dataPreg = viewModel.cowInfoEvent?.pregnancy.toString()
                    val dataMilk = viewModel.cowInfoEvent?.milk.toString()
                    val dataCast = viewModel.cowInfoEvent?.castration.toString()
                    val dataWish = viewModel.cowInfoEvent?.list!!.toInt()
                    val dataNum = viewModel.cowInfoEvent?.num!!.toInt()

                    val data = MilkCowInfoModel(dataId,
                            dataName,
                            dataBirth,
                            dataVariety,
                            dataGender,
                            dataVaccine,
                            dataPreg,
                            dataMilk,
                            dataCast,
                            dataWish,
                            dataNum)
                    intent.putExtra("regist", data)
                    startActivity(intent)
                    finish()
                }
                val layoutParams = btnPositive.layoutParams as LinearLayout.LayoutParams
                layoutParams.weight = 10f
                btnPositive.layoutParams = layoutParams
            }
        }, 1000)
    }

    override fun onLikedBtnClicked() {
    }
    override fun onSubscribeBtnClicked() {
    }

    fun listImgEvent() {
        viewModel.event.observe(this){
            when(it){
                "success" ->{
                    dialogEvent(myCustomDialog)
                }
                "fail" -> {
                    Toast.makeText(this, "통신실패", Toast.LENGTH_SHORT).show()
                }
                "fail1" ->{
                    Toast.makeText(this, "통신상태불량", Toast.LENGTH_SHORT).show()
                }
                "fail2" ->{
                    Toast.makeText(this, "통신 실패", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}

    // 절대경로 변환
    fun absolutelyPath(ctx: Activity, uri: Uri?): String {
        var result = ""
        var proj: Array<String> = arrayOf(MediaStore.Images.Media.DATA)
        var c: Cursor? = ctx.contentResolver.query(uri!!, proj, null, null, null)

        if(c==null) {
            result = uri?.path.toString()
        } else {
            c.moveToFirst()
            var index = c.getColumnIndex(proj[0])
            result = c.getString(index)
            c.close()
        }

        return result!!
    }


