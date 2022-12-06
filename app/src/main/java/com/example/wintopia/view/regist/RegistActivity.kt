package com.example.wintopia.view.regist

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.ImageDecoder
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
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wintopia.MyCustomDialog
import com.example.wintopia.MyCustomDialogInterface
import com.example.wintopia.R
import com.example.wintopia.databinding.ActivityRegistBinding
import com.example.wintopia.view.adapter.RegistAdapter
import com.example.wintopia.view.edit.MilkCowInfoModel
import com.example.wintopia.view.main.MainActivity
import com.example.wintopia.view.utilssd.Constants
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat

class RegistActivity : AppCompatActivity(),MyCustomDialogInterface {

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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_regist)

        binding.vm = viewModel
        binding.lifecycleOwner = this
        // 뷰바인딩
        mBinding = ActivityRegistBinding.inflate(layoutInflater)


        observeData()

//        binding.btnRegistCancel.setOnClickListener {
//            finish()
//        }

        registAdapter = RegistAdapter(viewModel.imgList)

        binding.rvRegistPhoto.apply {

            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = registAdapter
            binding.rvRegistPhoto.adapter = adapter
        }
        binding.btnRegisPhoto.setOnClickListener {
            if (checkPermission()) dispatchSelectPictureIntent() else requestPermission()

        }

        binding.btnRegistRegist.setOnClickListener {
            viewModel.sendImage(cow_id, viewModel.imgFileList)
            val intent = Intent(this, RegistInfoActivity::class.java)
            startActivity(intent)

//            val myCustomDialog = MyCustomDialog(this, this)
//            // 다이얼로그 밖에 화면 눌러서 끄기 막기
//            myCustomDialog.setCancelable(false)
//            myCustomDialog.show()
//            Handler(Looper.getMainLooper()).postDelayed({
//                if (res.equals("false")) {
//                    val alertDialog = AlertDialog.Builder(this).create()
//                    alertDialog.setTitle("소 사진이 아닙니다.")
//
//                    alertDialog.setButton(
//                        AlertDialog.BUTTON_POSITIVE, "확인"
//                    ) { dialog, which -> dialog.dismiss() }
//                    alertDialog.show()
//
//                    val btnPositive = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
//                    btnPositive.setOnClickListener {
//                        myCustomDialog.dismiss()
//                        alertDialog.dismiss()
//                    }
//
//                    val layoutParams = btnPositive.layoutParams as LinearLayout.LayoutParams
//                    layoutParams.weight = 10f
//                    btnPositive.layoutParams = layoutParams
//
//
//                } else if (res.equals("true")) {
//                    val alertDialog = AlertDialog.Builder(this).create()
//                    alertDialog.setTitle("소를 등록하시겠습니까?")
//
//                    alertDialog.setButton(
//                        AlertDialog.BUTTON_POSITIVE, "취소 하기"
//                    ) { dialog, which -> dialog.dismiss() }
//
//                    alertDialog.setButton(
//                        AlertDialog.BUTTON_NEGATIVE, "등록 하기"
//                    ) { dialog, which -> dialog.dismiss() }
//                    alertDialog.show()
//
//                    val btnPositive = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
//                    btnPositive.setOnClickListener {
//                        myCustomDialog.dismiss()
//                        alertDialog.dismiss()
//                    }
//                    val btnNegative = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
//                    btnNegative.setOnClickListener {
//                        val intent = Intent(this, MainActivity::class.java)
////                    intent.putExtra("cowInfo", cowInfo)
//                        startActivity(intent)
//                    }
//
//
//                    val layoutParams = btnPositive.layoutParams as LinearLayout.LayoutParams
//                    layoutParams.weight = 10f
//                    btnPositive.layoutParams = layoutParams
//                    btnNegative.layoutParams = layoutParams
//
//                } else {
//                    val alertDialog = AlertDialog.Builder(this).create()
//                    alertDialog.setTitle("이미 등록된 객체입니다!")
//
//                    alertDialog.setButton(
//                        AlertDialog.BUTTON_POSITIVE, "확인"
//                    ) { dialog, which -> dialog.dismiss() }
//                    alertDialog.show()
//
//                    val btnPositive = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
//                    btnPositive.setOnClickListener {
//                        val intent = Intent(this, MainActivity::class.java)
////                    intent.putExtra("cowInfo", cowInfo)
//                        startActivity(intent)
//                    }
//                    val layoutParams = btnPositive.layoutParams as LinearLayout.LayoutParams
//                    layoutParams.weight = 10f
//                    btnPositive.layoutParams = layoutParams
//                }
//            }, 3000)
//
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

    // camera intent && image파일 생성
    fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            if (takePictureIntent.resolveActivity(this.packageManager) != null) {
                val photoFile: File? =
                    try {
                        createImageFile()
                    } catch (ex: IOException) {
                        Log.d("Camera오류", "그림파일 만드는 중 에러생김")
                        null
                    }
                if (Build.VERSION.SDK_INT < 24) {
                    if(photoFile != null) {
                        val photoURI = Uri.fromFile(photoFile)
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                    }
                } else {
                    photoFile?.also {
                        val photoURI: Uri = FileProvider.getUriForFile(
                            this, "com.example.wintopia.fileprovider", it
                        )
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                    }
                }
            }

        }
    }

    // camera로 찍은 파일 저장하기
    fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(java.util.Date())
        val storageDir: File? = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply { currentPhotoPath = absolutePath }
    }

    // gallery에서 사진 선택
    fun dispatchSelectPictureIntent() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        startActivityForResult(intent, REQUEST_GALLERY)
