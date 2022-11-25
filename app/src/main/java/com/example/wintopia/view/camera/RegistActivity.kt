package com.example.wintopia.view.camera

import android.Manifest
import android.app.Activity
import android.app.ActivityManager
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.wintopia.R
import com.example.wintopia.databinding.ActivityRegistBinding
import com.example.wintopia.databinding.RegistDialogBinding
import com.github.dhaval2404.imagepicker.ImagePicker
import okhttp3.internal.wait
import java.io.File
import java.io.IOException
import java.io.Serializable
import java.text.SimpleDateFormat
import kotlin.properties.Delegates

class RegistActivity : AppCompatActivity(){

    // databinding
    lateinit var binding: ActivityRegistBinding
    var resultNum: Int = 0
    val viewModel: RegistViewModel by viewModels()

    // 카메라 및 갤러리 연동 변수들
    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_GALLERY = 2
    lateinit var currentPhotoPath: String
    lateinit var img: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_regist)

        binding.vm = viewModel
        binding.lifecycleOwner = this

        observeData()

        binding.btnRegistCancel.setOnClickListener {  }

        binding.btnRegistRegist.setOnClickListener {  }

        // 사진등록 onClickListener
        binding.imgRegistFace.setOnClickListener {
            val dialog = CamDialog(this)
            dialog.show()
            dialog.setOnCamDialogClickListener(object: CamDialog.CamDialogClickListener {
                override fun onClick(req: Int) {
                    resultNum = req
                    try{// dialog 선택 후 실행 이벤트
                        if (resultNum == 1) {
                            if (checkPermission()) dispatchTakePictureIntent() else requestPermission()
                            dialog.dismiss()
                        } else if (resultNum == 2) {
                            if (checkPermission()) dispatchSelectPictureIntent() else requestPermission()
                            dialog.dismiss()
                        } else{
                            dialog.dismiss()
                        }
                    } catch (e: Exception) {
                    }
                }

            })
            img = binding.imgRegistFace
        }

        binding.imgRegistLeft.setOnClickListener {
            val dialog = CamDialog(this)
            dialog.show()
            dialog.setOnCamDialogClickListener(object: CamDialog.CamDialogClickListener {
                override fun onClick(req: Int) {
                    resultNum = req
                    try{// dialog 선택 후 실행 이벤트
                        if (resultNum == 1) {
                            if (checkPermission()) dispatchTakePictureIntent() else requestPermission()
                            dialog.dismiss()
                        } else if (resultNum == 2) {
                            if (checkPermission()) dispatchSelectPictureIntent() else requestPermission()
                            dialog.dismiss()
                        } else{
                            dialog.dismiss()
                        }
                    } catch (e: Exception) {
                    }
                }

            })
            img = binding.imgRegistLeft
        }

        binding.imgRegistRight.setOnClickListener {
            val dialog = CamDialog(this)
            dialog.show()
            dialog.setOnCamDialogClickListener(object: CamDialog.CamDialogClickListener {
                override fun onClick(req: Int) {
                    resultNum = req
                    try{// dialog 선택 후 실행 이벤트
                        if (resultNum == 1) {
                            if (checkPermission()) dispatchTakePictureIntent() else requestPermission()
                            dialog.dismiss()
                        } else if (resultNum == 2) {
                            if (checkPermission()) dispatchSelectPictureIntent() else requestPermission()
                            dialog.dismiss()
                        } else{
                            dialog.dismiss()
                        }
                    } catch (e: Exception) {
                    }
                }

            })
            img = binding.imgRegistRight
        }
    }


    // data변경 실시간 반영
    fun observeData() {

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
    lateinit var result: String
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "권한 설정 완료", Toast.LENGTH_SHORT).show()
            result = "success"
        }
        else {
            Toast.makeText(this, "권한 설정 실패", Toast.LENGTH_SHORT).show()
            result = "fail"
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
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, REQUEST_GALLERY)
    }


    // camera로 찍어서 저장한 파일 imageview로 띄워주기
    lateinit var bitmap: Bitmap
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            REQUEST_IMAGE_CAPTURE -> {
                Log.v("순서", "onActivityResult img")
                Log.v("img_request", "${img?.id}")
                if(resultCode == Activity.RESULT_OK) {
                    val file = File(currentPhotoPath)
                    if (Build.VERSION.SDK_INT < 28) {
                        bitmap = MediaStore.Images.Media
                            .getBitmap(this.contentResolver, Uri.fromFile(file))
                    } else {
                        val decode = ImageDecoder.createSource(this.contentResolver,
                            Uri.fromFile(file))
                        bitmap = ImageDecoder.decodeBitmap(decode)
                    }
                    img?.setImageBitmap(bitmap)

                }
            }
            REQUEST_GALLERY -> {
                val selectedImageURI: Uri? = data?.data

                if(selectedImageURI != null) img?.setImageURI(selectedImageURI)
                else Toast.makeText(this, "사진을 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
            }
            else -> {
                Toast.makeText(this, "잘못된 접근입니다..", Toast.LENGTH_SHORT).show()
            }
        }
    }
}



