package com.example.financiallog

import android.annotation.SuppressLint
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
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.NumberFormat
import java.util.*

class AnalyzeMonthAct: AppCompatActivity() {
    lateinit var monthText: TextView
    lateinit var income_text: TextView
    lateinit var expend_text: TextView
    lateinit var mFormat: SimpleDateFormat
    lateinit var currentDate: Date
    lateinit var monthChart: BarChart
    lateinit var mon_cateChart: BarChart

    lateinit var ranking_cate1: TextView;lateinit var ranking_text1: TextView;lateinit var ranking_per1: TextView;
    lateinit var ranking_cate2: TextView;lateinit var ranking_text2: TextView;lateinit var ranking_per2: TextView;
    lateinit var ranking_cate3: TextView;lateinit var ranking_text3: TextView;lateinit var ranking_per3: TextView;
    lateinit var ranking_cate4: TextView;lateinit var ranking_text4: TextView;lateinit var ranking_per4: TextView;
    lateinit var ranking_cate5: TextView;lateinit var ranking_text5: TextView;lateinit var ranking_per5: TextView;
    lateinit var satisfaction_cate: TextView;lateinit var satisfaction_bname: TextView;lateinit var satisfaction_price: TextView;lateinit var satisfaction_per: TextView;
    lateinit var satisfaction_textS: TextView;lateinit var satisfaction_textE: TextView;
    var selectedMonth: Date = Date() // 초기화


