package com.example.wintopia.view.list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.TranslateAnimation
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wintopia.R
import com.example.wintopia.databinding.FragmentListBinding

class ListFragment : Fragment() {

    lateinit var binding: FragmentListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)


        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list, container, false)
        // Inflate the layout for this fragment
//        mBinding = FragmentListBinding.inflate(inflater, container, false)

        var data = arrayListOf<ListVO>()
        data.add(ListVO("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSrcg48Fej-S3muJwRGLbtfNcWcHwEKKfcbrA&usqp=CAU",
            "분홍얼룩이", "22111001"))


        val listAdapter = ListVOAdapter(data)

        val swipeController = SwipeController().apply { setClamp(200f) }
        val itemTouchHelper = ItemTouchHelper(swipeController)
        itemTouchHelper.attachToRecyclerView(binding.rvList)

        binding.rvList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = listAdapter
            addItemDecoration(ItemDecoration())

            setOnTouchListener { _, _ ->
                swipeController.removePreviousClamp(this)
                false
            }
        }
        return binding.root
    }
}