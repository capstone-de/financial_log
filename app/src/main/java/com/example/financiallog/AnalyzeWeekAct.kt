package com.example.financiallog

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class AnalyzeWeekAct : AppCompatActivity() {

    lateinit var weekText: TextView
    lateinit var weekText_date: TextView
    lateinit var currentDate: Date
    lateinit var mFormat: SimpleDateFormat
    lateinit var weekbarChart: BarChart
    lateinit var incomePieChart: PieChart
    lateinit var expensePieChart: PieChart

    val apiobject: ApiObject by lazy { ApiObject() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.analyze_week)

        val analyze_btn = findViewById<Button>(R.id.btn_analyze)
        val analyzedi_btn = findViewById<Button>(R.id.btn_diary)
        val tab_analyze = findViewById<TabLayout>(R.id.tabLayout)
        val tab_day = findViewById<TabLayout>(R.id.day)
        val tab_week = findViewById<TabLayout>(R.id.week)
        val tab_month = findViewById<TabLayout>(R.id.month)
        val tab_yearly = findViewById<TabLayout>(R.id.yearly)

        val leftBtn = findViewById<ImageButton>(R.id.leftButton)
        val rightBtn = findViewById<ImageButton>(R.id.rightButton)

        weekText = findViewById(R.id.week_text)
        weekText_date = findViewById(R.id.week_text2)
        weekbarChart = findViewById(R.id.chart)
        expensePieChart = findViewById(R.id.chart2)
        incomePieChart = findViewById(R.id.chart3)

        // 날짜 표시
        mFormat = SimpleDateFormat("MM월 dd일", Locale.KOREAN)
        currentDate = Date()
        updateWeekText(currentDate)

        leftBtn.setOnClickListener { changeDate(-7) }
        rightBtn.setOnClickListener { changeDate(7) }

        // 현재 데이터 가져오기
        getDataForWeek(currentDate)


        // 가계부 버튼 클릭 시
        analyze_btn.setOnClickListener {
            val intent = Intent(this, AnalyzeDayAct::class.java)
            startActivity(intent)
        }

        // 일기 버튼 클릭 시
        analyzedi_btn.setOnClickListener {
            val intent = Intent(this, AnalyzeDiaryAct::class.java)
            startActivity(intent)
        }

        // 탭 전환 시
        tab_analyze.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let { // tab이 null이 아닌 경우에만 실행
                    when (it.position) {
                        0 -> {
                            val intent = Intent(this@AnalyzeWeekAct, AnalyzeDayAct::class.java)
                            startActivity(intent)
                        }

                        1 -> {
                            val intent = Intent(this@AnalyzeWeekAct, AnalyzeWeekAct::class.java)
                            startActivity(intent)
                        }

                        2 -> {
                            val intent = Intent(this@AnalyzeWeekAct, AnalyzeMonthAct::class.java)
                            startActivity(intent)
                        }

                        3 -> {
                            val intent = Intent(this@AnalyzeWeekAct, AnalyzeyearlyAct::class.java)
                            startActivity(intent)
                        }
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // 탭이 선택 해제될 때 필요한 처리
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                // 탭이 다시 선택될 때 필요한 처리
                tab?.let { // tab이 null이 아닌 경우에만 실행
                    when (it.position) {
                        0 -> {
                            val intent = Intent(this@AnalyzeWeekAct, AnalyzeDayAct::class.java)
                            startActivity(intent)
                        }

                        1 -> {
                            val intent = Intent(this@AnalyzeWeekAct, AnalyzeWeekAct::class.java)
                            startActivity(intent)
                        }

                        2 -> {
                            val intent = Intent(this@AnalyzeWeekAct, AnalyzeMonthAct::class.java)
                            startActivity(intent)
                        }

                        3 -> {
                            val intent = Intent(this@AnalyzeWeekAct, AnalyzeyearlyAct::class.java)
                            startActivity(intent)
                        }
                    }
                }
            }
        })

        // 하단바 클릭 시
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_view)
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    val intent = Intent(this, HomeMain::class.java)
                    startActivity(intent)
                    Toast.makeText(applicationContext, "home", Toast.LENGTH_SHORT).show()
                }

                R.id.financial -> {
                    val intent = Intent(this, AnalyzeDayAct::class.java)
                    startActivity(intent)
                    Toast.makeText(applicationContext, "financial", Toast.LENGTH_SHORT).show()
                }

                R.id.add -> {
                    showMoreMenu()
                    true
                    // val intent = Intent(this, ExpendAct::class.java)
                    // startActivity(intent)
                }

                R.id.diary -> {
                    val intent = Intent(this, DiarySns::class.java)
                    startActivity(intent)
                    Toast.makeText(applicationContext, "sns_feed", Toast.LENGTH_SHORT).show()
                }

                R.id.settings -> {
                    val intent = Intent(this, MyPage::class.java)
                    startActivity(intent)
                    Toast.makeText(applicationContext, "mypage", Toast.LENGTH_SHORT).show()
                }

                else -> {
                    Toast.makeText(applicationContext, "else", Toast.LENGTH_SHORT).show()
                }
            }
            true
        }

    }

    private fun showMoreMenu() {
        val moreBottomView = BottomNavigationView(this)
        moreBottomView.menu.add(0, R.id.add_income, 0, "수입")
        moreBottomView.menu.add(0, R.id.add_expend, 1, "지출")
        moreBottomView.menu.add(0, R.id.add_diary, 2, "일기")

        // 새로운 BottomNavigationView를 화면에 표시합니다.
        // 여기서는 예시로 다이얼로그 형태로 표시하였습니다.
        val dialog = AlertDialog.Builder(this)
            .setView(moreBottomView)
            .create()
        dialog.show()

        // 새로운 BottomNavigationView의 클릭 이벤트를 처리합니다.
        moreBottomView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.add_income -> {
                    // More 1 메뉴 선택 시 동작 구현
                    val intent = Intent(this, IncomeAct::class.java)
                    startActivity(intent)
                    true
                }

                R.id.add_expend -> {
                    // More 2 메뉴 선택 시 동작 구현
                    val intent = Intent(this, ExpendAct::class.java)
                    startActivity(intent)
                    true
                }

                R.id.add_diary -> {
                    // Retrofit 서비스 호출
                    apiobject.api.diarywriteEx(6,getCurrentFormattedDate()).enqueue(object : Callback<List<DataEx>> {
                        override fun onResponse(call: Call<List<DataEx>>, response: Response<List<DataEx>>) {
                            if (response.isSuccessful && response.body() != null) {
                                // 네트워크 응답이 성공적이고 데이터가 있는 경우
                                val intent = Intent(this@AnalyzeWeekAct, DiaryWriteAct::class.java)
                                startActivity(intent)
                            } else {
                                // 네트워크 응답이 실패했거나 데이터가 없는 경우
                                Toast.makeText(this@AnalyzeWeekAct, "이미 저장된 일기가 있습니다.", Toast.LENGTH_SHORT).show()
                                dialog.dismiss() // 다이얼로그 닫기
                            }
                        }

                        override fun onFailure(call: Call<List<DataEx>>, t: Throwable) {
                            // 네트워크 요청 실패 시
                            Toast.makeText(this@AnalyzeWeekAct, "네트워크 요청에 실패했습니다.", Toast.LENGTH_SHORT).show()
                            t.printStackTrace()  // 오류 스택 트레이스를 출력하여 디버깅에 도움을 줌
                            dialog.dismiss() // 다이얼로그 닫기
                        }
                    })
                    true
                }

                else -> false
            }
        }


    }
    fun getCurrentFormattedDate(): String {
        val currentDate = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return currentDate.format(formatter)
    }

    // 날짜 변경 시
    private fun changeDate(days: Int) {
        val calendar = Calendar.getInstance()
        calendar.time = currentDate
        calendar.add(Calendar.DATE, days)
        currentDate = calendar.time
        updateWeekText(currentDate)
        getDataForWeek(currentDate)
    }

    private fun updateWeekText(date: Date) {
        val calendar = Calendar.getInstance()
        calendar.time = date

        // 현재 주의 첫날 (월요일)
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        val startOfWeek = calendar.time

        // 현재 주의 마지막 날 (일요일)
        calendar.add(Calendar.DATE, 6)
        val endOfWeek = calendar.time

        // 월별 주 번호 계산
        calendar.time = date
        val weekOfMonth = calendar.get(Calendar.WEEK_OF_MONTH)

        val monthFormat = SimpleDateFormat("MM월", Locale.KOREAN)
        val dayFormat = SimpleDateFormat("dd", Locale.KOREAN)
        val weekNumberText = "${monthFormat.format(date)} ${weekOfMonth}주"
        val weekRangeText = "(${monthFormat.format(startOfWeek)}.${dayFormat.format(startOfWeek)}~${
            monthFormat.format(endOfWeek)
        }.${dayFormat.format(endOfWeek)})"

        weekText.text = weekNumberText
        weekText_date.text = weekRangeText
    }


    private fun getDataForWeek(date: Date) {
        val calendar = Calendar.getInstance().apply {
            time = date
        }

        val startOfWeek = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)
        //val formattedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date)

        apiobject.api.getStatisticsWeekly(6, startOfWeek).enqueue(object : Callback<ResponseStatWeek> {
            override fun onResponse(call: Call<ResponseStatWeek>, response: Response<ResponseStatWeek>) {
                if (response.isSuccessful) {
                    response.body()?.let { data ->
                        Log.d("데이터 받기", data.toString())

                        WeekBarChart(data)

                        //WeekExpensePieChart(listOf(data))
                        //WeekIncomePieChart(listOf(data))
                    }
                } else {
                    // API 호출 실패 처리
                    Log.e("API 에러", "API 호출 에러")
                    Toast.makeText(applicationContext, "API 정보를 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseStatWeek>, t: Throwable) {
                Log.e("API Error", "Failed to fetch data: ${t.message}")
                Toast.makeText(applicationContext, "정보를 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    //일별 수입지출 통계
    /*private fun WeekBarChart(data: List<ResponseStatWeek>) {

        val incomeEntries = mutableListOf<BarEntry>()
        val expenseEntries = mutableListOf<BarEntry>()

        data.forEachIndexed { index, item ->
            val offset = index * 7 // 각 주별 데이터는 7개의 항목이 필요합니다.

            // 월요일 데이터 처리
            val totalIncomeMon = item.mon?.totalIncome ?: 0
            incomeEntries.add(BarEntry((offset).toFloat(), totalIncomeMon.toFloat()))
            val totalExpenseMon = item.mon?.totalExpense ?: 0
            expenseEntries.add(BarEntry((offset).toFloat(), totalExpenseMon.toFloat()))

            // 화요일 데이터 처리
            val totalIncomeTue = item.tue?.totalIncome ?: 0
            incomeEntries.add(BarEntry((offset + 1).toFloat(), totalIncomeTue.toFloat()))
            val totalExpenseTue = item.tue?.totalExpense ?: 0
            expenseEntries.add(BarEntry((offset + 1).toFloat(), totalExpenseTue.toFloat()))

            // 수요일 데이터 처리
            val totalIncomeWed = item.wed?.totalIncome ?: 0
            incomeEntries.add(BarEntry((offset + 2).toFloat(), totalIncomeWed.toFloat()))
            val totalExpenseWed = item.wed?.totalExpense ?: 0
            expenseEntries.add(BarEntry((offset + 2).toFloat(), totalExpenseWed.toFloat()))

            // 목요일 데이터 처리
            val totalIncomeThu = item.thu?.totalIncome ?: 0
            incomeEntries.add(BarEntry((offset + 3).toFloat(), totalIncomeThu.toFloat()))
            val totalExpenseThu = item.thu?.totalExpense ?: 0
            expenseEntries.add(BarEntry((offset + 3).toFloat(), totalExpenseThu.toFloat()))

            // 금요일 데이터 처리
            val totalIncomeFri = item.fri?.totalIncome ?: 0
            incomeEntries.add(BarEntry((offset + 4).toFloat(), totalIncomeFri.toFloat()))
            val totalExpenseFri = item.fri?.totalExpense ?: 0
            expenseEntries.add(BarEntry((offset + 4).toFloat(), totalExpenseFri.toFloat()))

            // 토요일 데이터 처리
            val totalIncomeSat = item.sat?.totalIncome ?: 0
            incomeEntries.add(BarEntry((offset + 5).toFloat(), totalIncomeSat.toFloat()))
            val totalExpenseSat = item.sat?.totalExpense ?: 0
            expenseEntries.add(BarEntry((offset + 5).toFloat(), totalExpenseSat.toFloat()))

            // 일요일 데이터 처리
            val totalIncomeSun = item.sun?.totalIncome ?: 0
            incomeEntries.add(BarEntry((offset + 6).toFloat(), totalIncomeSun.toFloat()))
            val totalExpenseSun = item.sun?.totalExpense ?: 0
            expenseEntries.add(BarEntry((offset + 6).toFloat(), totalExpenseSun.toFloat()))
        }

        // 수입 데이터셋 설정
        val incomeDataSet = BarDataSet(incomeEntries, "수입")
        incomeDataSet.color = Color.parseColor("#6C72FF") // 수입 색상 설정
        incomeDataSet.valueTextColor = Color.BLACK
        incomeDataSet.valueTextSize = 10f

        // 지출 데이터셋 설정
        val expenseDataSet = BarDataSet(expenseEntries, "지출")
        expenseDataSet.color = Color.parseColor("#F998B5") // 지출 색상 설정
        expenseDataSet.valueTextColor = Color.BLACK
        expenseDataSet.valueTextSize = 10f

        // 수입과 지출을 각각 보여줄 바 차트 데이터 설정
        val barData = BarData(incomeDataSet, expenseDataSet)
        barData.barWidth = 0.4f // 막대 너비 설정

        // 바 차트에 데이터 설정 및 업데이트
        weekbarChart.data = barData

        // X축 설정
        val xAxis = weekbarChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.valueFormatter = IndexAxisValueFormatter(getDays())

        weekbarChart.groupBars(0f, 0.04f, 0.01f) // 그룹 간격 설정
        weekbarChart.invalidate()
    }
    private fun getDays(): List<String> {
        return listOf("월요일", "화요일", "수요일", "목요일", "금요일", "토요일", "일요일")
    }*/

    /*private fun WeekBarChart(data: List<ResponseStatWeek>?) {
        val incomeEntries = mutableListOf<BarEntry>()
        val expenseEntries = mutableListOf<BarEntry>()

        data?.firstOrNull()?.let { weekData ->
            for (weekIndex in 0 until 7) {
                val dayData = when (weekIndex) {
                    0 -> weekData.mon
                    1 -> weekData.tue
                    2 -> weekData.wed
                    3 -> weekData.thu
                    4 -> weekData.fri
                    5 -> weekData.sat
                    6 -> weekData.sun
                    else -> null
                } ?: ResponseStatWeek.DayStatistics(0, 0)

                val totalIncome = dayData.totalIncome ?: 0
                val totalExpense = dayData.totalExpense ?: 0


                val index = weekIndex * 7
                incomeEntries.add(BarEntry(index.toFloat(), totalIncome.toFloat()))
                expenseEntries.add(BarEntry(index.toFloat(), totalExpense.toFloat()))

                // 디버깅을 위한 로그 출력
                Log.d("WeekBarChart", "weekIndex: $weekIndex, totalIncome: $totalIncome, totalExpense: $totalExpense")
            }
        }

        val incomeDataSet = BarDataSet(incomeEntries, "수입").apply {
            color = Color.parseColor("#6C72FF")
            valueTextColor = Color.BLACK
            valueTextSize = 10f
        }

        val expenseDataSet = BarDataSet(expenseEntries, "지출").apply {
            color = Color.parseColor("#F998B5")
            valueTextColor = Color.BLACK
            valueTextSize = 10f
        }

        val barData = BarData(incomeDataSet, expenseDataSet).apply {
            barWidth = 0.4f
        }

        weekbarChart.data = barData
        weekbarChart.groupBars(0f, 0.04f, 0.01f)
        weekbarChart.invalidate()

        val xAxis = weekbarChart.xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            setDrawGridLines(false)
            valueFormatter = IndexAxisValueFormatter(getDays())
        }

        weekbarChart.invalidate()
    }

    private fun getDays(): List<String> {
        return listOf("월요일", "화요일", "수요일", "목요일", "금요일", "토요일", "일요일")
    }*/

    private fun WeekBarChart(data: ResponseStatWeek) {
        val incomeEntries = mutableListOf<BarEntry>()
        val expenseEntries = mutableListOf<BarEntry>()
        
        val daysData = listOf(
            data.mon, data.tue, data.wed, data.thu,
            data.fri, data.sat, data.sun
        )
        
        //데이터 모델에서 데이터 추출
        daysData.forEachIndexed{ index, dayData ->
            val totalIncome = dayData?.totalIncome ?: 0
            val totalExpense = dayData?.totalExpense ?: 0

            incomeEntries.add(BarEntry(index.toFloat(), totalIncome.toFloat()))
            expenseEntries.add(BarEntry(index.toFloat(), totalExpense.toFloat()))
        }


        val incomeDataSet = BarDataSet(incomeEntries, "수입")
        incomeDataSet.color = Color.parseColor("#6C72FF")
        incomeDataSet.valueTextColor = Color.BLACK
        incomeDataSet.valueTextSize = 10f

        val expenseDataSet = BarDataSet(expenseEntries, "지출")
        expenseDataSet.color = Color.parseColor("#F998B5")
        expenseDataSet.valueTextColor = Color.BLACK
        expenseDataSet.valueTextSize = 10f

        val barData = BarData(incomeDataSet, expenseDataSet)
        weekbarChart.data = barData

        //바 차트
        barData.barWidth = 0.4f
        weekbarChart.groupBars(0f, 0.04f, 0.01f)
        weekbarChart.invalidate()

        //x축
        val xAxis = weekbarChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.valueFormatter = IndexAxisValueFormatter(getDays())

        weekbarChart.invalidate()
    }

    private fun getDays(): List<String> {
        return listOf("월요일", "화요일", "수요일", "목요일", "금요일", "토요일", "일요일")
    }




    // 지출 파이 차트
    /*private fun WeekExpensePieChart(data: List<ResponseStatWeek>) {
        val entries = mutableListOf<PieEntry>()
        val categoryExpense = mutableMapOf<String, Int>()

        // 카테고리별 지출 합산
        data.forEach { weekData ->
            weekData.category?.expense?.forEach { expense ->
                val category = expense.category ?: "Unknown"
                val totalExpense = expense.totalExpense ?: 0
                categoryExpense[category] = categoryExpense.getOrDefault(category, 0) + totalExpense
            }
        }
        if (categoryExpense.isEmpty()) {
            Log.e("AnalyzeWeekAct", "No income data available for pie chart. Adding test data.")
            // 테스트 데이터 추가
            categoryExpense["Food"] = 200
            categoryExpense["Transportation"] = 100
            categoryExpense["Entertainment"] = 50

        }

        categoryExpense.forEach { (category, totalExpense) ->
            entries.add(PieEntry(totalExpense.toFloat(), category))
        }

        // 파이 데이터셋 생성 및 설정
        val dataSet = PieDataSet(entries, "주간 카테고리별 지출")
        dataSet.colors = ColorTemplate.MATERIAL_COLORS.toList() // 색상 설정

        // 파이 차트에 데이터 설정
        val pieData = PieData(dataSet)
        expensePieChart.data = pieData

        // 파이 차트 스타일 및 라벨 설정
        expensePieChart.setUsePercentValues(true)
        expensePieChart.description.isEnabled = true
        expensePieChart.description.text = "주간 카테고리별 지출"
        expensePieChart.description.textSize = 12f
        expensePieChart.setEntryLabelColor(Color.BLACK)
        expensePieChart.setEntryLabelTextSize(12f)


        // 파이 차트 중앙 구멍 활성화 및 제목 설정
        expensePieChart.isDrawHoleEnabled = true
        expensePieChart.holeRadius = 40f // 중앙 구멍 반지름 설정
        expensePieChart.transparentCircleRadius = 45f // 투명한 원 반지름 설정
        expensePieChart.centerText = "지출"
        expensePieChart.setCenterTextSize(15f)
        expensePieChart.setCenterTextColor(Color.BLACK)

        //차트 업데이트
        expensePieChart.invalidate() // Refresh the chart
    }


    // 수입 파이 차트
    private fun WeekIncomePieChart(data: List<ResponseStatWeek>) {
        val entries = mutableListOf<PieEntry>()
        val categoryIncome = mutableMapOf<String, Int>()

        data.forEach { weekData ->
            weekData.category?.income?.forEach { income ->
                val category = income.category ?: "Unknown"
                val totalIncome = income.totalIncome ?: 0
                categoryIncome[category] = categoryIncome.getOrDefault(category, 0) + totalIncome
            }
        }

        if (categoryIncome.isEmpty()) {
            Log.e("AnalyzeWeekAct", "No income data available for pie chart.")
            // 테스트 데이터 추가
            categoryIncome["Salary"] = 5000
            categoryIncome["Investment"] = 2000
            categoryIncome["Freelance"] = 3000
        }

        categoryIncome.forEach { (category, totalIncome) ->
            entries.add(PieEntry(totalIncome.toFloat(), category))
        }

        val dataSet = PieDataSet(entries, "수입")
        dataSet.colors = ColorTemplate.JOYFUL_COLORS.toList()
        dataSet.valueTextColor = Color.BLACK
        dataSet.valueTextSize = 12f

        val pieData = PieData(dataSet)
        incomePieChart.data = pieData

        // 파이 차트 스타일 설정
        incomePieChart.setUsePercentValues(true)
        incomePieChart.description.isEnabled = true
       // incomePieChart.description.text = "카테고리별 수입"
        // incomePieChart.description.textSize = 12f

        // 파이 차트 중앙 구멍 활성화 및 제목 설정
        incomePieChart.isDrawHoleEnabled = true
        incomePieChart.holeRadius = 40f
        incomePieChart.transparentCircleRadius = 45f
        incomePieChart.centerText = "수입"
        incomePieChart.setCenterTextSize(15f)
        incomePieChart.setCenterTextColor(Color.BLACK)

        // 라벨 설정
        incomePieChart.setEntryLabelColor(Color.BLACK)
        incomePieChart.setEntryLabelTextSize(12f)

        // 파이 조각의 최소 각도 설정 (기본값은 0f)
        incomePieChart.minAngleForSlices = 15f // 각 파이 조각의 최소 각도를 설정하여 보기 좋게 조정

        incomePieChart.invalidate() // Refresh the chart
    }*/


}

