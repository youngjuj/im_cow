package com.example.wintopia.view.camera

import android.Manifest.permission.CAMERA
import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.wintopia.R
import com.example.wintopia.databinding.FragmentCameraBinding
import com.example.wintopia.view.main.MainViewModel
import java.io.File
import java.io.IOException
import java.lang.Exception
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.jar.Manifest

class CameraFragment : Fragment() {
    private var show:Boolean? = null
    private val REQUEST_IMAGE_CAPTURE = 1
    private val GALLERY = 2
    lateinit var currentPhotoPath: String


    lateinit var binding: FragmentCameraBinding

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
            show = it.getBoolean("data") }

        // BottomNavi multipleClick options
        if (show!!) {
            binding.fbCameraCam.visibility = View.VISIBLE
            binding.fbCameraGal.visibility = View.VISIBLE
        } else {
            binding.fbCameraCam.visibility = View.GONE
            binding.fbCameraGal.visibility = View.GONE
        }

        // camera floatting button onClickListener
        binding.fbCameraCam.setOnClickListener{
            Toast.makeText(requireActivity(), "fbCameraCam", Toast.LENGTH_SHORT).show()
            if(checkPermission()) dispatchTakePictureIntent() else requestPermission()
        }


            // Inflate the layout for this fragment
            return binding.root
        }

    // camera 접근 권한 요청
    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE, CAMERA),
            REQUEST_IMAGE_CAPTURE)
    }

    // camera 접근 권한 체크
    private fun checkPermission(): Boolean {

        return (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(requireContext(),
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
            Toast.makeText(requireContext(), "권한 설정 완료", Toast.LENGTH_SHORT).show()
        }
        else {
            Toast.makeText(requireContext(), "권한 설정 실패", Toast.LENGTH_SHORT).show()
        }
    }

//    private fun dispatchTakePictureIntent() {
//        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
//            takePictureIntent.resolveActivity(requireActivity().packageManager).also {
//                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
//            }
//        }
//    }

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
                            if(photoFile != null) {
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

    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(java.util.Date())
        val storageDir: File? = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply { currentPhotoPath = absolutePath }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            REQUEST_IMAGE_CAPTURE -> {
                if(resultCode == Activity.RESULT_OK) {
                    val file = File(currentPhotoPath)
                    if (Build.VERSION.SDK_INT < 28) {
                        val bitmap = MediaStore.Images.Media
                            .getBitmap(requireActivity().contentResolver, Uri.fromFile(file))
                        binding.imgCameraPic.setImageBitmap(bitmap)
                    } else {
                        val decode = ImageDecoder.createSource(requireActivity().contentResolver,
                        Uri.fromFile(file))
                        val bitmap = ImageDecoder.decodeBitmap(decode)
                        binding.imgCameraPic.setImageBitmap(bitmap)
                    }
                }
            }
        }

//        if(requestCode == Activity.RESULT_OK) {
//            if (requestCode == GALLERY) {
//                var imageData: Uri? = data?.data
//                Toast.makeText(requireContext(), imageData.toString(), Toast.LENGTH_SHORT).show()
//                try {
//                    val bitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, imageData)
//                    binding.imgCameraPic.setImageBitmap(bitmap)
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                }
//            }
//            else if (requestCode == REQUEST_IMAGE_CAPTURE) {
//                val imageBitmap: Bitmap? = data?.extras?.get("data") as Bitmap
//                binding.imgCameraPic.setImageBitmap(imageBitmap)
//            }
//        }
    }
}

