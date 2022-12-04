package com.example.wintopia.view.mypage

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.example.wintopia.R
import com.example.wintopia.data.UserList
import com.example.wintopia.databinding.FragmentMyPageBinding
import com.example.wintopia.view.login.LoginActivity
import com.example.wintopia.view.login.LoginViewModel
import com.example.wintopia.view.main.MainActivity
import com.example.wintopia.view.utilssd.Constants.TAG
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.utils.ColorTemplate

class MyPageFragment : Fragment() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    lateinit var binding: FragmentMyPageBinding
    val viewModel: MyPageViewModel by viewModels()

    private val TAG = this.javaClass.simpleName
    lateinit var lineChart: LineChart
    private val chartData = ArrayList<ChartData>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_page, container, false)
        // Inflate the layout for this fragment

        val pref = context?.getSharedPreferences("userId", 0)
        val edit = pref?.edit() // 수정모드
        val savedCheckBox = pref?.getBoolean("checkBox", false)

        val user_id = UserList().getId().toString()
        Log.d(TAG, "$user_id")
        viewModel.userPageInfo("test")
//        myPageEvent()



        // 서버에서 데이터 가져오기 (서버에서 가져온 데이터로 가정하고 직접 추가)
        chartData.clear()
        addChartItem("1월", 7.9)
        addChartItem("2월", 8.2)
        addChartItem("3월", 8.3)
        addChartItem("4월", 8.5)
        addChartItem("5월", 7.3)

        // 그래프 그릴 자료 넘기기
        LineChartGraph(chartData, "강남")














        binding.btnMyPageLogout.setOnClickListener(){
            edit?.remove("userId")
            edit?.clear()
            edit?.commit()
            Log.d(TAG, "MyPage check 버튼클릭")

            val intent = Intent(requireActivity(), LoginActivity::class.java)
            startActivity(intent)
        }



        return binding.root
    }

    fun myPageEvent() {
        viewModel.myPageCountEvent.observe(requireActivity()){
            when(it){
                "success" ->{
                    Toast.makeText(context, "마이페이지에 오신걸 환영합니다..", Toast.LENGTH_SHORT).show()
                    val intent = Intent(context, MainActivity::class.java)
                    startActivity(intent)
                }
                "fail" -> {
                    Toast.makeText(context, "아이디를 확인해주세요.", Toast.LENGTH_SHORT).show()
                }
                "fail1" ->{
                    Toast.makeText(context, "통신상태가 불량입니다.", Toast.LENGTH_SHORT).show()
                }
                "fail2" ->{
                    Toast.makeText(context, "통신 실패", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }





    private fun addChartItem(lableitem: String, dataitem: Double) {
        val item = ChartData()
        item.lableData = lableitem
        item.valData = dataitem
        chartData.add(item)
    }

    private fun LineChartGraph(chartItem: ArrayList<ChartData>, displayname: String) {
        lineChart = binding.linechart

        val entries = ArrayList<Entry>()
        for (i in chartItem.indices) {
            entries.add(Entry(chartItem[i].valData.toFloat(), i))
        }

        val depenses = LineDataSet(entries, displayname)
        depenses.axisDependency = YAxis.AxisDependency.LEFT
        depenses.valueTextSize = 12f // 값 폰트 지정하여 크게 보이게 하기
        depenses.setColors(ColorTemplate.COLORFUL_COLORS) //
        //depenses.setDrawCubic(true); //선 둥글게 만들기
        depenses.setDrawFilled(false) //그래프 밑부분 색칠

        val labels = ArrayList<String>()
        for (i in chartItem.indices) {
            labels.add(chartItem[i].lableData)
        }

        val dataSets = ArrayList<ILineDataSet>()
        dataSets.add(depenses as ILineDataSet)
        val data = LineData(labels, dataSets) // 라이브러리 v3.x 사용하면 에러 발생함

        lineChart.data = data
        //lineChart.animateXY(1000,1000);
        lineChart.invalidate()
    }

    data class ChartData(
        var lableData: String = "",
        var valData: Double = 0.0
    )

}