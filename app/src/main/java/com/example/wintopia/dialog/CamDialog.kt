package com.example.wintopia.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.regist_dialog, null, false)
        camDialog()
    }

    // dialog 표시 함수
    fun camDialog() {
        setContentView(binding.root)
        dialog.window!!.setLayout(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)


        binding.tvRegistDCam.setOnClickListener {
            listener.onClick(1)
            dialog.dismiss()
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

    // activity로 데이터 넘길 때 필요(1)
    private lateinit var listener: CamDialogClickListener
    fun setOnCamDialogClickListener(listener: CamDialogClickListener) {
        this.listener = listener
    }

    // activity로 데이터 넘길 때 필요(2)
    interface CamDialogClickListener {
        fun onClick(req: Int)
    }
}