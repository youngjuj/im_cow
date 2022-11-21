package com.example.wintopia.view.main
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.wintopia.R
import com.example.wintopia.databinding.ActivityMainBinding
import com.example.wintopia.databinding.FragmentCameraBinding
import com.example.wintopia.view.camera.CameraFragment
import com.example.wintopia.view.list.ListFragment
import com.example.wintopia.view.mypage.MyPageFragment

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    val viewModel: MainViewModel by viewModels()
    var show = true

    fun setDataAtFragment(fragment: Fragment, data:Boolean) {
        val bundle = Bundle()
        bundle.putBoolean("data", data)

        fragment.arguments = bundle

        supportFragmentManager.beginTransaction().replace(
            R.id.fl, fragment
        ).commit()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this

        supportFragmentManager.beginTransaction().replace(
            R.id.fl, ListFragment()
        ).commit()
        binding.bnv.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.tab1 -> {
                    Toast.makeText(this, "List화면", Toast.LENGTH_SHORT).show()

                    supportFragmentManager.beginTransaction().replace(
                        R.id.fl, ListFragment()
                    ).commit()
                }
                R.id.tab2 -> {
//                    var intent = Intent(this, CameraFragment::class.java)
//                    intent.putExtra("show", show)
//                    startActivity(intent)


                    Toast.makeText(this, "카메라 화면", Toast.LENGTH_SHORT).show()

                    setDataAtFragment(CameraFragment(), show)
                    show = !show
//                    var fragment = CameraFragment()
//
//                    val bundle = Bundle()
//                    bundle.putString("data", "^^")
//
//                    fragment.arguments = bundle
//
//                    supportFragmentManager.beginTransaction().replace(
//                        R.id.fl, fragment
//                    ).commit()

                }
                R.id.tab3 -> {
                    Toast.makeText(this, "내농장 화면", Toast.LENGTH_SHORT).show()

                    supportFragmentManager.beginTransaction().replace(
                        R.id.fl, MyPageFragment()
                    ).commit()
                }
            }

            true

        }


    }

}