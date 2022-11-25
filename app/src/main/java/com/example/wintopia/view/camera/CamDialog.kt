package com.example.wintopia.view.camera

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Context
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
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import com.example.wintopia.R
import com.example.wintopia.databinding.RegistDialogBinding
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import kotlin.properties.Delegates

class CamDialog(
    context: Context,
) : Dialog(context) {
    private lateinit var binding : RegistDialogBinding

    private val dialog = Dialog(context)
    var req: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.regist_dialog, null, false)
        setContentView(binding.root)
        camDialog()
    }

    fun camDialog() {
//        dialog.setContentView(binding.root)
        dialog.window!!.setLayout(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)


        binding.tvRegistDCam.setOnClickListener {
            req = 1
            val intent = Intent()
            intent.putExtra("req", req)
            context.startActivity(intent)
            Toast.makeText(context, "사진클릭클릭", Toast.LENGTH_SHORT).show()
//            listener.onCamClick()
            Log.v("순서", "camDialog onclick")

            dialog.dismiss()
        }
        binding.tvRegistDGal.setOnClickListener{
//            listener.onGalClick()
            val intent = Intent(context, RegistActivity::class.java)
            req = 2
            intent.putExtra("req", req)
            context.startActivity(intent)
            dialog.dismiss()
        }
        binding.tvRegistDCancel.setOnClickListener{
            dialog.dismiss()
        }
        dialog.show()
    }

//    private lateinit var listener: CamDialogClickListener
//    fun setOnCamDialogClickListener(listener: () -> Unit) {
//        this.listener = object : CamDialogClickListener{
//            override fun onCamClick() {
//                val intent = Intent(context, RegistActivity::class.java)
//                req = 1
//                intent.putExtra("req", req)
//            }
//
//            override fun onGalClick() {
//                val intent = Intent(context, RegistActivity::class.java)
//                req = 2
//                intent.putExtra("req", req)
//            }
//
//            override fun onCancelClick() {
//                dialog.dismiss()
//            }
//        }
//
//    }

//    interface CamDialogClickListener {
//        fun onCamClick()
//        fun onGalClick()
//        fun onCancelClick()
//    }
//


}