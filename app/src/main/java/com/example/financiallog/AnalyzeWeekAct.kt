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
import com.github.mikephil.charting.components.Legend
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

                        WeekExpensePieChart(listOf(data))
                        WeekIncomePieChart(listOf(data))
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
    private fun WeekBarChart(data: ResponseStatWeek) {
        val incomeEntries = mutableListOf<BarEntry>()
        val expenseEntries = mutableListOf<BarEntry>()

        val daysData = listOf(
            data.Mon, data.Tue, data.Wed, data.Thu,
            data.Fri, data.Sat, data.Sun
        )

        // 데이터 모델에서 데이터 추출
        daysData.forEachIndexed { index, dayData ->
            val totalIncome = dayData?.total_income ?: 0
            val totalExpense = dayData?.total_expense ?: 0

            incomeEntries.add(BarEntry(index.toFloat(), totalIncome.toFloat()))
            expenseEntries.add(BarEntry(index.toFloat(), totalExpense.toFloat()))
        }

        val incomeDataSet = BarDataSet(incomeEntries, "수입")
        incomeDataSet.color = Color.parseColor("#6C72FF")
        //incomeDataSet.setDrawValues(false) // 값을 차트 위에 표시하지 않음
        //incomeDataSet.setBarRounded(true) // 모든 바의 모서리를 라운드 처리

        val expenseDataSet = BarDataSet(expenseEntries, "지출")
        expenseDataSet.color = Color.parseColor("#F998B5")
        //expenseDataSet.setDrawValues(false) // 값을 차트 위에 표시하지 않음
        //expenseDataSet.setBarRounded(true) // 모든 바의 모서리를 라운드 처리

        val barData = BarData(incomeDataSet, expenseDataSet)
        weekbarChart.data = barData

        // 바 차트
        val barWidth = 0.3f // 바의 넓이 조절
        barData.barWidth = barWidth
        val groupSpace = 1f
        val barSpace = 0.05f
        weekbarChart.groupBars(0f, groupSpace, barSpace)

        // x축
        val xAxis = weekbarChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.valueFormatter = IndexAxisValueFormatter(getDays())
        xAxis.setDrawAxisLine(false) // x축 선 제거

        /*// y축
        val yAxisLeft = weekbarChart.axisLeft
        yAxisLeft.setDrawGridLines(false)
        yAxisLeft.setDrawAxisLine(false) // y축 왼쪽 선 제거
        yAxisLeft.setDrawLabels(false) // y축 왼쪽 레이블 제거

        val yAxisRight = weekbarChart.axisRight
        yAxisRight.setDrawGridLines(false)
        yAxisRight.setDrawAxisLine(false) // y축 오른쪽 선 제거
        yAxisRight.setDrawLabels(false) // y축 오른쪽 레이블 제거*/

        // 차트 스타일 설정
        weekbarChart.description.isEnabled = false // 차트 설명 제거
        weekbarChart.legend.isEnabled = true // 범례 설정 (범례는 유지)

        // 차트 업데이트
        weekbarChart.invalidate()
    }

    private fun getDays(): List<String> {
        return listOf("월요일", "화요일", "수요일", "목요일", "금요일", "토요일", "일요일")
    }


    // 지출 파이 차트
    private fun WeekExpensePieChart(data: List<ResponseStatWeek>) {
        val entries = mutableListOf<PieEntry>()
        val categoryExpense = mutableMapOf<String, Int>()

        // 카테고리 이름 매핑
        val categoryMap = mapOf(
            "tax" to "세금",
            "food" to "음식",
            "housing/communication" to "주거/통신",
            "tranportation/vehicle" to "교통/차량",
            "education" to "교육",
            "personal event" to "경조사/회비",
            "medical" to "병원/약국",
            "cultural/living" to "문화생활",
            "shopping" to "쇼핑",
            "etc" to "기타"
        )

        // 카테고리별 지출 합산
        data.forEach { weekData ->
            weekData.category?.expense?.forEach { expense ->
                val category = expense.category ?: "Unknown"
                val totalExpense = expense.total_expense ?: 0
                categoryExpense[category] = categoryExpense.getOrDefault(category, 0) + totalExpense
            }
        }

        if (categoryExpense.isEmpty()) {
            Log.e("AnalyzeWeekAct", "지출 데이터가 없습니다.")
        }

        categoryExpense.forEach { (category, totalExpense) ->
            // 카테고리 이름을 한글로 매핑
            val categoryName = categoryMap[category] ?: category
            entries.add(PieEntry(totalExpense.toFloat(), categoryName))
        }

        // 파이 데이터셋 생성 및 설정
        val dataSet = PieDataSet(entries, "")
        dataSet.colors = ColorTemplate.VORDIPLOM_COLORS.toList() // 색상 설정
        dataSet.setDrawValues(false) // 파이 조각 위 숫자값 숨기기

        // 파이 차트에 데이터 설정
        val pieData = PieData(dataSet)
        expensePieChart.data = pieData

        // 파이 차트 스타일 및 라벨 설정
        expensePieChart.setUsePercentValues(true)
        expensePieChart.description.isEnabled = true

        // 차트 위의 라벨 숨기기
        expensePieChart.setDrawEntryLabels(false)

        // 파이 차트 중앙 구멍 활성화 및 제목 설정
        expensePieChart.isDrawHoleEnabled = true
        expensePieChart.holeRadius = 40f // 중앙 구멍 반지름 설정
        expensePieChart.transparentCircleRadius = 45f // 투명한 원 반지름 설정
        expensePieChart.centerText = "지출"
        expensePieChart.setCenterTextSize(15f)
        expensePieChart.setCenterTextColor(Color.BLACK)

        // 범례 설정
        val legend = expensePieChart.legend
        legend.isEnabled = true
        legend.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
        legend.orientation = Legend.LegendOrientation.HORIZONTAL
        legend.setDrawInside(false)
        legend.textSize = 12f

        // 차트 업데이트
        expensePieChart.invalidate()
    }




    // 수입 파이 차트
    private fun WeekIncomePieChart(data: List<ResponseStatWeek>) {
        val entries = mutableListOf<PieEntry>()
        val categoryIncome = mutableMapOf<String, Int>()

        // 카테고리 이름 매핑
        val categoryMap = mapOf(

            "salary" to "월급",
            "etc" to "기타"
            //"additionalincome" to "부수입",
            //"financialincome" to "금융수입"

        )

        data.forEach { weekData ->
            weekData.category?.income?.forEach { income ->
                val category = income.category ?: "Unknown"
                val totalIncome = income.total_income ?: 0
                categoryIncome[category] = categoryIncome.getOrDefault(category, 0) + totalIncome
            }
        }

        if (categoryIncome.isEmpty()) {
            Log.e("AnalyzeWeekAct", "수입 데이터가 없습니다.")
        }

        categoryIncome.forEach { (category, totalIncome) ->

            val categoryName = categoryMap[category] ?: category
            entries.add(PieEntry(totalIncome.toFloat(), categoryName))
        }

        // 파이 데이터셋 생성 및 설정
        val dataSet = PieDataSet(entries, "")
        //다른색 dataSet.colors = ColorTemplate.JOYFUL_COLORS.toList()
        dataSet.colors = ColorTemplate.LIBERTY_COLORS.toList()
        dataSet.setDrawValues(false) // 파이 조각 위 숫자값 숨기기


        //dataSet.valueTextColor = Color.BLACK
        //dataSet.valueTextSize = 12f

        // 파이 차트에 데이터 설정
        val pieData = PieData(dataSet)
        incomePieChart.data = pieData

        // 파이 차트 스타일 및 라벨 설정
        incomePieChart.setUsePercentValues(true)
        incomePieChart.description.isEnabled = true

        // 차트 위의 라벨 숨기기
        incomePieChart.setDrawEntryLabels(false)

        // 파이 차트 중앙 구멍 활성화 및 제목 설정
        incomePieChart.isDrawHoleEnabled = true
        incomePieChart.holeRadius = 40f
        incomePieChart.transparentCircleRadius = 45f
        incomePieChart.centerText = "수입"
        incomePieChart.setCenterTextSize(15f)
        incomePieChart.setCenterTextColor(Color.BLACK)

        // 범례 설정
        val legend = incomePieChart.legend
        legend.isEnabled = true
        legend.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
        legend.orientation = Legend.LegendOrientation.HORIZONTAL
        legend.setDrawInside(false)
        legend.textSize = 12f


        // 차트 업데이트
        incomePieChart.invalidate()
    }

}

