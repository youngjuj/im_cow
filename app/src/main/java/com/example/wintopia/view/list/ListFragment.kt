package com.example.wintopia.view.list

import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.wintopia.R
import com.example.wintopia.data.UserList
import com.example.wintopia.databinding.FragmentListBinding
import com.example.wintopia.retrofit.RetrofitClient
import com.example.wintopia.retrofit.RetrofitInterface
import com.example.wintopia.view.camera.RegistActivity
import com.example.wintopia.view.edit.CowInfo
import com.example.wintopia.view.edit.MilkCowInfoModel
import com.example.wintopia.view.info.InfoActivity
import com.example.wintopia.view.utilssd.API_
import com.example.wintopia.view.utilssd.Constants
import com.google.gson.Gson
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.converter.gson.GsonConverterFactory

class ListFragment : Fragment() {

    private val swipeRefreshLayout: SwipeRefreshLayout by lazy {
        binding.swipeRefreschLayout
    }
    lateinit var binding: FragmentListBinding
    lateinit var data: MutableList<ListVO>


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)


        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list, container, false)
        // Inflate the layout for this fragment
//        mBinding = FragmentListBinding.inflate(inflater, container, false)


        cowInfo("test")
//        data.add(ListVO("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSrcg48Fej-S3muJwRGLbtfNcWcHwEKKfcbrA&usqp=CAU",
//            "분홍얼룩이", "22111001"))
//        data.add(
//            ListVO("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSrcg48Fej-S3muJwRGLbtfNcWcHwEKKfcbrA&usqp=CAU",
//                "검정얼룩이", "22111002"))
//        data.add(
//            ListVO("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSrcg48Fej-S3muJwRGLbtfNcWcHwEKKfcbrA&usqp=CAU",
//                "푸른얼룩이", "22111003"))




        binding.fbListRegist.setOnClickListener {
            val intent = Intent(requireActivity(), RegistActivity::class.java)
            startActivity(intent)
        }


        return binding.root
    }

    private fun setItemTouchHelper() {

        ItemTouchHelper(object : ItemTouchHelper.Callback() {

            private val limitScrollX = dipToPx(140f, requireContext()) // 스와이프 범위 제한 140dp
            private var currentScrollX = 0
            private var currentScrollXWhenInActive = 0
            private var initXWhenInActive = 0f
            private var firstInActive = false

            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                val dragFlags = 0
                val swipeFlags = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
                return makeMovementFlags(dragFlags, swipeFlags)
            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            }

            override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
                return Integer.MAX_VALUE.toFloat()
            }

            override fun getSwipeVelocityThreshold(defaultValue: Float): Float {
                return Integer.MAX_VALUE.toFloat()
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    if (dX == 0f) {
                        currentScrollX = viewHolder.itemView.scrollX
                        firstInActive = true
                    }
                    if (isCurrentlyActive) {
                        // 스와이프하기
                        var scrollOffset = currentScrollX + (-dX).toInt()
                        if(scrollOffset > limitScrollX) {
                            scrollOffset = limitScrollX
                        } else if (scrollOffset < 0) {
                            scrollOffset = 0
                        }
                        viewHolder.itemView.scrollTo(scrollOffset, 0)
                    } else {
                         if (firstInActive) {
                             firstInActive = false
                             currentScrollXWhenInActive = viewHolder.itemView.scrollX
                             initXWhenInActive = dX
                         }

                        if (viewHolder.itemView.scrollX < limitScrollX) {
                            viewHolder.itemView.scrollTo((currentScrollXWhenInActive * dX / initXWhenInActive).toInt(), 0)
                        }
                    }
                }
            }

            override fun clearView(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ) {
                super.clearView(recyclerView, viewHolder)

                if (viewHolder.itemView.scrollX > limitScrollX) {
                    viewHolder.itemView.scrollTo(limitScrollX, 0)
                } else if (viewHolder.itemView.scrollX < 0) {
                    viewHolder.itemView.scrollTo(0, 0)
                }
            }
        }).apply {
            attachToRecyclerView(binding.rvList)
        }
    }

    private fun dipToPx(dipValue: Float, context: Context): Int {
        return (dipValue * context.resources.displayMetrics.density).toInt()
    }

    // 서버에서 전체 정보 가져오기
    fun  cowInfo(userId: String) {
        Log.d(Constants.TAG,"웹서버로 이미지전송")

        //Retrofit 인스턴스 생성
        val retrofit = RetrofitClient.getInstnace(API_.BASE_URL)
//        val retrofit = Retrofit.Builder().baseUrl(API_.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build()
        val service = retrofit.create(RetrofitInterface::class.java) // 레트로핏 인터페이스 객체 구현


        val call = service.cowListAll(userId) //통신 API 패스 설정

        call?.enqueue(object : Callback<MutableList<ListVO>> {
            override fun onResponse(call: Call<MutableList<ListVO>>, response: Response<MutableList<ListVO>>) {
                if (response.isSuccessful) {
//                    Log.d("로그 ",""+response?.body().toString())
                    data = response.body()!!

                    val listAdapter = ListVOAdapter(data)

                    listAdapter.reload(data)

                    binding.rvList.apply {
                        layoutManager = LinearLayoutManager(requireContext())
                        adapter = listAdapter
                        addItemDecoration(ItemDecoration())

                        swipeRefreshLayout.setOnRefreshListener {
                            swipeRefreshLayout.isRefreshing = false
                        }

                        setItemTouchHelper()

//
                    }
//                    Log.d("로그 ",res)
                    Toast.makeText(requireActivity(),"통신성공", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireActivity(),"통신실패", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<MutableList<ListVO>>, t: Throwable) {
                Log.d("로그",t.message.toString())
            }
        })
    }

}