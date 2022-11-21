package com.example.wintopia.view.camera

import android.Manifest.permission.CAMERA
import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
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
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.wintopia.R
import com.example.wintopia.databinding.FragmentCameraBinding
import com.example.wintopia.view.main.MainViewModel
import java.lang.Exception
import java.util.jar.Manifest

class CameraFragment : Fragment() {
    private var show:Boolean? = null

    lateinit var binding: FragmentCameraBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_camera, container, false)
        arguments?.let {
            show = it.getBoolean("data") }

        binding.fbCameraCam.setOnClickListener{
            Toast.makeText(requireActivity(), "fbCameraCam", Toast.LENGTH_SHORT).show()
            if(checkPermission()) dispatchTakePictureIntent() else requestPermission()
        }
        Log.v("CF show", show.toString())

        if (show!!) {
            binding.fbCameraCam.visibility = View.VISIBLE
            binding.fbCameraGal.visibility = View.VISIBLE
        } else {
            binding.fbCameraCam.visibility = View.GONE
            binding.fbCameraGal.visibility = View.GONE
        }





            // Inflate the layout for this fragment
            return binding.root
        }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE, CAMERA),
            1)
    }

    private fun checkPermission(): Boolean {

        return (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(requireContext(),
            android.Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED)
    }

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

    private val REQUEST_IMAGE_CAPTURE = 2
    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(requireActivity().packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    private val GALLERY = 3
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == Activity.RESULT_OK) {
            if (requestCode == GALLERY) {
                var imageData: Uri? = data?.data
                Toast.makeText(requireContext(), imageData.toString(), Toast.LENGTH_SHORT).show()
                try {
                    val bitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, imageData)
                    binding.imgCattle.setImageBitmap(bitmap)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            else if (requestCode == REQUEST_IMAGE_CAPTURE) {
                val imageBitmap: Bitmap? = data?.extras?.get("data") as Bitmap
                binding.imgCattle.setImageBitmap(imageBitmap)
            }
        }
    }
}

