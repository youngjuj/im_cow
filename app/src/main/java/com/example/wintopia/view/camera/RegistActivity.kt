package com.example.wintopia.view.camera

import android.Manifest
import android.app.Activity
import android.app.Dialog
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
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import com.example.wintopia.R
import com.example.wintopia.databinding.ActivityRegistBinding
import com.example.wintopia.databinding.RegistDialogBinding
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat

class RegistActivity : AppCompatActivity(), View.OnClickListener {

    // databinding
    lateinit var binding: ActivityRegistBinding
    lateinit var dBinding: RegistDialogBinding

    val viewModel: RegistViewModel by viewModels()

    // 카메라 및 갤러리 연동 변수들
    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_GALLERY = 2
    lateinit var currentPhotoPath: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_regist)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_regist)
        dBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.regist_dialog, null, false)

        binding.vm = viewModel
        binding.lifecycleOwner = this

        observeData()

        binding.btnRegistCancel.setOnClickListener {  }

        binding.btnRegistRegist.setOnClickListener {  }

        binding.imgRegistFace.setOnClickListener(this)
        binding.imgRegistLeft.setOnClickListener(this)
        binding.imgRegistRight.setOnClickListener(this)

        dBinding.tvRegistDCam.setOnClickListener(this)
        dBinding.tvRegistDGal.setOnClickListener(this)
        dBinding.tvRegistDCancel.setOnClickListener(this)


    }
    fun observeData() {

    }


    // camera 접근 권한 요청
    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA),
            REQUEST_IMAGE_CAPTURE)
    }

    // camera 접근 권한 체크
    private fun checkPermission(): Boolean {

        return (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
            android.Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED)
    }

    // 권한 요청 결과
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "권한 설정 완료", Toast.LENGTH_SHORT).show()
        }
        else {
            Toast.makeText(this, "권한 설정 실패", Toast.LENGTH_SHORT).show()
        }
    }

    // camera intent && image파일 생성
    private fun dispatchTakePictureIntent() {
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
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(java.util.Date())
        val storageDir: File? = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply { currentPhotoPath = absolutePath }
    }

    private fun dispatchSelectPictureIntent() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, REQUEST_GALLERY)
    }




    // camera로 찍어서 저장한 파일 imageview로 띄워주기
    lateinit var bitmap: Bitmap
    lateinit var img: ImageView
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            REQUEST_IMAGE_CAPTURE -> {
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
                    binding.imgRegistFace.setImageBitmap(bitmap)

                }
            }
            REQUEST_GALLERY -> {
                val selectedImageURI: Uri? = data?.data

                if(selectedImageURI != null) binding.imgRegistFace.setImageURI(selectedImageURI)
                else Toast.makeText(this, "사진을 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
            }
            else -> {
                Toast.makeText(this, "잘못된 접근입니다..", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            binding.imgRegistFace.id -> {
                img = binding.imgRegistFace
                camDialog()

            }
            binding.imgRegistLeft.id -> {
                img = binding.imgRegistLeft
                camDialog()
            }
            binding.imgRegistRight.id -> {
                img = binding.imgRegistRight
                camDialog()
            }
            dBinding.tvRegistDCam.id -> {
                Toast.makeText(this, "사진클릭클릭", Toast.LENGTH_SHORT).show()
                if(checkPermission()) dispatchTakePictureIntent() else requestPermission()
            }
            dBinding.tvRegistDGal.id -> {
                if(checkPermission()) dispatchSelectPictureIntent() else requestPermission()
            }
        }

    }

    private fun camDialog() {
        val dialog = Dialog(this)

        dialog.setContentView(R.layout.regist_dialog)
        dialog.window!!.setLayout(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)
        dialog.show()

//        dBinding.tvRegistDCam.setOnClickListener {
//            Toast.makeText(this, "사진클릭클릭", Toast.LENGTH_SHORT).show()
//            if(checkPermission()) dispatchTakePictureIntent() else requestPermission()
//        }
//        dBinding.tvRegistDGal.setOnClickListener{
//            if(checkPermission()) dispatchSelectPictureIntent() else requestPermission()
//
//        }
//        dBinding.tvRegistDCancel.setOnClickListener{
//            dialog.closeOptionsMenu()
//        }


    }

}