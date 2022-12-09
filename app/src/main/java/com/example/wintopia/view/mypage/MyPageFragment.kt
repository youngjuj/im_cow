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

        val pref = context?.getSharedPreferences("userId", 0)
        val edit = pref?.edit() // 수정모드
        val savedCheckBox = pref?.getBoolean("checkBox", false)

        val user_id = UserList().getId().toString()
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

        var xValueList = arrayOf<Float>(0f, 1f, 2f, 3f, 4f, 5f)
        var yValueList1 = arrayOf<Float>(25f, 28f, 29f, 29f, 30f, 31f)
        //entryChart1에 좌표 데이터를 담는다.
        for (i in xValueList){
            entryChart1.add(Entry(xValueList[i.toInt()], yValueList1[i.toInt()]))
        }

        var yValueList2 = arrayOf<Float>(4f, 5f, 6f, 6f, 7f, 5f)
        //entryChart2에 좌표 데이터를 담는다.
        for (i in xValueList){
            entryChart2.add(Entry(xValueList[i.toInt()], yValueList2[i.toInt()]))
        }

        //entryChart3에 좌표 데이터를 담는다.
        var yValueList3 = arrayOf<Float>(3f, 5f, 7f, 7f, 8f, 6f)
        for (i in xValueList){
            entryChart3.add(Entry(xValueList[i.toInt()], yValueList3[i.toInt()]))
        }

        //entryChart4에 좌표 데이터를 담는다.
        var yValueList4 = arrayOf<Float>(7f, 9f, 9f, 10f, 11f, 11f)
        for (i in xValueList){
            entryChart4.add(Entry(xValueList[i.toInt()], yValueList4[i.toInt()]))
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

        // 해당 LineDataSet의 색 설정 :: 각 Line 과 관련된 세팅은 여기서 설정한다.
        lineDataSet1.color = Color.rgb(255, 155, 155)
        lineDataSet1.circleColors = mutableListOf(Color.rgb(255, 155, 155))
        lineDataSet1.circleRadius = 6f
        lineDataSet1.lineWidth = 4f
        lineDataSet1.setDrawValues(false)
        lineDataSet1.setDrawCircleHole(true)
        lineDataSet1.setDrawCircles(true)
        lineDataSet1.setDrawHorizontalHighlightIndicator(false)
        lineDataSet1.setDrawHighlightIndicators(false)

        lineDataSet2.color = Color.rgb(178, 223, 138)
        lineDataSet2.circleColors = mutableListOf(Color.rgb(178, 223, 138))
        lineDataSet2.circleRadius = 6f
        lineDataSet2.lineWidth = 4f
        lineDataSet2.setDrawValues(false)
        lineDataSet2.setDrawCircleHole(true)
        lineDataSet2.setDrawCircles(true)
        lineDataSet2.setDrawHorizontalHighlightIndicator(false)
        lineDataSet2.setDrawHighlightIndicators(false)

        lineDataSet3.color = Color.rgb(166, 208, 227)
        lineDataSet3.circleColors = mutableListOf(Color.rgb(166, 208, 227))
        lineDataSet3.circleRadius = 6f
        lineDataSet3.lineWidth= 4f
        lineDataSet3.setDrawValues(false)
        lineDataSet3.setDrawCircleHole(true)
        lineDataSet3.setDrawCircles(true)
        lineDataSet3.setDrawHorizontalHighlightIndicator(false)
        lineDataSet3.setDrawHighlightIndicators(false)

        lineDataSet4.color = Color.rgb(31, 120, 180)
        lineDataSet4.circleColors = mutableListOf(Color.rgb(31, 120, 180))
        lineDataSet4.circleRadius = 6f
        lineDataSet4.lineWidth= 4f
        lineDataSet4.setDrawValues(false)
        lineDataSet4.setDrawCircleHole(true)
        lineDataSet4.setDrawCircles(true)
        lineDataSet4.setDrawHorizontalHighlightIndicator(false)
        lineDataSet4.setDrawHighlightIndicators(false)

        // 해당 LineDataSet 을 적용될 차트에 들어갈 DataSet 에 넣는다.
        chartData.addDataSet(lineDataSet1)
        chartData.addDataSet(lineDataSet2)
        chartData.addDataSet(lineDataSet3)
        chartData.addDataSet(lineDataSet4)

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

        var xValueList = arrayOf<Float>(0f, 1f, 2f, 3f, 4f, 5f)
        var yValueList1 = arrayOf<Float>(25f, 28f, 29f, 29f, 30f, 31f)
        //entryChart1에 좌표 데이터를 담는다.
        for (i in xValueList){
            barEntries1.add(BarEntry(xValueList[i.toInt()], yValueList1[i.toInt()]))
        }

        var yValueList2 = arrayOf<Float>(4f, 5f, 6f, 6f, 7f, 5f)
        for (i in xValueList){
            barEntries2.add(BarEntry(xValueList[i.toInt()], yValueList2[i.toInt()]))
        }

        var yValueList3 = arrayOf<Float>(3f, 5f, 7f, 7f, 8f, 6f)
        for (i in xValueList){
            barEntries3.add(BarEntry(xValueList[i.toInt()], yValueList3[i.toInt()]))
        }

        var yValueList4 = arrayOf<Float>(7f, 9f, 9f, 10f, 11f, 11f)
        for (i in xValueList){
            barEntries4.add(BarEntry(xValueList[i.toInt()], yValueList4[i.toInt()]))
        }

        val barDataSet1 = BarDataSet(barEntries1, "전체개체")
        barDataSet1.color = Color.rgb(255, 155, 155)
        val barDataSet2 = BarDataSet(barEntries2, "임신개체")
        barDataSet2.color = Color.rgb(178, 223, 138)
        val barDataSet3 = BarDataSet(barEntries3, "건유개체")
        barDataSet3.color = Color.rgb(166, 208, 227)
        val barDataSet4 = BarDataSet(barEntries4, "거세개체")
        barDataSet4.color = Color.rgb(31, 120, 180)

        val months = arrayOf("8월", "9월", "10월", "11월")
        val data = BarData(barDataSet1, barDataSet2, barDataSet3, barDataSet4)
        var barChart = binding.chartWeek
        barChart.setData(data)

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