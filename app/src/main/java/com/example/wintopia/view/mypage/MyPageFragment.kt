package com.example.wintopia.view.mypage

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.wintopia.R
import com.example.wintopia.data.UserList
import com.example.wintopia.databinding.FragmentMyPageBinding
import com.example.wintopia.view.login.LoginActivity
import com.example.wintopia.view.main.MainActivity
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.*
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import java.util.*
import kotlin.collections.ArrayList


class MyPageFragment : Fragment() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    lateinit var binding: FragmentMyPageBinding
    val viewModel: MyPageViewModel by viewModels()

    private val TAG = this.javaClass.simpleName
    lateinit var lineChart: LineChart
    //선 그래프
//    private var lineChart: LineChart? = null
//    private val chartData = ArrayList<ChartData>()
    val entryChart1 = ArrayList<Entry>() // 데이터를 담을 Arraylist
    val entryChart2 = ArrayList<Entry>()
    val entryChart3 = ArrayList<Entry>()
    val entryChart4 = ArrayList<Entry>()

    val chartData = LineData() // 차트에 담길 데이터

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_page, container, false)
        // Inflate the layout for this fragment

//        binding.linearLayout.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
//            if (scrollY <= -10) requireActivity().window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.beige)
//
//        }
        val pref = context?.getSharedPreferences("userId", 0)
        val edit = pref?.edit() // 수정모드
        val savedCheckBox = pref?.getBoolean("checkBox", false)

        val user_id = UserList().getId().toString()
        Log.d(TAG, "$user_id")
        viewModel.userPageInfo("test")
