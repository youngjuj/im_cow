package com.example.wintopia.view.camera

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.wintopia.R
import com.example.wintopia.databinding.FragmentCameraBinding
import com.example.wintopia.view.main.MainViewModel

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
    }

