package com.example.wintopia.view.camera

import android.Manifest.permission.CAMERA
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.ImageDecoder
import android.media.SoundPool
import android.net.Uri
import android.os.*
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.example.wintopia.R
import com.example.wintopia.databinding.FragmentCameraBinding
import com.example.wintopia.dialog.MyCustomDialog
import com.example.wintopia.dialog.MyCustomDialogInterface
import com.example.wintopia.retrofit.RetrofitClient
import com.example.wintopia.retrofit.RetrofitInterface
import com.example.wintopia.view.edit.MilkCowInfoModel
import com.example.wintopia.view.info.InfoActivity
import com.example.wintopia.view.main.MainActivity
import com.example.wintopia.view.regist.RegistActivity
import com.example.wintopia.view.regist.RegistInfoActivity
import com.example.wintopia.view.utilssd.API_
import com.example.wintopia.view.utilssd.Constants
import com.example.wintopia.view.utilssd.Constants.TAG
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Multipart
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat

class CameraFragment: DialogFragment(), MyCustomDialogInterface {
    private var show: Boolean? = null

    // 카메라 및 갤러리 연동 변수들
    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_GALLERY = 2
    lateinit var currentPhotoPath: String
    var oneImgEvent = MutableLiveData<String>()
    lateinit var myCustomDialog: MyCustomDialog


