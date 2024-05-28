package com.example.financiallog

import android.annotation.SuppressLint
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
                    apiobject.api.diarywriteEx(6,getCurrentFormattedDate()).enqueue(object : Callback<List<DataEx>> {
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

    private var selectedMonth: Date = Date() // 초기화

    private fun showMonthPickerDialog() {
        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)

        // 다이얼로그에 표시할 월 목록 생성
        val months = (1..12).map { month -> "${String.format("%02d", month)}월" }.toTypedArray()

        AlertDialog.Builder(this)
            .setTitle("달 선택")
            .setItems(months) { dialog, which ->
                calendar.set(Calendar.MONTH, which)
                currentDate = calendar.time
                monthText.text = mFormat.format(currentDate)

                // 선택한 달에 해당하는 데이터로 업데이트
                getDataForMonth(selectedMonth)
            }
            .show()
    }

    private fun getDataForMonth(date: Date) {
        val formattedDate = SimpleDateFormat("yyyy-MM", Locale.getDefault()).format(date)

        apiobject.api.getStatisticsMonthly(6, formattedDate).enqueue(object : Callback<List<ResponseStatMonth>> {
            override fun onResponse(
                call: Call<List<ResponseStatMonth>>,
                response: Response<List<ResponseStatMonth>>
            ) {
                if (response.isSuccessful) {
                    val data = response.body()
                    data?.let {
                        val monthlyCategory = createMonthlyCategory(it[0])
                        // updateBarChart 함수에 생성한 MonthlyCategory 객체를 전달합니다.

                        updateBarChart(monthlyCategory)
                        updateIncomeAndExpense(it)
                        getMonthStatistics(it)

                        // 이번달 카테고리 데이터를 처리하여 상위 5개 카테고리와 각 카테고리의 지출 비율을 추출합니다.
                        val top5CategoriesWithRatio = getTop5CategoriesWithRatio(it[0].category.thisMonth)
                        // UI에 상위 5개 카테고리와 해당 비율을 표시하는 함수를 호출합니다.
                        showTop5CategoriesWithRatio(top5CategoriesWithRatio)

                        month_bestsatisfaction(it)
                    }
                } else {
                    // API 호출 실패 처리
                    Toast.makeText(this@AnalyzeMonthAct, "데이터를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show()
                    updateIncomeAndExpense(null)
                }
            }

            override fun onFailure(call: Call<List<ResponseStatMonth>>, t: Throwable) {
                // API 호출 실패 처리
                Toast.makeText(this@AnalyzeMonthAct, "네트워크 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
                updateIncomeAndExpense(null)
            }
        })
    }

    // MonthlyCategory 객체를 생성하는 함수
    private fun createMonthlyCategory(data: ResponseStatMonth): ResponseStatMonth.MonthlyCategory {
        // ResponseStatMonth에서 필요한 데이터를 추출하여 MonthlyCategory 객체를 생성합니다.
        val lastMonth = data.category.lastMonth.map { ResponseStatMonth.MonthCategoryExpense(it.category, it.totalExpense) }
        val thisMonth = data.category.thisMonth.map { ResponseStatMonth.MonthCategoryExpense(it.category, it.totalExpense) }
        return ResponseStatMonth.MonthlyCategory(lastMonth, thisMonth)
    }

    //월별 수입 지출 표시
    private fun updateIncomeAndExpense(data: List<ResponseStatMonth>?) {
        val currentMonth = Calendar.getInstance().get(Calendar.MONTH)
        val monthStatistics = data?.firstOrNull()?.let {
            when (currentMonth) {
                0 -> it.jan
                1 -> it.feb
                2 -> it.mar
                3 -> it.apr

                else -> null
            }
        }
        val totalIncome = monthStatistics?.totalIncome ?: 0
        val totalExpense = monthStatistics?.totalExpense ?: 0
        income_text.text = totalIncome.toString()
        expend_text.text = totalExpense.toString()
    }


    // 월별 총 수입과 지출을 표시하는 함수
    /*private fun updateIncomeAndExpense(data: List<ResponseStatMonth>?) {
        val currentMonthData = data?.firstOrNull { it.month == "apr" } // "apr" 월 데이터를 현재 월로 간주합니다.
        val totalIncome = currentMonthData?.totalIncome ?: 0
        val totalExpense = currentMonthData?.totalExpense ?: 0
        income_text.text = totalIncome.toString()
        expend_text.text = totalExpense.toString()
    }*/


    //4개의 월별 그래프
    private fun getMonthStatistics(data: List<ResponseStatMonth>?) {
        // 그래프를 그릴 데이터를 준비합니다.
        val currentMonth = Calendar.getInstance().get(Calendar.MONTH)
        val monthStatisticsList = mutableListOf<ResponseStatMonth.MonthStatistics>()

        data?.firstOrNull()?.let {
            monthStatisticsList.add(it.jan)
            monthStatisticsList.add(it.feb)
            monthStatisticsList.add(it.mar)
            monthStatisticsList.add(it.apr)
        }


        // 막대 그래프를 업데이트합니다.
        val barEntries = monthStatisticsList.mapIndexed { index, monthData ->
            BarEntry(index.toFloat(),
                monthData.totalExpense?.toFloat() ?: 0f, // totalExpense가 null이면 0을 사용
                monthData.totalIncome?.toFloat() ?: 0f    // totalIncome이 null이면 0을 사용
            )
        }

        val dataSet = BarDataSet(barEntries, "월별 지출")
        dataSet.colors = ColorTemplate.MATERIAL_COLORS.toList()
        dataSet.valueTextColor = Color.BLACK
        dataSet.valueTextSize = 10f

        val barData = BarData(dataSet)
        monthChart.data = barData

        // X축 설정
        val xAxis = monthChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.granularity = 1f
        xAxis.valueFormatter = IndexAxisValueFormatter(barEntries.map { it.data as String })

        monthChart.invalidate() // 그래프 새로고침
    }


    // 이번달 카테고리 데이터에서 상위 5개 카테고리와 각 카테고리의 지출 비율을 추출하는 함수
    private fun getTop5CategoriesWithRatio(thisMonthData: List<ResponseStatMonth.MonthCategoryExpense>): List<Pair<String, Float>> {
        // 카테고리별 지출을 계산합니다.
        val categoryMap = mutableMapOf<String, Int>()
        for (categoryExpense in thisMonthData) {
            categoryMap[categoryExpense.category] = categoryMap.getOrDefault(categoryExpense.category, 0) + (categoryExpense.totalExpense ?: 0)
        }
        // 지출이 많은 순서대로 정렬한 후 상위 5개의 카테고리를 추출합니다.
        val top5Categories = categoryMap.entries.sortedByDescending { it.value }.take(5)
        // 전체 지출 합계를 구합니다.
        val totalExpense = categoryMap.values.sum()
        // 각 카테고리의 지출 비율을 계산합니다.
        return top5Categories.map { it.key to it.value.toFloat() / totalExpense }
    }

    // UI에 상위 5개 카테고리와 해당 비율을 표시하는 함수
    private fun showTop5CategoriesWithRatio(top5CategoriesWithRatio: List<Pair<String, Float>>) {
        // 각 순위별로 UI에 표시할 View와 Pair<String, Float> 데이터를 연결합니다.

        val rankingViews = listOf(
            Pair(ranking_cate1, ranking_per1),
            Pair(ranking_cate2, ranking_per2),
            Pair(ranking_cate3, ranking_per3),
            Pair(ranking_cate4, ranking_per4),
            Pair(ranking_cate5, ranking_per5)
        )

        for (i in top5CategoriesWithRatio.indices) {
            if (i >= rankingViews.size) {
                break // 상위 5개 이상의 데이터가 있으면 루프를 중단합니다.
            }
            val (categoryView, percentView) = rankingViews[i] // 순위에 해당하는 View들을 가져옵니다.
            val (category, ratio) = top5CategoriesWithRatio[i] // 상위 카테고리와 해당 비율을 가져옵니다.

            // View에 데이터를 설정합니다.
            categoryView.text = category // 카테고리 이름 설정
            percentView.text = "${(ratio * 100).toInt()}%" // 비율 설정 (소수점 버리고 정수로 표시)

            //percentView.text = "${Math.round(ratio * 100)}%" // 비율 설정 (반올림하여 정수로 표시)
            //percentView.text = String.format("%.2f", ratio * 100) + "%" // 비율 설정 (소수점 2자리까지)
        }
    }


    //지난달 대비 증가 카테고리
    private fun updateBarChart(categoryData: ResponseStatMonth.MonthlyCategory) {
        val barEntries = mutableListOf<BarEntry>()
        val lastMonthExpenses = categoryData.lastMonth.associateBy { it.category }
        val thisMonthExpenses = categoryData.thisMonth.associateBy { it.category }

        thisMonthExpenses.forEach { index, expense ->
            val category = expense.category
            val thisMonthExpense = expense.totalExpense ?: 0

            val lastMonthExpense = lastMonthExpenses[category]?.totalExpense ?: 0
            val increasedExpense = thisMonthExpense - lastMonthExpense

            if (increasedExpense > 0) {
                barEntries.add(BarEntry(index.toFloat(), increasedExpense.toFloat(), category))
            }
        }


        val dataSet = BarDataSet(barEntries, "지출 증가 카테고리")
        dataSet.colors = ColorTemplate.MATERIAL_COLORS.toList()
        dataSet.valueTextColor = Color.BLACK
        dataSet.valueTextSize = 10f

        val barData = BarData(dataSet)
        mon_cateChart.data = barData

        // X축 설정
        val xAxis = mon_cateChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.granularity = 1f
        xAxis.valueFormatter = IndexAxisValueFormatter(barEntries.map { it.data as String })

        mon_cateChart.invalidate() // Refresh the chart
    }


    private fun month_bestsatisfaction(monthData: List<ResponseStatMonth>) {
        val satisfactionData = monthData.firstOrNull()?.satisfaction

        if (satisfactionData != null) {
            val satisfactionItem = satisfactionData.satisfaction
            if (satisfactionItem != null) { // satisfactionItem이 null이 아닌 경우에만 처리
                val category = satisfactionItem.category
                val bname = satisfactionItem.bname
                val price = satisfactionItem.price
                val satisfaction = satisfactionItem.satisfaction

                // UI에 데이터를 표시합니다.
                satisfaction_cate.text = category
                satisfaction_bname.text = bname
                satisfaction_price.text = price.toString()
                satisfaction_per.text = satisfaction.toString()
            } else {
                // 만족도 데이터가 없는 경우에 대한 처리를 여기에 추가합니다.
                satisfaction_cate.text = "데이터없음"
                satisfaction_bname.text = "데이터없음"
                satisfaction_price.text = "데이터없음"
                satisfaction_per.text = "데이터없음"
            }
        } else {
            // 만족도 데이터가 없는 경우에 대한 처리를 여기에 추가합니다.
            satisfaction_cate.text = "없음"
            satisfaction_price.text = "없음"
            satisfaction_per.text = "없음"
        }
    }


}