//        Intent(Intent.ACTION_PICK).apply {
//            data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
//            startActivityForResult(this, REQUEST_GALLERY)
//        }
    }


    // camera로 찍어서 저장한 파일 imageview로 띄워주기
    lateinit var bitmap: Bitmap
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
//            REQUEST_IMAGE_CAPTURE -> {
//                Log.v("순서", "onActivityResult img")
////                Log.v("img_request", "${img?.id}")
//                if(resultCode == Activity.RESULT_OK) {
//                    val file = File(currentPhotoPath)
//                    Log.d("file 경로", currentPhotoPath)
//                    val uri = currentPhotoPath.toUri()
//                    viewModel.imgList.add(uri)
//
//                    val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
//                    viewModel.imgFileList.add(MultipartBody.Part.createFormData("files", file.name, requestFile))
////                    val body = MultipartBody.Part.createFormData("file", file.name, requestFile)
//
//                    var user_id = "test"
//                    var cow_id = "1"
//                    Log.d("가즈아", ""+viewModel.imgList)
//
////                    sendImage(cow_id, imgList)
//
//                    if (Build.VERSION.SDK_INT < 28) {
//                        bitmap = MediaStore.Images.Media
//                            .getBitmap(this.contentResolver, Uri.fromFile(file))
//                    } else {
//                        val decode = ImageDecoder.createSource(this.contentResolver,
//                            Uri.fromFile(file))
//                        bitmap = ImageDecoder.decodeBitmap(decode)
//                    }
////                    img?.setImageBitmap(bitmap)
//
//                }
//            }
            REQUEST_GALLERY -> {
                Log.d(Constants.TAG, "" + viewModel.imgList)
                Log.d(Constants.TAG, "GALLERY" + viewModel.imgList)
//                if (selectedImageURI != null) {
////                    sendImage(user_id, cow_id, imgList)
//
//                    if (selectedImageURI != null) img?.setImageURI(selectedImageURI)
//                    else Toast.makeText(this, "사진을 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
//                }
            }
            else -> {
                Toast.makeText(this, "잘못된 접근입니다..", Toast.LENGTH_SHORT).show()
            }
        }
            // 사진 선택 개수 제한
            if(data?.clipData != null) {
                val count = data.clipData!!.itemCount
                if(count >5) {
                    Toast.makeText(applicationContext, "사진은 5장까지 선택 가능합니다.", Toast.LENGTH_SHORT).show()
                } else if(count <3) {
                    Toast.makeText(applicationContext, "사진은 최소 3장을 선택해주세요.", Toast.LENGTH_SHORT).show()
                    return
                }

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
                Log.d(Constants.TAG, "이미지 리스트에 추가 완료")
            }
        registAdapter.notifyDataSetChanged()
        }

    // Subscribe Button Clicked
    override fun onLikedBtnClicked() {
        Toast.makeText(this, "구독버튼 클릭", Toast.LENGTH_SHORT).show()
    }
    // Like Button Clikced
    override fun onSubscribeBtnClicked() {
        Toast.makeText(this, "좋아요버튼 클릭", Toast.LENGTH_SHORT).show()
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

        Log.d("경로로그", result.toString())

        return result!!
    }











//이미지와 정보들을 엮어서 보내지 않아도 되는걸까?



