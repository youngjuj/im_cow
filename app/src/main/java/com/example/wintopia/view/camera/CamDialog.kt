package com.example.wintopia.view.camera

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.wintopia.R
import com.example.wintopia.databinding.RegistDialogBinding

class CamDialog(
    context: Context,
) : Dialog(context) {
    private lateinit var binding : RegistDialogBinding

    private val dialog = Dialog(context)
    var req: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.regist_dialog, null, false)
//        setContentView(binding.root)
        camDialog()
    }

    fun camDialog() {
//        binding = DataBindingUtil.inflate(layoutInflater, R.layout.regist_dialog, null, false)
        setContentView(binding.root)
        dialog.window!!.setLayout(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)


        binding.tvRegistDCam.setOnClickListener {
            listener.onClick(1)
            dialog.dismiss()
//            req = 1
//            val intent = Intent()
//            intent.putExtra("req", req)
//            context.startActivity(intent)
            Toast.makeText(context, "사진클릭클릭", Toast.LENGTH_SHORT).show()
//            listener.onCamClick()
            Log.v("순서", "camDialog onclick")


        }
        binding.tvRegistDGal.setOnClickListener{
            listener.onClick(2)
            dialog.dismiss()
        }
        binding.tvRegistDCancel.setOnClickListener{
            listener.onClick(3)
            dialog.dismiss()
        }
        dialog.dismiss()
    }

    private lateinit var listener: CamDialogClickListener
    fun setOnCamDialogClickListener(listener: CamDialogClickListener) {
        this.listener = listener
    }

    interface CamDialogClickListener {

        fun onClick(req: Int)
    }


//    fun setDataAtActivity(activity: Activity, req: Int) {
//        val bundle = Bundle()
//        bundle.putInt("req", req)
//        activity
//    }

}