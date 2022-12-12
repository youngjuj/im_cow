package com.example.wintopia.view.list

import android.annotation.SuppressLint
import android.content.Intent
import android.media.SoundPool
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.wintopia.R
import com.example.wintopia.databinding.ActivityMainBinding
import com.example.wintopia.databinding.FragmentListBinding
import com.example.wintopia.databinding.FragmentWishBinding
import com.example.wintopia.retrofit.RetrofitClient
import com.example.wintopia.retrofit.RetrofitInterface
import com.example.wintopia.utils.SwipeHelperCallback
import com.example.wintopia.view.regist.RegistActivity
import com.example.wintopia.view.edit.MilkCowInfoModel
import com.example.wintopia.view.utilssd.API_
import com.example.wintopia.view.utilssd.Constants
import com.example.wintopia.view.utilssd.Constants.TAG
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WishFragment : Fragment() {

    private val swipeRefreshLayout: SwipeRefreshLayout by lazy {
        binding.swipeRefreschLayoutWish
    }
    lateinit var binding: FragmentWishBinding
    lateinit var data: List<MilkCowInfoModel>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        cowInfoWish("test")

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_wish, container, false)

    return binding.root
}


        // 서버에서 전체 정보 가져오기
        fun cowInfoWish(userId: String) {
            //Retrofit 인스턴스 생성
            val retrofit = RetrofitClient.getInstnace(API_.BASE_URL)
            val service = retrofit.create(RetrofitInterface::class.java) // 레트로핏 인터페이스 객체 구현


            val call = service.cowInfoWish(userId) //통신 API 패스 설정

            call?.enqueue(object : Callback<MutableList<MilkCowInfoModel>> {
                @SuppressLint("ClickableViewAccessibility")
                override fun onResponse(
                    call: Call<MutableList<MilkCowInfoModel>>,
                    response: Response<MutableList<MilkCowInfoModel>>
                ) {
                    if (response.isSuccessful) {
                        Log.d(TAG, "" + response?.body().toString())
                        data = response?.body()!!

                        val listAdapter = ListVOAdapter(data as MutableList<MilkCowInfoModel>)

                        listAdapter.reload(data)

                        binding.rvWish.apply {
                            layoutManager = LinearLayoutManager(context)
                            adapter = listAdapter

                            swipeRefreshLayout.setOnRefreshListener {
                                cowInfoWish(userId)
                                swipeRefreshLayout.isRefreshing = false
                            }

                            var swipeHelperCallback = SwipeHelperCallback().apply {
                                setClamp(200f)
                            }
                            val itemTouchHelper = ItemTouchHelper(swipeHelperCallback)
                            itemTouchHelper.attachToRecyclerView(binding.rvWish)
                            setOnTouchListener { v, event ->
                                swipeHelperCallback.removePreviousClamp(binding.rvWish)
                                false
                            }
                        }

                    } else {
                    }
                }

                override fun onFailure(call: Call<MutableList<MilkCowInfoModel>>, t: Throwable) {
                    Log.d("로그", t.message.toString())
                }
            })}
        }


