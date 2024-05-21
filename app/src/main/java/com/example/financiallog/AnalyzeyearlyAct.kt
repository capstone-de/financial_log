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
import java.util.Date
import java.util.Locale

import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.RadarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.RadarData
import com.github.mikephil.charting.data.RadarDataSet
import com.github.mikephil.charting.data.RadarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import java.util.*


class AnalyzeyearlyAct: AppCompatActivity() {

    lateinit var yearText: TextView
    lateinit var mFormat: SimpleDateFormat
    lateinit var currentDate: Date
    lateinit var year_satText: TextView
    lateinit var year_expText: TextView
    lateinit var year_barChart: BarChart
    lateinit var sat_raderChart: RadarChart
    lateinit var expend_barChart: BarChart

    val apiobject : ApiObject by lazy { ApiObject() };

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.analyze_yearly)

        val analyze_btn = findViewById<Button>(R.id.btn_analyze)
        val analyzedi_btn = findViewById<Button>(R.id.btn_diary)

        val tab_analyze =findViewById<TabLayout>(R.id.tabLayout)
        val tab_day = findViewById<TabLayout>(R.id.day)
        val tab_week =findViewById<TabLayout>(R.id.week)
        val tab_month = findViewById<TabLayout>(R.id.month)
        val tab_yearly = findViewById<TabLayout>(R.id.yearly)

        val year_btn = findViewById<ImageButton>(R.id.underButton)

        yearText = findViewById(R.id.yearly_tv)
        year_satText = findViewById(R.id.tv_yearly_sat)
        year_expText = findViewById(R.id.tv_yearly_exp)

        year_barChart = findViewById(R.id.chart)
        sat_raderChart = findViewById(R.id.chart3)
        expend_barChart = findViewById(R.id.chart2)

        // 날짜 표시
        mFormat = SimpleDateFormat("yyyy년", Locale.KOREAN)
        currentDate = Date()
        yearText.text = mFormat.format(currentDate)
        year_satText.text = mFormat.format(currentDate)
        year_expText.text = mFormat.format(currentDate)

        year_btn.setOnClickListener { showYearPickerDialog() } // 다른 달 선택 다이얼로그 표시

        //apiobject = ApiObject()

        loadDataAndUpdateChart()


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
                            val intent = Intent(this@AnalyzeyearlyAct, AnalyzeDayAct::class.java)
                            startActivity(intent)
                        }
                        1 -> {
                            val intent = Intent(this@AnalyzeyearlyAct, AnalyzeWeekAct::class.java)
                            startActivity(intent)
                        }
                        2 -> {
                            val intent = Intent(this@AnalyzeyearlyAct, AnalyzeMonthAct::class.java)
                            startActivity(intent)
                        }
                        3 -> {
                            val intent = Intent(this@AnalyzeyearlyAct, AnalyzeyearlyAct::class.java)
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
                    when(it.position) {
                        0 -> {
                            val intent = Intent(this@AnalyzeyearlyAct, AnalyzeDayAct::class.java)
                            startActivity(intent)
                        }
                        1 -> {
                            val intent = Intent(this@AnalyzeyearlyAct, AnalyzeWeekAct::class.java)
                            startActivity(intent)
                        }
                        2 -> {
                            val intent = Intent(this@AnalyzeyearlyAct, AnalyzeMonthAct::class.java)
                            startActivity(intent)
                        }
                        3 -> {
                            val intent = Intent(this@AnalyzeyearlyAct, AnalyzeyearlyAct::class.java)
                            startActivity(intent)
                        }
                    }
                }
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
    private fun showYearPickerDialog() {
        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)

        val years = (currentYear downTo currentYear - 10).map { it.toString() }.toTypedArray()

        AlertDialog.Builder(this)
            .setTitle("연도 선택")
            .setItems(years) { dialog, which ->
                val selectedYear = years[which].toInt()
                yearText.text = selectedYear.toString() + "년"
                // 여기서 선택한 연도에 해당하는 데이터를 가져오는 함수를 호출하면 됩니다.
                //fetchYearlyData(selectedYear)
            }
            .show()
    }

    private fun loadDataAndUpdateChart() {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                // 백그라운드 스레드에서 API 호출
                val response = withContext(Dispatchers.IO) {
                    apiobject.api.getStatisticsYearly().execute()
                }
                if (response.isSuccessful) {
                    val data = response.body()
                    if (data != null) {
                        updateRadarChart(data)
                        updateBarChart(data)
                        updateBarChartCate(data)
                    } else {
                        Toast.makeText(this@AnalyzeyearlyAct, "데이터가 null입니다.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@AnalyzeyearlyAct, "API 호출이 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                // 오류 처리
                Toast.makeText(this@AnalyzeyearlyAct, "데이터를 가져오는 동안 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }
        }
    }
    //만족했던 카테고리별 레이더 차트
    private fun updateRadarChart(data: List<ResponseStatYear>) {
        val radarEntries = ArrayList<RadarEntry>()

        // 데이터 모델에서 차트에 맞게 데이터 추출
        data.forEach { item ->
            val yearlySatisfactionList = item.satisfaction.yearly
            yearlySatisfactionList.forEach { satisfactionItem ->
                // 각 항목의 만족도 값을 추출하여 레이더 차트에 추가
                val satisfactionValue = satisfactionItem.averageSatisfaction.toFloat()
                radarEntries.add(RadarEntry(satisfactionValue))
            }
        }

        val radarDataSet = RadarDataSet(radarEntries, "만족도")

        // 차트 스타일 등 설정
        radarDataSet.color = Color.BLUE
        radarDataSet.valueTextColor = Color.BLACK

        val radarData = RadarData(radarDataSet)
        sat_raderChart.data = radarData
        sat_raderChart.invalidate() // 차트 다시 그리기
    }

    //수입, 지출별 월통계 바차트
    private fun updateBarChart(data: List<ResponseStatYear>) {
        val barEntries = ArrayList<BarEntry>()

        // 데이터 모델에서 차트에 맞게 데이터 추출
        data.forEachIndexed { index, item ->
            val totalIncome = when (index) {
                0 -> item.jan.totalIncome ?: 0
                1 -> item.feb.totalIncome ?: 0
                2 -> item.mar.totalIncome ?: 0
                3 -> item.apr.totalIncome ?: 0
                4 -> item.may.totalIncome ?: 0
                5 -> item.jun.totalIncome ?: 0
                6 -> item.jul.totalIncome ?: 0
                7 -> item.aug.totalIncome ?: 0
                8 -> item.sep.totalIncome ?: 0
                9 -> item.oct.totalIncome ?: 0
                10 -> item.nov.totalIncome ?: 0
                11 -> item.dec.totalIncome ?: 0
                else -> 0
            }

            val totalExpense = when (index) {
                0 -> item.jan.totalExpense ?: 0
                1 -> item.feb.totalExpense ?: 0
                2 -> item.mar.totalExpense ?: 0
                3 -> item.apr.totalExpense ?: 0
                4 -> item.may.totalExpense ?: 0
                5 -> item.jun.totalExpense ?: 0
                6 -> item.jul.totalExpense ?: 0
                7 -> item.aug.totalExpense ?: 0
                8 -> item.sep.totalExpense ?: 0
                9 -> item.oct.totalExpense ?: 0
                10 -> item.nov.totalExpense ?: 0
                11 -> item.dec.totalExpense ?: 0
                else -> 0
            }

            barEntries.add(BarEntry(index.toFloat(), totalIncome.toFloat(), totalExpense.toFloat()))
        }


        val barDataSet = BarDataSet(barEntries, "월별 수입")

        // 차트 스타일 등 설정
        barDataSet.color = Color.GREEN
        barDataSet.valueTextColor = Color.BLACK

        val barData = BarData(barDataSet)

        // X축 설정
        val xAxis = year_barChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.labelRotationAngle = -45f
        xAxis.setDrawGridLines(false)
        xAxis.valueFormatter = IndexAxisValueFormatter(getMonths())

        year_barChart.data = barData
        year_barChart.invalidate() // 차트 다시 그리기
    }

    private fun getMonths(): List<String> {
        return listOf("1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월", "10월", "11월", "12월")
    }

    //카테고리별 소비금액 바 차트
    private fun updateBarChartCate(data: List<ResponseStatYear>) {
        val barEntries = ArrayList<BarEntry>()

        // 데이터 모델에서 차트에 맞게 데이터 추출
        data.forEachIndexed { index, item ->
            val totalExpense = item.category.yearly[index].totalExpense // 월별 지출을 가져옴
            barEntries.add(BarEntry(index.toFloat(), totalExpense.toFloat()))
        }

        /*val radarEntries = ArrayList<RadarEntry>()

        // 데이터 모델에서 차트에 맞게 데이터 추출
        data.forEach { item ->
            val yearlySatisfactionList = item.satisfaction.yearly
            yearlySatisfactionList.forEach { satisfactionItem ->
                // 각 항목의 만족도 값을 추출하여 레이더 차트에 추가
                val satisfactionValue = satisfactionItem.averageSatisfaction.toFloat()
                radarEntries.add(RadarEntry(satisfactionValue))
            }
        }*/

        val barDataSet = BarDataSet(barEntries, "카테고리별 소비 금액")

        // 차트 스타일 등 설정
        barDataSet.color = Color.RED
        barDataSet.valueTextColor = Color.BLACK

        val barData = BarData(barDataSet)

        // X축 설정
        val xAxis = expend_barChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.labelRotationAngle = -45f
        xAxis.setDrawGridLines(false)
        xAxis.valueFormatter = IndexAxisValueFormatter(getCategory())

        expend_barChart.data = barData
        expend_barChart.invalidate() // 차트 다시 그리기
    }
    private fun getCategory(): List<String> {
        return listOf("식비", "문화생활", "교통", "생활용품", "마트/편의점", "주거/통신", "교육",
            "경조사/회비", "쇼핑", "기타")
    }


}


