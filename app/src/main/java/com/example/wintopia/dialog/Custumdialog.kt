package com.example.wintopia.dialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import com.example.wintopia.databinding.ActivityCustomDialogBinding
import com.example.wintopia.databinding.FragmentCustomDialog1Binding

class Custumdialog (context: Context, MyCustomDialogInterface: MyCustomDialogInterface) : Dialog(context) {
    private var mBinding: ActivityCustomDialogBinding? = null
    private val binding get() = mBinding!!

    val image = String
    val image2 = String
    val image3 = String

    private var myCustomDialogInterface: MyCustomDialogInterface? = null

    // 인터페이스 연결
    init {
        this.myCustomDialogInterface = MyCustomDialogInterface
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityCustomDialogBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 배경 투명하게
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

//        binding.subscribeBtn.setOnClickListener {
//            this.myCustomDialogInterface?.onSubscribeBtnClicked()
//
//        }
//        binding.likeBtn.setOnClickListener {
//            this.myCustomDialogInterface?.onLikedBtnClicked()
//        }
    }
}