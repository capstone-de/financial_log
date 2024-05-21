package com.example.financiallog

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
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


    private val userId: Int = 6 // 사용자 ID를 여기에 할당하세요
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

        // 데이터 가져오기
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
                    // More 3 메뉴 선택 시 동작 구현
                    val intent = Intent(this, DiaryWriteAct::class.java)
                    startActivity(intent)
                    true
                }

                else -> false
            }
        }

        // 새로운 BottomNavigationView를 화면에 표시합니다.
        // 여기서는 예시로 다이얼로그 형태로 표시하였습니다.
        val dialog = AlertDialog.Builder(this)
            .setView(moreBottomView)
            .create()
        dialog.show()
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

        // 주 번호 계산
        calendar.time = date
        val weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR)

        // 월별 주 번호 계산
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
        val dateString = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date)
        apiobject.api.getStatisticsWeekly().enqueue(object : Callback<List<ResponseStatWeek>> {
            override fun onResponse(
                call: Call<List<ResponseStatWeek>>,
                response: Response<List<ResponseStatWeek>>
            ) {
                if (response.isSuccessful) {
                    val data = response.body()
                    data?.let {
                        updateBarChart(it)
                        updateExpensePieChart(it)
                        updateIncomePieChart(it)
                    }
                } else {
                    // API 호출 실패 처리
                }
            }

            override fun onFailure(call: Call<List<ResponseStatWeek>>, t: Throwable) {
                // API 호출 실패 처리
            }
        })
    }

    //일별 수입지출 통계
    private fun updateBarChart(data: List<ResponseStatWeek>) {
        val incomeEntries = mutableListOf<BarEntry>()
        val expenseEntries = mutableListOf<BarEntry>()

        data.forEachIndexed { index, item ->
            val offset = index * 7 // 각 주별 데이터는 7개의 항목이 필요합니다.

            // 월요일 데이터 처리
            val totalIncomeMon = item.mon.totalIncome ?: 0
            incomeEntries.add(BarEntry((offset).toFloat(), totalIncomeMon.toFloat()))
            val totalExpenseMon = item.mon.totalExpense ?: 0
            expenseEntries.add(BarEntry((offset).toFloat(), totalExpenseMon.toFloat()))

            // 화요일 데이터 처리
            val totalIncomeTue = item.tue.totalIncome ?: 0
            incomeEntries.add(BarEntry((offset + 1).toFloat(), totalIncomeTue.toFloat()))
            val totalExpenseTue = item.tue.totalExpense ?: 0
            expenseEntries.add(BarEntry((offset + 1).toFloat(), totalExpenseTue.toFloat()))

            // 수요일 데이터 처리
            val totalIncomeWed = item.wed.totalIncome ?: 0
            incomeEntries.add(BarEntry((offset + 2).toFloat(), totalIncomeWed.toFloat()))
            val totalExpenseWed = item.wed.totalExpense ?: 0
            expenseEntries.add(BarEntry((offset + 2).toFloat(), totalExpenseWed.toFloat()))

            // 목요일 데이터 처리
            val totalIncomeThu = item.thu.totalIncome ?: 0
            incomeEntries.add(BarEntry((offset + 3).toFloat(), totalIncomeThu.toFloat()))
            val totalExpenseThu = item.thu.totalExpense ?: 0
            expenseEntries.add(BarEntry((offset + 3).toFloat(), totalExpenseThu.toFloat()))

            // 금요일 데이터 처리
            val totalIncomeFri = item.fri.totalIncome ?: 0
            incomeEntries.add(BarEntry((offset + 4).toFloat(), totalIncomeFri.toFloat()))
            val totalExpenseFri = item.fri.totalExpense ?: 0
            expenseEntries.add(BarEntry((offset + 4).toFloat(), totalExpenseFri.toFloat()))

            // 토요일 데이터 처리
            val totalIncomeSat = item.sat.totalIncome ?: 0
            incomeEntries.add(BarEntry((offset + 5).toFloat(), totalIncomeSat.toFloat()))
            val totalExpenseSat = item.sat.totalExpense ?: 0
            expenseEntries.add(BarEntry((offset + 5).toFloat(), totalExpenseSat.toFloat()))

            // 일요일 데이터 처리
            val totalIncomeSun = item.sun.totalIncome ?: 0
            incomeEntries.add(BarEntry((offset + 6).toFloat(), totalIncomeSun.toFloat()))
            val totalExpenseSun = item.sun.totalExpense ?: 0
            expenseEntries.add(BarEntry((offset + 6).toFloat(), totalExpenseSun.toFloat()))
        }

        // 수입 데이터셋 설정
        val incomeDataSet = BarDataSet(incomeEntries, "수입")
        incomeDataSet.color = Color.GREEN

        // 지출 데이터셋 설정
        val expenseDataSet = BarDataSet(expenseEntries, "지출")
        expenseDataSet.color = Color.RED

        // 수입과 지출을 각각 보여줄 바 차트 데이터 설정
        val barData = BarData(incomeDataSet, expenseDataSet)

        // 바 차트에 데이터 설정 및 업데이트
        weekbarChart.data = barData

        // X축 설정
        val xAxis = weekbarChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.valueFormatter = IndexAxisValueFormatter(getDays())

        weekbarChart.groupBars(0f, 0.4f, 0.04f) // 그룹 간격 설정
        weekbarChart.invalidate()
    }
    private fun getDays(): List<String> {
        return listOf("월요일", "화요일", "수요일", "목요일", "금요일", "토요일", "일요일")
    }

    // 지출 파이 차트
    private fun updateExpensePieChart(data: List<ResponseStatWeek>) {
        val entries = mutableListOf<PieEntry>()

        // Aggregate expenses for all categories over the week
        val categoryExpenses = mutableMapOf<String, Int>()

        data.forEach { weekData ->
            weekData.category.expense.forEach { expense ->
                categoryExpenses[expense.category] = categoryExpenses.getOrDefault(expense.category, 0) + expense.totalExpense
            }
        }

        // Convert the aggregated data into PieEntry objects
        categoryExpenses.forEach { (category, totalExpense) ->
            entries.add(PieEntry(totalExpense.toFloat(), category))
        }

        val dataSet = PieDataSet(entries, "주간 카테고리별 지출")
        dataSet.colors = ColorTemplate.MATERIAL_COLORS.toList()

        val pieData = PieData(dataSet)
        expensePieChart.data = pieData
        expensePieChart.invalidate() // Refresh the chart
    }

    //수입 파이 차트
    private fun updateIncomePieChart(data: List<ResponseStatWeek>) {
        val entries = mutableListOf<PieEntry>()

        // Aggregate income for all categories over the week
        val categoryIncome = mutableMapOf<String, Int>()

        data.forEach { weekData ->
            weekData.category.income.forEach { income ->
                categoryIncome[income.category] = categoryIncome.getOrDefault(income.category, 0) + income.totalIncome
            }
        }

        // Convert the aggregated data into PieEntry objects
        categoryIncome.forEach { (category, totalIncome) ->
            entries.add(PieEntry(totalIncome.toFloat(), category))
        }

        val dataSet = PieDataSet(entries, "주간 카테고리별 수입")
        dataSet.colors = ColorTemplate.JOYFUL_COLORS.toList()

        val pieData = PieData(dataSet)
        incomePieChart.data = pieData
        incomePieChart.invalidate() // Refresh the chart
    }


}

