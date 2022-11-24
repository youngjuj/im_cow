package com.example.wintopia.view.camera

import android.app.Dialog
import android.content.Intent
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.wintopia.R
import com.example.wintopia.databinding.RegistDialogBinding
import kotlin.properties.Delegates

class CamDialog(private val context: AppCompatActivity) {
    private lateinit var binding : RegistDialogBinding
    private val dialog = Dialog(context)
    var req: Int? = null

    fun camDialog() {
        binding = DataBindingUtil.inflate(context.layoutInflater, R.layout.regist_dialog, null, false)
        dialog.setContentView(binding.root)
        dialog.window!!.setLayout(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)

        binding.tvRegistDCam.setOnClickListener {
            Toast.makeText(context, "사진클릭클릭", Toast.LENGTH_SHORT).show()
            listener.onCamClick()
            Log.v("순서", "camDialog onclick")
            val intent = Intent(context, RegistActivity::class.java)
            req = 1
            intent.putExtra("req", req)
            context.startActivity(intent)
            dialog.dismiss()
        }
        binding.tvRegistDGal.setOnClickListener{
            listener.onGalClick()
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

    private lateinit var listener: CamDialogClickListener
    fun setOnCamDialogClickListener(listener: () -> Unit) {
        this.listener = object : CamDialogClickListener{
            override fun onCamClick() {
                val intent = Intent(context, RegistActivity::class.java)
                req = 1
                intent.putExtra("req", req)
            }

            override fun onGalClick() {
                val intent = Intent(context, RegistActivity::class.java)
                req = 2
                intent.putExtra("req", req)
            }

            override fun onCancelClick() {
                dialog.dismiss()
            }
        }

    }

    interface CamDialogClickListener {
        fun onCamClick()
        fun onGalClick()
        fun onCancelClick()
    }
}