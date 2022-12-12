package com.example.wintopia.view.main
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.TypedValue
import android.util.TypedValue.COMPLEX_UNIT_DIP
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
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
import com.google.android.material.bottomnavigation.BottomNavigationView

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
                    supportFragmentManager.beginTransaction().replace(
                        R.id.fl, ListFragment()
                    ).commit()
                    binding.ic.root.isVisible = true
                    window.statusBarColor = ContextCompat.getColor(this, R.color.beige)
                    val sampleDp = 770
                    val dpi = resources.displayMetrics.densityDpi
                    val value = (sampleDp * dpi).toInt()

                    binding.fl.layoutParams.height = value
                }
                R.id.tab2 -> {
                    setDataAtFragment(CameraFragment(), show)
                    show = !show
                    binding.ic.root.isVisible = true
                    window.statusBarColor = ContextCompat.getColor(this, R.color.beige)
                    val sampleDp = 770
                    val dpi = resources.displayMetrics.densityDpi
                    val value = (sampleDp * dpi).toInt()
                    binding.fl.layoutParams.height = value
                }
                R.id.tab3 -> {
                    supportFragmentManager.beginTransaction().replace(
                        R.id.fl, MyPageFragment()
                    ).commit()
                    binding.ic.root.isVisible = false
                    window.statusBarColor = ContextCompat.getColor(this, R.color.myPage)
                    val sampleDp = 830
                    val dpi = resources.displayMetrics.densityDpi
                    val value = (sampleDp * dpi).toInt()
                    binding.fl.layoutParams.height = value
                }
            }

            true

        }


    }


}