package com.example.financiallog

import android.content.Intent
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
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class AnalyzeWeekAct : AppCompatActivity() {

    lateinit var weekText: TextView
    lateinit var weekText_date: TextView
    lateinit var currentDate: Date
    lateinit var mFormat: SimpleDateFormat
    lateinit var barChart: BarChart
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
        barChart = findViewById(R.id.chart)

        // 날짜 표시
        mFormat = SimpleDateFormat("MM월 dd일", Locale.KOREAN)
        currentDate = Date()
        updateWeekText(currentDate)

        leftBtn.setOnClickListener { changeDate(-7) }
        rightBtn.setOnClickListener { changeDate(7) }

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

        // 데이터 가져오기
        getDataForWeek(currentDate)
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
        val weekRangeText = "(${monthFormat.format(startOfWeek)}.${dayFormat.format(startOfWeek)}~${monthFormat.format(endOfWeek)}.${dayFormat.format(endOfWeek)})"

        weekText.text = weekNumberText
        weekText_date.text = weekRangeText
    }

    private fun getDataForWeek(date: Date) {
        val dateString = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date)
        apiobject.api.getStatisticsWeekly().enqueue(object : Callback<List<ResponseStatWeek>> {
            override fun onResponse(call: Call<List<ResponseStatWeek>>, response: Response<List<ResponseStatWeek>>) {
                if (response.isSuccessful) {
                    val data = response.body()
                    data?.let { updateChart(it) }
                } else {
                    // API 호출 실패 처리
                }
            }

            override fun onFailure(call: Call<List<ResponseStatWeek>>, t: Throwable) {
                // API 호출 실패 처리
            }
        })
    }

    //차트 내용 수정해야함
    private fun updateChart(data: List<ResponseStatWeek>) {
        val entries = data.mapIndexed { index, statWeek ->
            val totalAmount = calculateTotalAmount(statWeek)
            BarEntry(index.toFloat(), totalAmount)
        }
        val dataSet = BarDataSet(entries, "주간 총 금액")
        val barData = BarData(dataSet)
        barChart.data = barData
        barChart.invalidate()
    }

    private fun calculateTotalAmount(statWeek: ResponseStatWeek): Float {
        val days = listOf(
            statWeek.mon, statWeek.tue, statWeek.wed, statWeek.thu,
            statWeek.fri, statWeek.sat, statWeek.sun
        )
        var totalAmount = 0f
        for (day in days) {
            val income = day.totalIncome ?: 0
            val expense = day.totalExpense ?: 0
            totalAmount += (income - expense)
        }
        return totalAmount
    }
}

