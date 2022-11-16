package com.example.wintopia.view.list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
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

        binding.rvList.layoutManager = LinearLayoutManager(requireContext())
        binding.rvList.adapter = ListVOAdapter(data)

        return binding.root
    }

}