    val apiobject: ApiObject by lazy { ApiObject() }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.analyze_month)

        val analyze_btn = findViewById<Button>(R.id.btn_analyze)
        val analyzedi_btn = findViewById<Button>(R.id.btn_diary)

        val tab_analyze =findViewById<TabLayout>(R.id.tabLayout3)
        val tab_day = findViewById<TabLayout>(R.id.day)
        val tab_week =findViewById<TabLayout>(R.id.week)
        val tab_month = findViewById<TabLayout>(R.id.month)
        val tab_yearly = findViewById<TabLayout>(R.id.yearly)

        val month_btn = findViewById<ImageButton>(R.id.m_underButton)

        monthText = findViewById(R.id.monthly_tv)
        income_text = findViewById(R.id.in_price)
        expend_text = findViewById(R.id.ex_price)

        monthChart = findViewById((R.id.mon_chart))
        mon_cateChart = findViewById((R.id.mon_chart2))

        //랭킹
        ranking_cate1 = findViewById(R.id.ranking_1)
        ranking_text1 = findViewById(R.id.ranking_text1)
        ranking_per1 = findViewById(R.id.ranking_per1)

        ranking_cate2 = findViewById(R.id.ranking_2)
        ranking_text2 = findViewById(R.id.ranking_text2)
        ranking_per2 = findViewById(R.id.ranking_per2)

        ranking_cate3 = findViewById(R.id.ranking_3)
        ranking_text3 = findViewById(R.id.ranking_text3)
        ranking_per3 = findViewById(R.id.ranking_per3)

        ranking_cate4 = findViewById(R.id.ranking_4)
        ranking_text4 = findViewById(R.id.ranking_text4)
        ranking_per4 = findViewById(R.id.ranking_per4)

        ranking_cate5 = findViewById(R.id.ranking_5)
        ranking_text5 = findViewById(R.id.ranking_text5)
        ranking_per5 = findViewById(R.id.ranking_per5)

        //만족도
        satisfaction_cate = findViewById(R.id.month_satis_cate)
        satisfaction_bname = findViewById(R.id.month_satis_bname)
        satisfaction_price = findViewById(R.id.month_satis_price)
        satisfaction_per = findViewById(R.id.month_satis_per)
        satisfaction_textS = findViewById(R.id.month_satis)
        satisfaction_textE = findViewById(R.id.month_satis_text)



        // 날짜 표시
        mFormat = SimpleDateFormat("yyyy년 MM월", Locale.KOREAN)
        currentDate = Date()
        monthText.text = mFormat.format(currentDate)

        month_btn.setOnClickListener { showMonthPickerDialog() } // 다른 달 선택 다이얼로그 표시

        //selectedMonth = Date() // 오늘 날짜로 초기화
        val calendar = Calendar.getInstance() // 현재 날짜와 시간으로 초기화된 Calendar 인스턴스를 가져옵니다.
        val year = calendar.get(Calendar.YEAR) // 연도를 가져옵니다.
        val month = calendar.get(Calendar.MONTH) + 1 // 월을 가져옵니다. (Calendar.MONTH는 0부터 시작하므로 +1을 해줍니다.)

        getDataForMonth(year,month) // 오늘 날짜 데이터 불러오기


        //가계부 버튼 클릭 시
        analyze_btn.setOnClickListener(View.OnClickListener{
            val intent = Intent(this, AnalyzeDayAct::class.java)
            startActivity(intent)
        })

        //일기 버튼 클릭 시
        analyzedi_btn.setOnClickListener(View.OnClickListener{
            val intent = Intent(this, AnalyzeDiaryAct::class.java)
            startActivity(intent)
        })

        // 탭 전환 시
        tab_analyze.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let { // tab이 null이 아닌 경우에만 실행
                    when(it.position) {
                        0 -> {
                            val intent = Intent(this@AnalyzeMonthAct, AnalyzeDayAct::class.java)
                            startActivity(intent)
                        }
                        1 -> {
                            val intent = Intent(this@AnalyzeMonthAct, AnalyzeWeekAct::class.java)
                            startActivity(intent)
                        }
                        2 -> {
                            val intent = Intent(this@AnalyzeMonthAct, AnalyzeMonthAct::class.java)
                            startActivity(intent)
                        }
                        3 -> {
                            val intent = Intent(this@AnalyzeMonthAct, AnalyzeyearlyAct::class.java)
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
            }
        })


        //하단바 클릭 시
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_view)
        bottomNavigation.setOnItemSelectedListener { item ->
            when(item.itemId){
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
                    //val intent = Intent(this, ExpendAct::class.java)
                    //startActivity(intent)
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
            }; true
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
                    apiobject.api.diarywriteEx(3,getCurrentFormattedDate()).enqueue(object : Callback<List<DataEx>> {
                        override fun onResponse(call: Call<List<DataEx>>, response: Response<List<DataEx>>) {
                            if (response.isSuccessful && response.body() != null) {
                                // 네트워크 응답이 성공적이고 데이터가 있는 경우
                                val intent = Intent(this@AnalyzeMonthAct, DiaryWriteAct::class.java)
                                startActivity(intent)
                            } else {
                                // 네트워크 응답이 실패했거나 데이터가 없는 경우
                                Toast.makeText(this@AnalyzeMonthAct, "이미 저장된 일기가 있습니다.", Toast.LENGTH_SHORT).show()
                                dialog.dismiss() // 다이얼로그 닫기
                            }
                        }

                        override fun onFailure(call: Call<List<DataEx>>, t: Throwable) {
                            // 네트워크 요청 실패 시
                            Toast.makeText(this@AnalyzeMonthAct, "네트워크 요청에 실패했습니다.", Toast.LENGTH_SHORT).show()
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

    private fun showMonthPickerDialog() {
        val calendar = Calendar.getInstance()

        // 다이얼로그에 표시할 월 목록 생성
        val months = (1..12).map { month -> "${String.format("%02d", month)}월" }.toTypedArray()

        AlertDialog.Builder(this)
            .setTitle("달 선택")
            .setItems(months) { _, which ->
                calendar.set(Calendar.MONTH, which)
                val year = calendar.get(Calendar.YEAR)
                val month = which + 1 // Calendar.MONTH는 0부터 시작하므로 +1 필요
                // 사용자가 선택한 달로 selectedMonth 업데이트
                selectedMonth = calendar.time
                monthText.text = SimpleDateFormat("yyyy년 MM월", Locale.getDefault()).format(selectedMonth)

                // 선택한 달에 해당하는 데이터로 업데이트
                getDataForMonth(year, month)
            }
            .show()
    }

    //데이터 가져오기
    private fun getDataForMonth(year: Int, month: Int) {
        val yearStr = year.toString()
        val monthStr = month.toString().padStart(2, '0')

        apiobject.api.getStatisticsMonthly(3, yearStr, monthStr).enqueue(object : Callback<ResponseStatMonth> {
            override fun onResponse(call: Call<ResponseStatMonth>, response: Response<ResponseStatMonth>) {
                if (response.isSuccessful) {
                    response.body()?.let { data ->
                        Log.d("데이터 받기", data.toString())
                        val monthlyCategory = createMonthlyCategory(data)
                        updateBarChart(monthlyCategory)
                        updateIncomeAndExpense(listOf(data), month)
                        getMonthStatistics(listOf(data))
                        month_bestsatisfaction(data)

                        val top5CategoriesWithRatio = getTop5CategoriesWithRatio(data.category!!.this_month)
                        showTop5CategoriesWithRatio(top5CategoriesWithRatio)
                    } ?: run {
                        // 응답은 성공적이지만 body가 null인 경우 처리
                        showToast("데이터가 비어있습니다.")
                        updateIncomeAndExpense(null, month)
                    }
                } else {
                    // 응답 실패 처리
                    showToast("데이터를 불러오지 못했습니다.")
                    updateIncomeAndExpense(null, month)
                }
            }

            override fun onFailure(call: Call<ResponseStatMonth>, t: Throwable) {
                // API 호출 실패 처리
                showToast("네트워크 오류가 발생했습니다.")
                updateIncomeAndExpense(null, month)
            }
        })
    }

    private fun showToast(message: String) {
        Toast.makeText(this@AnalyzeMonthAct, message, Toast.LENGTH_SHORT).show()
    }

    // MonthlyCategory 객체를 생성하는 함수
    private fun createMonthlyCategory(response: ResponseStatMonth): ResponseStatMonth.MonthlyCategory {
        // ResponseStatMonth에서 필요한 데이터를 추출하여 MonthlyCategory 객체를 생성합니다.
        val lastMonth = response.category!!.last_month?.map { ResponseStatMonth.MonthCategoryExpense(it.category, it.total_expense) } ?: emptyList()
        val thisMonth = response.category!!.this_month?.map { ResponseStatMonth.MonthCategoryExpense(it.category, it.total_expense) } ?: emptyList()

        // 필요한 로직을 여기에 추가합니다.
        return ResponseStatMonth.MonthlyCategory(lastMonth, thisMonth)
    }

    // 월별 총 수입과 지출을 표시하는 함수
    /*private fun updateIncomeAndExpense(data: List<ResponseStatMonth>?) {
        val currentMonthData = data?.firstOrNull { it.month == "apr" } // "apr" 월 데이터를 현재 월로 간주합니다.
        val totalIncome = currentMonthData?.totalIncome ?: 0
        val totalExpense = currentMonthData?.totalExpense ?: 0
        income_text.text = totalIncome.toString()
        expend_text.text = totalExpense.toString()
    }*/

    //4개월간의 수입지출 표시 함수
    private fun getMonthStatistics(data: List<ResponseStatMonth>?) {
        val calendar = Calendar.getInstance()
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentYear = calendar.get(Calendar.YEAR)
        val monthStatisticsList = mutableListOf<ResponseStatMonth.MonthStatistics>()

        data?.firstOrNull()?.let {
            for (i in 0 until 4) {
                val monthIndex = (currentMonth - i + 12) % 12
                val yearOffset = (currentMonth - i) / 12
                val relevantYear = currentYear + yearOffset

                val monthData = when (monthIndex) {
                    0 -> it.Jan
                    1 -> it.Feb
                    2 -> it.Mar
                    3 -> it.Apr
                    4 -> it.May
                    5 -> it.Jun
                    6 -> it.Jul
                    7 -> it.Aug
                    8 -> it.Sep
                    9 -> it.Oct
                    10 -> it.Nov
                    11 -> it.Dec
                    else -> null
                } ?: ResponseStatMonth.MonthStatistics(0, 0)

                monthStatisticsList.add(0, monthData) // 최근 데이터가 먼저 오도록 리스트 앞에 추가
            }
        }

        val expenseEntries = monthStatisticsList.mapIndexed { index, monthData ->
            BarEntry(index.toFloat() * 2, monthData.total_expense?.toFloat() ?: 0f) // 지출 데이터
        }

        val incomeEntries = monthStatisticsList.mapIndexed { index, monthData ->
            BarEntry(index.toFloat() * 2 + 1, monthData.total_income?.toFloat() ?: 0f) // 수입 데이터
        }

        val expenseDataSet = BarDataSet(expenseEntries, "지출")
        expenseDataSet.color = Color.parseColor("#F998B5") // 지출 색상 설정
        expenseDataSet.valueTextColor = Color.BLACK
        expenseDataSet.valueTextSize = 10f

        val incomeDataSet = BarDataSet(incomeEntries, "수입")
        incomeDataSet.color = Color.parseColor("#2F8FFF") // 수입 색상 설정
        incomeDataSet.valueTextColor = Color.BLACK
        incomeDataSet.valueTextSize = 10f

        val barData = BarData(expenseDataSet, incomeDataSet)
        barData.barWidth = 0.4f // 막대 너비 설정

        monthChart.data = barData

        // X축 설정
        val xAxis = monthChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.granularity = 2f
        xAxis.axisMinimum = 0f
        xAxis.axisMaximum = barData.xMax + 1f
        monthChart.axisRight.isEnabled = false
        //monthChart.axisLeft.isEnabled = false
        monthChart.legend.isEnabled = false //범례 지우기

        // 월 이름 배열 생성
        val months = listOf("1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월", "10월", "11월", "12월")
        val recentMonths = (0 until 4).map { (currentMonth - it + 12) % 12 }.map { months[it] }.reversed()
        xAxis.valueFormatter = IndexAxisValueFormatter(recentMonths)

        monthChart.groupBars(0f, 1f, 0.1f) // 그룹 간격 설정

        monthChart.invalidate() // 그래프 새로고침
    }

    // 월별 수입 지출 표시
    private fun updateIncomeAndExpense(data: List<ResponseStatMonth>?, selectedMonth: Int) {
        val monthStatistics = data?.firstOrNull()?.let {
            when (selectedMonth) {
                1 -> it.Jan
                2 -> it.Feb
                3 -> it.Mar
                4 -> it.Apr
                5 -> it.May
                6 -> it.Jun
                7 -> it.Jul
                8 -> it.Aug
                9 -> it.Sep
                10 -> it.Oct
                11 -> it.Nov
                12 -> it.Dec
                else -> null
            }
        }

        val totalIncome = monthStatistics?.total_income ?: 0
        val totalExpense = monthStatistics?.total_expense ?: 0

        val formatIncome = NumberFormat.getNumberInstance(Locale.KOREA).format(totalIncome)
        income_text.text = formatIncome

        val formatExpense = NumberFormat.getNumberInstance(Locale.KOREA).format(totalExpense)
        expend_text.text = formatExpense
    }

    // 이번달 카테고리 데이터에서 상위 5개 카테고리와 각 카테고리의 지출 비율을 추출하는 함수
    private fun getTop5CategoriesWithRatio(thisMonthData: List<ResponseStatMonth.MonthCategoryExpense>?): List<Pair<String, Float>> {
        // Null 체크 추가
        if (thisMonthData.isNullOrEmpty()) {
            // 데이터가 null이거나 비어 있으면 빈 리스트 반환
            return emptyList()
        }

        // 카테고리별 지출을 계산합니다.
        val categoryExpenseMap = mutableMapOf<String, Int>()
        for (categoryExpense in thisMonthData) {
            val expense = categoryExpense.total_expense ?: 0
            categoryExpenseMap[categoryExpense.category] = categoryExpenseMap.getOrDefault(categoryExpense.category, 0) + expense
        }

        // 지출이 많은 순서대로 정렬한 후 상위 5개의 카테고리를 추출합니다.
        val top5Categories = categoryExpenseMap.entries.sortedByDescending { it.value }.take(5)

        // 전체 지출 합계를 구합니다.
        val totalExpense = categoryExpenseMap.values.sum()
        if (totalExpense == 0) {
            // 전체 지출이 0이면 비율 계산이 불가능하므로 빈 리스트 반환
            return emptyList()
        }

        // 각 카테고리의 지출 비율을 계산합니다.
        return top5Categories.map { it.key to it.value.toFloat() / totalExpense }
    }


    // UI에 상위 5개 카테고리와 해당 비율을 표시하는 함수
    private fun showTop5CategoriesWithRatio(top5CategoriesWithRatio: List<Pair<String, Float>>) {

        val categoryMap = mapOf(
            "tax" to "세금",
            "food" to "음식",
            "housing/communication" to "주거/통신",
            "transportation/vehicle" to "교통/차량",
            "education" to "교육",
            "personal event" to "경조사/회비",
            "medical" to "병원/약국",
            "cultural/living" to "문화생활",
            "shopping" to "쇼핑",
            "etc" to "기타"
        )
        // 각 순위별로 UI에 표시할 View와 Pair<String, Float> 데이터를 연결합니다.
        val rankingViews = listOf(
            Pair(ranking_text1, ranking_per1),
            Pair(ranking_text2, ranking_per2),
            Pair(ranking_text3, ranking_per3),
            Pair(ranking_text4, ranking_per4),
            Pair(ranking_text5, ranking_per5)
        )

        for (i in rankingViews.indices) {
            if (i < top5CategoriesWithRatio.size) {
                val (categoryView, percentView) = rankingViews[i] // 순위에 해당하는 View들을 가져옵니다.
                val (category, ratio) = top5CategoriesWithRatio[i] // 상위 카테고리와 해당 비율을 가져옵니다.
                val categoryName = categoryMap[category] ?: category

                // View에 데이터를 설정합니다.
                categoryView.text = categoryName // 카테고리 이름 설정
                percentView.text = "${(ratio * 100).toInt()}%" // 비율 설정 (정수만)
            } else {
                // 데이터가 부족한 경우 빈 문자열로 설정
                val (categoryView, percentView) = rankingViews[i]
                categoryView.text = ""
                percentView.text = ""
            }
        }
    }

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

    private fun updateBarChart(categoryData: ResponseStatMonth.MonthlyCategory) {
        val lastMonthExpenses = categoryData.last_month!!.associateBy { it.category }
        val thisMonthExpenses = categoryData.this_month!!.associateBy { it.category }

        var maxIncreaseCategory = ""
        var maxIncreaseAmount = 0f

        // 최대 증가 카테고리 찾기
        thisMonthExpenses.forEach { (_, expense) ->
            val category = expense.category
            val thisMonthExpense = expense.total_expense ?: 0

            val lastMonthExpense = lastMonthExpenses[category]?.total_expense ?: 0
            val increasedExpense = thisMonthExpense - lastMonthExpense

            if (increasedExpense > maxIncreaseAmount) {
                maxIncreaseCategory = category
                maxIncreaseAmount = increasedExpense.toFloat()
            }
        }

        // 최대 증가 카테고리가 있는 경우에만 BarEntry 생성
        val barEntries = mutableListOf<BarEntry>()
        if (maxIncreaseCategory.isNotEmpty()) {
            barEntries.add(BarEntry(0f, maxIncreaseAmount, maxIncreaseCategory))
        }

        val dataSet = BarDataSet(barEntries, "지출 증가 카테고리")
        dataSet.color = Color.parseColor("#6CB0FF")
        dataSet.valueTextColor = Color.BLACK
        dataSet.valueTextSize = 10f

        val barData = BarData(dataSet)
        barData.barWidth = 0.1f// 막대 바를 얇게 설정 (0.2f는 예시값, 필요에 따라 조정 가능)
        mon_cateChart.data = barData

        // X축 설정
        val xAxis = mon_cateChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.granularity = 1f
        mon_cateChart.legend.isEnabled = false
        mon_cateChart.axisRight.isEnabled = false
        mon_cateChart.axisLeft.isEnabled = false

        // 카테고리 이름을 X축 레이블로 사용
        xAxis.valueFormatter = IndexAxisValueFormatter(barEntries.map { it.data as String })

        mon_cateChart.invalidate() // 차트 새로고침

        // 카테고리 이름을 한글로 변환
        val maxIncreaseCategoryKorean = categoryMap[maxIncreaseCategory] ?: maxIncreaseCategory

        // 가장 많이 증가한 카테고리를 텍스트 뷰에 설정
        val moreCateTextView: TextView = findViewById(R.id.more_cate)
        moreCateTextView.text = "지난달보다\n '$maxIncreaseCategoryKorean' 지출이\n 가장 많이 늘었어요!"
    }


    private fun month_bestsatisfaction(monthData: ResponseStatMonth) {
        val satisfactionItem = monthData.satisfaction?.satisfaction
        if (satisfactionItem != null) {
            // 카테고리 매핑을 위한 맵
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

            val category = satisfactionItem.category
            val bname = satisfactionItem.bname
            val formattedPrice = NumberFormat.getNumberInstance(Locale.KOREA).format(satisfactionItem.price)
            //val price = satisfactionItem.price
            val satisfaction = satisfactionItem.satisfaction

            // UI에 데이터를 표시합니다.
            satisfaction_cate.text = categoryMap[category] ?: "분류 없음" // 매핑된 카테고리로 표시, 매핑 실패 시 "분류 없음"
            satisfaction_bname.text = bname
            satisfaction_price.text = formattedPrice
            satisfaction_per.text = satisfaction.toString() + "%"
        } else {
            // 만족도 데이터가 없는 경우에 대한 처리
            satisfaction_cate.text = "데이터없음"
            satisfaction_bname.text = "데이터없음"
            satisfaction_price.text = "데이터없음"
            satisfaction_per.text = "데이터없음"
        }
    }

}