//        myPageEvent()
        observeData()


        barChartSetting()
        lineChart = binding.linechart
        lineEvent()

        // 차트에 위의 DataSet을 넣는다.
        lineChart.data = chartData
        // 차트 업데이트
        lineChart.invalidate()
        // 차트 터치 disable
        lineChart.setTouchEnabled(true)



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

    fun observeData() {
        viewModel.myfarm.observe(requireActivity()) {
            binding.tvMyPageMyfarm.text = it
        }
        viewModel.myPhone.observe(requireActivity()) {
            binding.tvMyPhone.text = it
        }
        viewModel.myCowCount.observe(requireActivity()) {
            binding.tvMypageCount.text = it
        }
        viewModel.cowBaby.observe(requireActivity()) {
            binding.tvMypageBaby.text = it
        }
        viewModel.myCowBull.observe(requireActivity()) {
            binding.tvMypageGender.text = it
        }

    }


        data class ChartData(
        var lableData: String = "",
        var valData: Double = 0.0
    )

    fun lineEvent(){

        //entryChart에 좌표 데이터를 담는다.
        var xValueList = arrayOf<Float>(0f, 1f, 2f, 3f, 4f, 5f)

        var yValueList1 = arrayOf<Float>(25f, 28f, 29f, 29f, 30f, 31f)
        var yValueList2 = arrayOf<Float>(4f, 5f, 6f, 6f, 7f, 5f)
        var yValueList3 = arrayOf<Float>(3f, 5f, 7f, 7f, 8f, 6f)
        var yValueList4 = arrayOf<Float>(7f, 9f, 9f, 10f, 11f, 11f)

        var yValueName = arrayOf(entryChart1, entryChart2, entryChart3, entryChart4)
        var yValueList = arrayOf(yValueList1, yValueList2, yValueList3, yValueList4)
        for (a: Int in 0..3){
            for (i: Int in 0..5) yValueName[a].add(Entry(xValueList[i], yValueList[a][i]))
        }

        // 데이터가 담긴 Arraylist 를 LineDataSet 으로 변환한다.
        val lineDataSet1 =
            LineDataSet(entryChart1, "전체개체")
        val lineDataSet2 =
            LineDataSet(entryChart2, "임신개체")
        val lineDataSet3 =
            LineDataSet(entryChart3, "건유개체")
        val lineDataSet4 =
            LineDataSet(entryChart4, "거세개체")

        val lineDataSet = arrayOf(lineDataSet1, lineDataSet2, lineDataSet3, lineDataSet4)
        val color = mutableListOf<Int>(
            Color.rgb(255, 155, 155),
            Color.rgb(166, 208, 227),
            Color.rgb(178, 223, 138),
            Color.rgb(31, 120, 180))

        // 해당 LineDataSet의 색 설정 :: 각 Line 과 관련된 세팅은 여기서 설정한다.
        for (i: Int in 0..3){
            lineDataSet[i].color = (color[i])
            lineDataSet[i].circleColors = mutableListOf(color[i])
            lineDataSet[i].circleRadius = 6f
            lineDataSet[i].lineWidth = 4f
            lineDataSet[i].setDrawValues(false)
            lineDataSet[i].setDrawCircleHole(true)
            lineDataSet[i].setDrawCircles(true)
            lineDataSet[i].setDrawHorizontalHighlightIndicator(false)
            lineDataSet[i].setDrawHighlightIndicators(false)
        }


        // 해당 LineDataSet 을 적용될 차트에 들어갈 DataSet 에 넣는다.
        for (i: Int in 0..3) chartData.addDataSet(lineDataSet[i])

        //바텀 좌표 값
        val xAxis: XAxis = lineChart.xAxis
        //change the position of x-axis to the bottom
        xAxis.position = XAxis.XAxisPosition.TOP
        //set the horizontal distance of the grid line
        xAxis.granularity = 1f
        xAxis.textColor = Color.BLACK
        //hiding the x-axis line, default true if not set
        xAxis.setDrawAxisLine(false)
        //hiding the vertical grid lines, default true if not set
        xAxis.setDrawGridLines(false)
        // x축 라벨
        val yAxisVals = ArrayList<String>(Arrays.asList("6월", "7월", "8월", "9월", "10월", "11월"))
        var yLAxis = lineChart.xAxis
        yLAxis.valueFormatter = IndexAxisValueFormatter(yAxisVals)
        yLAxis.granularity = 1f

        // y축 오른쪽 비활성화
        var yRAxis = lineChart.axisRight
        yRAxis.setDrawLabels(false)
        yRAxis.setDrawAxisLine(false)
        yRAxis.setDrawGridLines(false)
    }


    fun barChartSetting(){

        val barEntries1 = ArrayList<BarEntry>()
        val barEntries2 = ArrayList<BarEntry>()
        val barEntries3 = ArrayList<BarEntry>()
        val barEntries4 = ArrayList<BarEntry>()

        val barEntries = arrayOf(barEntries1, barEntries2, barEntries3, barEntries4)

        var xValueList = arrayOf<Float>(0f, 1f, 2f, 3f, 4f, 5f)

        var yValueList1 = arrayOf<Float>(25f, 28f, 29f, 29f, 30f, 31f)
        var yValueList2 = arrayOf<Float>(4f, 5f, 6f, 6f, 7f, 5f)
        var yValueList3 = arrayOf<Float>(3f, 5f, 7f, 7f, 8f, 6f)
        var yValueList4 = arrayOf<Float>(7f, 9f, 9f, 10f, 11f, 11f)

        var yValueList = arrayOf(yValueList1, yValueList2, yValueList3, yValueList4)

        //entryChart1에 좌표 데이터를 담는다.
        for (a: Int in 0..3){
            for (i: Int in 0..5) barEntries[a].add(BarEntry(xValueList[i], yValueList[a][i]))
        }

        val barDataSet1 = BarDataSet(barEntries1, "전체개체")
        val barDataSet2 = BarDataSet(barEntries2, "임신개체")
        val barDataSet3 = BarDataSet(barEntries3, "건유개체")
        val barDataSet4 = BarDataSet(barEntries4, "거세개체")

        val barDataSet = arrayOf(barDataSet1, barDataSet2, barDataSet3, barDataSet4)
        val color = mutableListOf<Int>(
            Color.rgb(255, 155, 155),
            Color.rgb(166, 208, 227),
            Color.rgb(178, 223, 138),
            Color.rgb(31, 120, 180))

        for (i: Int in 0..3) barDataSet[i].color = (color[i])


        val months = arrayOf("8월", "9월", "10월", "11월")
        val data = BarData(barDataSet1, barDataSet2, barDataSet3, barDataSet4)
        var barChart = binding.chartWeek
        barChart.data = data

        val xAxis: XAxis = barChart.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(months)
        barChart.axisLeft.axisMinimum = 0f
        barChart.axisLeft.axisMaximum = 35f
        xAxis.position = XAxis.XAxisPosition.TOP
        xAxis.granularity = 1f
        xAxis.setCenterAxisLabels(true)
        xAxis.isGranularityEnabled = true

        // y축 오른쪽 비활성화
        var yLAxis = barChart.axisLeft
        yLAxis.setDrawGridLines(false)
        yLAxis.setSpaceTop(30f)
        yLAxis.setAxisMinValue(0f) // this replaces setStartAtZero(true
        barChart.axisRight.isEnabled = false

        val barSpace = 0.02f
        val groupSpace = 0.3f
        val groupCount = 4

        //IMPORTANT *****

        //IMPORTANT *****
        data.barWidth = 0.15f
        barChart.getXAxis().setAxisMinimum(0f)
        barChart.getXAxis().setAxisMaximum(
            0 + barChart.getBarData().getGroupWidth(groupSpace, barSpace) * groupCount
        )
        barChart.groupBars(0f, groupSpace, barSpace) // perform the "explicit" grouping

        //***** IMPORTANT

    }




}