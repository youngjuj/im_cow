package com.example.wintopia.view.list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wintopia.R
import com.example.wintopia.databinding.FragmentListBinding

class ListFragment : Fragment() {

<<<<<<< HEAD
    //    lateinit var adapter: ListVOAdapter
    private var mBinding: FragmentListBinding? = null
    private val binding get() = mBinding!!
=======
    
//    lateinit var adapter: ListVOAdapter
//    private var mBinding: FragmentListBinding? = null
//    private val binding get() = mBinding!!
>>>>>>> 96d07cae4b7bbd1a7f6f11c6d711ff10ae14468a

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        mBinding = FragmentListBinding.inflate(inflater, container, false)

        var data = arrayListOf<ListVO>()
        data.add(ListVO("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSrcg48Fej-S3muJwRGLbtfNcWcHwEKKfcbrA&usqp=CAU",
            "분홍얼룩이", "22111001"))

//        binding.rvList.layoutManager = LinearLayoutManager(requireContext())
//        binding.rvList.adapter = ListVOAdapter(data)

<<<<<<< HEAD

        return binding.root
=======
        
        return container
>>>>>>> 96d07cae4b7bbd1a7f6f11c6d711ff10ae14468a
    }

}