    lateinit var binding: FragmentCameraBinding
    lateinit var res: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        // data binding
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_camera, container, false)

        // data for floatting buttons
        arguments?.let {
            show = it.getBoolean("data")
        }

        // sound effect
        val soundPool = SoundPool.Builder().build()
        var soundId :Int = 0
        soundId = soundPool.load(requireContext(), R.raw.cow, 1)

        // camera floatting button onClickListener
        binding.fbCameraCam.setOnClickListener {
            soundPool.play(soundId, 1.0f, 1.0f, 0, 0, 1.0f)

            Toast.makeText(requireActivity(), "fbCameraCam", Toast.LENGTH_SHORT).show()
            if (checkPermission()) dispatchTakePictureIntent() else requestPermission()
        }
        binding.fbCameraGal.setOnClickListener {
            soundPool.play(soundId, 1.0f, 1.0f, 0, 0, 1.0f)

            Toast.makeText(requireActivity(), "fbCameraGal", Toast.LENGTH_SHORT).show()
            if (checkPermission()) dispatchSelectPictureIntent() else requestPermission()
        }

        // Inflate the layout for this fragment
        return binding.root
    }

    // camera 접근 권한 요청
    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE, CAMERA),
            REQUEST_IMAGE_CAPTURE
        )
    }

    // camera 접근 권한 체크
    private fun checkPermission(): Boolean {

        return (ContextCompat.checkSelfPermission(
            requireContext(),
            android.Manifest.permission.CAMERA
        )
                == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
            requireContext(),
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        )
                == PackageManager.PERMISSION_GRANTED)
    }

    lateinit var result: String

    // 권한 요청 결과
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(requireActivity(), "권한 설정 완료", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireActivity(), "권한 설정 실패", Toast.LENGTH_SHORT).show()
        }
    }

    // camera intent && image파일 생성
    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            if (takePictureIntent.resolveActivity(requireActivity().packageManager) != null) {
                val photoFile: File? =
                    try {
                        createImageFile()
                    } catch (ex: IOException) {
                        Log.d("Camera오류", "그림파일 만드는 중 에러생김")
                        null
                    }
                if (Build.VERSION.SDK_INT < 24) {
                    if (photoFile != null) {
                        val photoURI = Uri.fromFile(photoFile)
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                    }
                } else {
                    photoFile?.also {
                        val photoURI: Uri = FileProvider.getUriForFile(
                            requireContext(), "com.example.wintopia.fileprovider", it
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
        val storageDir: File? =
            requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply { currentPhotoPath = absolutePath }
    }

    private fun dispatchSelectPictureIntent() {

        Intent(Intent.ACTION_PICK).apply {
            data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            startActivityForResult(this, REQUEST_GALLERY)
        }
    }


    // camera로 찍어서 저장한 파일 imageview로 띄워주기
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            REQUEST_IMAGE_CAPTURE -> {
                if (currentPhotoPath != null){
                if (resultCode == RESULT_OK) {
                    val file = File(currentPhotoPath)

                    val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
                    val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

                    Log.d(TAG, "" + body)


                    if (Build.VERSION.SDK_INT < 28) {
                        val bitmap = MediaStore.Images.Media
                            .getBitmap(requireActivity().contentResolver, Uri.fromFile(file))
                        binding.imgCameraPic.setImageBitmap(bitmap)

                    } else if (Build.VERSION.SDK_INT >= 28) {
                        val decode = ImageDecoder.createSource(
                            requireActivity().contentResolver,
                            Uri.fromFile(file)
                        )
                        val bitmap = ImageDecoder.decodeBitmap(decode)
                        binding.imgCameraPic.setImageBitmap(bitmap)
                    } else {
                        Toast.makeText(context, "오류", Toast.LENGTH_SHORT).show()
                    }
                    sendImage(body)

                } }else {
                    Toast.makeText(context, "오류", Toast.LENGTH_SHORT).show()
                }
            }
            REQUEST_GALLERY -> {
                val selectedImageURI: Uri? = data?.data
                if (selectedImageURI == null) {
                    Toast.makeText(context, "사진 입력 오류", Toast.LENGTH_SHORT).show()
                } else {
                    val path = absolutelyPath(requireActivity(), selectedImageURI)
                    val file = File(path)

                    val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
                    val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

                    sendImage(body)
                    Glide.with(requireContext())
                        .load(selectedImageURI)
                        .override(300, 400)
                        .fitCenter()
                        .into(binding.imgCameraPic)

                }

                if (selectedImageURI != null) {


                } else Toast.makeText(activity, "사진을 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
            }
            else -> {
                Toast.makeText(activity, "잘못된 접근입니다..", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // 절대경로 변환
    fun absolutelyPath(ctx: Activity, uri: Uri?): String {
        var proj: Array<String> = arrayOf(MediaStore.Images.Media.DATA)
        var c: Cursor? = ctx.contentResolver.query(uri!!, proj, null, null, null)

        if (c == null) {
            result = uri?.path.toString()
        } else {
            c.moveToFirst()
            var index = c.getColumnIndex(proj[0])
            result = c.getString(index)
            c.close()
        }

        return result!!
    }


    //웹서버로 이미지전송
    fun sendImage(image: MultipartBody.Part) {
        Log.d(TAG, "웹서버로 이미지전송")
        responseImg()

        //Retrofit 인스턴스 생성
        val retrofit = RetrofitClient.getInstnace(API_.BASE_URL)
        val service = retrofit.create(RetrofitInterface::class.java) // 레트로핏 인터페이스 객체 구현
        val call = service.getPhoto(image) //통신 API 패스 설정

        call?.enqueue(object : Callback<String?> {
            override fun onResponse(call: Call<String?>, response: Response<String?>) {
                if (response.isSuccessful) {
                    dialogEvent(myCustomDialog)
                    oneImgEvent.value = response.body().toString()

                } else {
                }
            }
            override fun onFailure(call: Call<String?>, t: Throwable) {
            }
        })
    }


    fun responseImg(){
        myCustomDialog = MyCustomDialog(requireContext(), this)
        // 다이얼로그 밖에 화면 눌러서 끄기 막기
        myCustomDialog.setCancelable(false)
        myCustomDialog.show()
    }

    fun dialogEvent(myCustomDialog:MyCustomDialog){

        Handler(Looper.getMainLooper()).postDelayed({
            if (oneImgEvent.value.toString() == "false"){
                // 소 아님 (dialog 필요)
                val alertDialog = AlertDialog.Builder(context).create()
                alertDialog.setTitle("소 사진이 아닙니다.")

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


            }else if (oneImgEvent.value.toString() == "true"){
                // 소 맞음(dialog 필요)
                val alertDialog = AlertDialog.Builder(context).create()
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
                }
                val btnNegative = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                btnNegative.setOnClickListener {
                    val intent = Intent(requireActivity(), RegistActivity::class.java)
                    startActivity(intent)
                }


                val layoutParams = btnPositive.layoutParams as LinearLayout.LayoutParams
                layoutParams.weight = 10f
                btnPositive.layoutParams = layoutParams
                btnNegative.layoutParams = layoutParams

            }
            else {
                // 해당 개체 조회 후 인텐트
                var cow_id = oneImgEvent.value.toString()
                val alertDialog = AlertDialog.Builder(context).create()
                alertDialog.setTitle("등록된 객체입니다!")

                alertDialog.setButton(
                    AlertDialog.BUTTON_POSITIVE, "확인"
                ) { dialog, which -> dialog.dismiss() }
                alertDialog.show()

                val btnPositive = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                btnPositive.setOnClickListener {
                    cowInfoOne(cow_id)
                    myCustomDialog.dismiss()
                    alertDialog.dismiss()
                }
                val layoutParams = btnPositive.layoutParams as LinearLayout.LayoutParams
                layoutParams.weight = 10f
                btnPositive.layoutParams = layoutParams
            }
        }, 3000)
    }

    fun cowInfoOne(cow_id: String){
        //Retrofit 인스턴스 생성
        val retrofit = RetrofitClient.getInstnace(API_.BASE_URL)
        val service = retrofit.create(RetrofitInterface::class.java) // 레트로핏 인터페이스 객체 구현

        val call: Call<List<MilkCowInfoModel>> = service.getData(cow_id)
        call!!.enqueue(object : Callback<List<MilkCowInfoModel>> {
            override fun onResponse(call: Call<List<MilkCowInfoModel>>, response: Response<List<MilkCowInfoModel>>) {
                Log.d(Constants.TAG, "onResponse")
                if (response.isSuccessful()) {
                    Log.e(Constants.TAG, "onResponse success")
                    // 서버에서 응답받은 데이터
                    val result = response.body()!!.get(0)
                    val intent = Intent(requireActivity(), InfoActivity::class.java)

                    intent.putExtra("where", "camera")
                    intent.putExtra("camera", result as MilkCowInfoModel)
                    startActivity(intent)

                } else {
                    // 서버통신 실패
                }
            }
            override fun onFailure(call: Call<List<MilkCowInfoModel>>, t: Throwable) {
                // 통신 실패
            }
        })
    }
    override fun onLikedBtnClicked() {
    }

    override fun onSubscribeBtnClicked() {
    }
}

