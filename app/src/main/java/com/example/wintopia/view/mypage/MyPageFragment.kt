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

        val pref = context?.getSharedPreferences("userId", 0)
        val edit = pref?.edit() // 수정모드
        val savedCheckBox = pref?.getBoolean("checkBox", false)

        val user_id = UserList().getId().toString()
        Log.d(TAG, "$user_id")
        viewModel.userPageInfo("test")
//        myPageEvent()








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


    data class ChartData(
        var lableData: String = "",
        var valData: Double = 0.0
    )

    fun lineEvent(){
        entryChart1.add(Entry(0f, 1f)) //entryChart1에 좌표 데이터를 담는다.
        entryChart1.add(Entry(1f, 5f))
        entryChart1.add(Entry(2f, 3f))
        entryChart1.add(Entry(3f, 8f))
        entryChart1.add(Entry(4f, 5f))
        entryChart1.add(Entry(5f, 10f))

        entryChart2.add(Entry(0f, 6f)) //entryChart2에 좌표 데이터를 담는다.
        entryChart2.add(Entry(1f, 3f))
        entryChart2.add(Entry(2f, 1f))
        entryChart2.add(Entry(3f, 3f))
        entryChart2.add(Entry(4f, 0f))
        entryChart2.add(Entry(5f, 1f))

        entryChart3.add(Entry(0f, 7f)) //entryChart3에 좌표 데이터를 담는다.
        entryChart3.add(Entry(1f, 4f))
        entryChart3.add(Entry(2f, 3f))
        entryChart3.add(Entry(3f, 5f))
        entryChart3.add(Entry(4f, 8f))
        entryChart3.add(Entry(5f, 2f))

        entryChart4.add(Entry(0f, 9f)) //entryChart4에 좌표 데이터를 담는다.
        entryChart4.add(Entry(1f, 5f))
        entryChart4.add(Entry(2f, 5f))
        entryChart4.add(Entry(3f, 3f))
        entryChart4.add(Entry(4f, 6f))
        entryChart4.add(Entry(5f, 4f))

        // 데이터가 담긴 Arraylist 를 LineDataSet 으로 변환한다.
        val lineDataSet1 =
            LineDataSet(entryChart1, "LineGraph1")
        val lineDataSet2 =
            LineDataSet(entryChart2, "LineGraph2")
        val lineDataSet3 =
            LineDataSet(entryChart3, "LineGraph3")
        val lineDataSet4 =
            LineDataSet(entryChart4, "LineGraph4")

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
        lineDataSet2.circleColors = mutableListOf(Color.rgb(178, 223, 138))
        lineDataSet3.circleRadius = 6f
        lineDataSet3.lineWidth= 4f
        lineDataSet3.setDrawValues(false)
        lineDataSet3.setDrawCircleHole(true)
        lineDataSet3.setDrawCircles(true)
        lineDataSet3.setDrawHorizontalHighlightIndicator(false)
        lineDataSet3.setDrawHighlightIndicators(false)

        lineDataSet4.color = Color.rgb(31, 120, 180)
        lineDataSet4.setCircleColor(Color.rgb(31, 120, 180))
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

        val barEntries = ArrayList<BarEntry>()
        val barEntries1 = ArrayList<BarEntry>()
        val barEntries2 = ArrayList<BarEntry>()
        val barEntries3 = ArrayList<BarEntry>()

        barEntries.add(BarEntry(1f, 5f))
        barEntries.add(BarEntry(2f, 3f))
        barEntries.add(BarEntry(3f, 8f))
        barEntries.add(BarEntry(4f, 7f))
        barEntries.add(BarEntry(5f, 6f))
        barEntries.add(BarEntry(6f, 4f))
        barEntries.add(BarEntry(7f, 8f))

        barEntries1.add(BarEntry(1f, 9f))
        barEntries1.add(BarEntry(2f, 1f))
        barEntries1.add(BarEntry(3f, 6f))
        barEntries1.add(BarEntry(4f, 7f))
        barEntries1.add(BarEntry(5f, 2f))
        barEntries1.add(BarEntry(6f, 5f))
        barEntries1.add(BarEntry(7f, 2f))

        barEntries2.add(BarEntry(1f, 9f))
        barEntries2.add(BarEntry(2f, 6f))
        barEntries2.add(BarEntry(3f, 1f))
        barEntries2.add(BarEntry(4f, 3f))
        barEntries2.add(BarEntry(5f, 2f))
        barEntries2.add(BarEntry(6f, 5f))
        barEntries2.add(BarEntry(7f, 1f))

        barEntries3.add(BarEntry(1f, 2f))
        barEntries3.add(BarEntry(2f, 9f))
        barEntries3.add(BarEntry(3f, 1f))
        barEntries3.add(BarEntry(4f, 3f))
        barEntries3.add(BarEntry(5f, 2f))
        barEntries3.add(BarEntry(6f, 5f))
        barEntries3.add(BarEntry(7f, 9f))

        val barDataSet = BarDataSet(barEntries, "전체 개체 수")
        barDataSet.color = Color.parseColor("#f5abbc")
        val barDataSet1 = BarDataSet(barEntries1, "임신 개체")
        barDataSet1.setColors(Color.parseColor("#eaa8f7"))
        val barDataSet2 = BarDataSet(barEntries2, "건유 개체")
        barDataSet2.setColors(Color.parseColor("#f3f576"))
        val barDataSet3 = BarDataSet(barEntries3, "거세 개체")
        barDataSet3.setColors(Color.parseColor("#85f2a6"))

        val months = arrayOf("8월", "9월", "10월", "11월")
        val data = BarData(barDataSet, barDataSet1, barDataSet2, barDataSet3)
        var barChart = binding.chartWeek
        barChart.setData(data)

        val xAxis: XAxis = barChart.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(months)
        barChart.axisLeft.axisMinimum = 0f
        barChart.axisLeft.axisMaximum = 20f
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