package com.example.wintopia.view.list

import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior.setTag
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.wintopia.R
import com.example.wintopia.databinding.FragmentListBinding
import com.example.wintopia.retrofit.RetrofitClient
import com.example.wintopia.retrofit.RetrofitInterface
import com.example.wintopia.view.regist.RegistActivity
import com.example.wintopia.view.edit.MilkCowInfoModel
import com.example.wintopia.view.utilssd.API_
import com.example.wintopia.view.utilssd.Constants
import com.example.wintopia.view.utilssd.Constants.TAG
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Float.min

class ListFragment : Fragment() {

    private val swipeRefreshLayout: SwipeRefreshLayout by lazy {
        binding.swipeRefreschLayout
    }
    lateinit var binding: FragmentListBinding
    lateinit var data: List<MilkCowInfoModel>


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        cowInfo("test")

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list, container, false)

        binding.fbListRegist.setOnClickListener {
            val intent = Intent(activity, RegistActivity::class.java)
            startActivity(intent)
        }


        return binding.root
    }

    private fun setItemTouchHelper(context: Context) {

        ItemTouchHelper(object : ItemTouchHelper.Callback() {

            private val limitScrollX = dipToPx(140f, context) // 스와이프 범위 제한 140dp
            private var currentScrollX = 0
            private var currentScrollXWhenInActive = 0
            private var initXWhenInActive = 0f
            private var firstInActive = false
            private var currentPosition: Int? = null
            private var previousPosition: Int? = null
            private var currentDx = 0f
            private var clamp = 0f

            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                val dragFlags = 0
                val swipeFlags = ItemTouchHelper.LEFT
                return makeMovementFlags(dragFlags, swipeFlags)
            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            }

            override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
                val isClamped = getTag(viewHolder)
                setTag(viewHolder, !isClamped && currentDx <= -clamp)
                return 2f
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
                    val view = getView(viewHolder)

                    val isClamped = getTag(viewHolder)
                    val x = clampViewPositionHorizontal(view, dX, isClamped,isCurrentlyActive)

                    if(x == -clamp) {
                        getView(viewHolder).animate().translationX(-clamp).setDuration(100L).start()
                        return
                    }

                    currentDx = x
                    getDefaultUIUtil().onDraw(
                        c,
                        recyclerView,
                        view,
                        dX,
                        dY,
                        actionState,
                        isCurrentlyActive
                    )
//                    if (dX == 0f) {
//                        currentScrollX = viewHolder.itemView.scrollX
//                        firstInActive = true
//                    }
                    if (isCurrentlyActive) {
                        // 스와이프하기
                        var scrollOffset = currentScrollX + (-dX).toInt()
                        if(scrollOffset > limitScrollX) {
                            scrollOffset = limitScrollX
                        } else if (scrollOffset < 0) {
                            scrollOffset = 0
                        }
                        viewHolder.itemView.scrollTo(scrollOffset, 0)
//                    } else {
//                         if (firstInActive) {
//                             firstInActive = false
//                             currentScrollXWhenInActive = viewHolder.itemView.scrollX
//                             initXWhenInActive = dX
//                         }

//                        if (viewHolder.itemView.scrollX < limitScrollX) {
//                            viewHolder.itemView.scrollTo((currentScrollXWhenInActive * dX / initXWhenInActive).toInt(), 0)
//                        }
                    }
                }
            }

            private fun clampViewPositionHorizontal(
                view: View,
                dX: Float,
                isClamped: Boolean,
                isCurrentlyActive: Boolean
            ): Float {
                val max: Float = 0f

                val x = if(isClamped) {
                    if (isCurrentlyActive) dX - clamp else -clamp
                } else {
                    dX / 2
                }
                return min(x, max)
            }

            private fun setTag(viewHolder: RecyclerView.ViewHolder, isClamped: Boolean) {
                viewHolder.itemView.tag = isClamped
            }
            private fun getTag(viewHolder: RecyclerView.ViewHolder): Boolean {
                return viewHolder.itemView.tag as? Boolean ?: false
            }

            fun setClamp(clamp: Float) {
                this.clamp = clamp
            }

            private fun getView(viewHolder: RecyclerView.ViewHolder): View {
                return (viewHolder as ListVOAdapter.ListVOViewHolder).itemView.rootView
            }

            fun removePreviousClamp(recyclerView: RecyclerView) {
                if(currentPosition == previousPosition)
                    return
                previousPosition?.let {
                    val viewHolder = recyclerView.findViewHolderForAdapterPosition(it) ?: return
                    getView(viewHolder).translationX = 0f
                    setTag(viewHolder, false)
                }
            }

            override fun clearView(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ) {

                currentDx = 0f
                getDefaultUIUtil().clearView(view)
                previousPosition = viewHolder.adapterPosition

//                super.clearView(recyclerView, viewHolder)

//                if (viewHolder.itemView.scrollX > limitScrollX) {
//                    viewHolder.itemView.scrollTo(limitScrollX, 0)
//                } else if (viewHolder.itemView.scrollX < 0) {
//                    viewHolder.itemView.scrollTo(0, 0)
//                }
            }

            override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
                viewHolder?.let {
                    currentPosition = viewHolder.adapterPosition
                    getDefaultUIUtil().onSelected(view)
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

        call?.enqueue(object : Callback<MutableList<MilkCowInfoModel>> {
            override fun onResponse(call: Call<MutableList<MilkCowInfoModel>>, response: Response<MutableList<MilkCowInfoModel>>) {
                if (response.isSuccessful) {
                    Log.d(TAG,""+response?.body().toString())
                    data = response?.body()!!

                    val listAdapter = ListVOAdapter(data as MutableList<MilkCowInfoModel>)

                    listAdapter.reload(data)

                    binding.rvList.apply {
                        layoutManager = LinearLayoutManager(context)
                        adapter = listAdapter
                        addItemDecoration(ItemDecoration())

                        swipeRefreshLayout.setOnRefreshListener {
                            swipeRefreshLayout.isRefreshing = false
                        }

                        setItemTouchHelper(context)


                    }
//                    Log.d("로그 ",res)
//                    Toast.makeText(requireActivity(),"통신성공", Toast.LENGTH_LONG).show()
                } else {
//                    Toast.makeText(requireActivity(),"통신실패", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<MutableList<MilkCowInfoModel>>, t: Throwable) {
                Log.d("로그",t.message.toString())
            }
        })
    }

}