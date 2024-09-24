package com.example.financiallog

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
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
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
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
        year_satText.text = mFormat.format(currentDate)+ " \n만족했던 소비"
        year_expText.text = mFormat.format(currentDate)+ " 소비금액"

        year_btn.setOnClickListener { showYearPickerDialog() } // 다른 달 선택 다이얼로그 표시

        //apiobject = ApiObject()

        loadDataAndUpdateChart(selectedYear)


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
                    apiobject.api.diarywriteEx(6,getCurrentFormattedDate()).enqueue(object :
                        Callback<List<DataEx>> {
                        override fun onResponse(call: Call<List<DataEx>>, response: Response<List<DataEx>>) {
                            if (response.isSuccessful && response.body() != null) {
                                // 네트워크 응답이 성공적이고 데이터가 있는 경우
                                val intent = Intent(this@AnalyzeyearlyAct, DiaryWriteAct::class.java)
                                startActivity(intent)
                            } else {
                                // 네트워크 응답이 실패했거나 데이터가 없는 경우
                                Toast.makeText(this@AnalyzeyearlyAct, "이미 저장된 일기가 있습니다.", Toast.LENGTH_SHORT).show()
                                dialog.dismiss() // 다이얼로그 닫기
                            }
                        }

                        override fun onFailure(call: Call<List<DataEx>>, t: Throwable) {
                            // 네트워크 요청 실패 시
                            Toast.makeText(this@AnalyzeyearlyAct, "네트워크 요청에 실패했습니다.", Toast.LENGTH_SHORT).show()
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

    //private var selectedYear: Date = Date() // 초기화
    private var selectedYear: Int = Calendar.getInstance().get(Calendar.YEAR)

    private fun showYearPickerDialog() {
        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)

        val years = (currentYear downTo currentYear - 10).map { it.toString() }.toTypedArray()

        AlertDialog.Builder(this)
            .setTitle("연도 선택")
            .setItems(years) { dialog, which ->
                selectedYear = years[which].toInt() // 전역 변수 업데이트
                yearText.text = "$selectedYear"+"년"
                year_satText.text = "$selectedYear \n만족했던 소비"
                year_expText.text = "$selectedYear 소비금액"

                // 선택된 연도를 인자로 전달하여 데이터를 로드
                loadDataAndUpdateChart(selectedYear)
            }
            .show()
    }

    private fun loadDataAndUpdateChart(year: Int) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    apiobject.api.getStatisticsYearly(year.toString()).execute()
                }
                if (response.isSuccessful) {
                    val data = response.body()
                    Log.d("데이터 받기", data.toString())
                    if (data != null) {
                        updateRadarChart(data)
                        updateBarChart(data)
                        updateBarChartCate(data)
                    } else {
                        showError("데이터를 불러오는 데 실패했습니다.")
                    }
                } else {
                    showError("서버 응답이 실패했습니다.")
                }
            } catch (e: Exception) {
                Log.d("오류", "오류${e.message}")
                showError("데이터를 불러오는 중 오류가 발생했습니다.")
            }
        }
    }

    private fun updateBarChart(data: ResponseStatYear) {
        val incomeEntries = ArrayList<BarEntry>()
        val expenseEntries = ArrayList<BarEntry>()

        val monthsData = listOf(
            data.Jan, data.Feb, data.Mar,
            data.Apr, data.May, data.Jun,
            data.Jul, data.Aug, data.Sep,
            data.Oct, data.Nov, data.Dec
        )

        // 데이터 모델에서 차트에 맞게 데이터 추출
        monthsData.forEachIndexed { index, monthData ->
            val totalIncome = monthData.total_income ?: 0
            val totalExpense = monthData.total_expense ?: 0

            incomeEntries.add(BarEntry(index.toFloat(), totalIncome.toFloat()))
            expenseEntries.add(BarEntry(index.toFloat(), totalExpense.toFloat()))
        }

        val incomeDataSet = BarDataSet(incomeEntries, "수입").apply {
            color = Color.parseColor("#4c8df7")
        }

        val expenseDataSet = BarDataSet(expenseEntries, "지출").apply {
            color = Color.parseColor("#e49995")
        }

        val barData = BarData(incomeDataSet, expenseDataSet)
        year_barChart.data = barData

        /*// 차트 설명 설정
        year_barChart.description.apply {
            text = "(단위:만원)"
            textSize = 8f
            textColor = Color.BLACK
            setPosition(year_barChart.width.toFloat() - 80f, 20f)
        }*/

        // X축 설정
        val xAxis = year_barChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.labelRotationAngle = -40f
        xAxis.setDrawGridLines(false)
        xAxis.valueFormatter = IndexAxisValueFormatter(getMonths())
        xAxis.granularity = 1f // 레이블 간격을 1로 설정하여 모든 레이블이 표시되도록 함
        xAxis.labelCount = getMonths().size // 레이블의 개수를 카테고리 수와 동일하게 설정
        year_barChart.axisRight.isEnabled = false
        year_barChart.legend.isEnabled = false
//        // X축 설정
//        val xAxis = year_barChart.xAxis
//        xAxis.position = XAxis.XAxisPosition.BOTTOM
//        xAxis.labelRotationAngle = -10f
//        xAxis.setDrawGridLines(false)
//        xAxis.valueFormatter = IndexAxisValueFormatter(getMonths())
//        year_barChart.axisRight.isEnabled = false // 오른쪽 Y축은 비활성화합니다.
//        year_barChart.legend.isEnabled = false

        // 바 차트 스타일 설정
        val barWidth = 0.2f
        barData.barWidth = barWidth

        // 그룹화 설정
        year_barChart.barData.groupBars(0f, 0.4f, 0.1f)

        // 차트 다시 그리기
        year_barChart.notifyDataSetChanged()
        year_barChart.invalidate()
    }

    private fun getMonths(): List<String> {
        return listOf("1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월", "10월", "11월", "12월")
    }



    private fun showError(message: String) {
        // 여기에 오류 메시지를 사용자에게 표시하는 로직을 추가하세요.
        Toast.makeText(this@AnalyzeyearlyAct, message, Toast.LENGTH_SHORT).show()
    }


    private fun updateRadarChart(data: ResponseStatYear) {
        val radarEntries = ArrayList<RadarEntry>()
        val categories = ArrayList<String>()

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

        // 데이터 모델에서 차트에 맞게 데이터 추출
        val yearlySatisfactionList = data.satisfaction.yearly
        yearlySatisfactionList.forEach { satisfactionItem ->
            // 각 항목의 만족도 값을 추출하여 레이더 차트에 추가
            val satisfactionValue = satisfactionItem.average_satisfaction.toFloat()
            println(satisfactionItem.category + " " + satisfactionItem.average_satisfaction)
            radarEntries.add(RadarEntry(satisfactionValue))
            // 카테고리 이름을 매핑하여 추가
            categories.add(categoryMap[satisfactionItem.category] ?: satisfactionItem.category) // 매핑 실패 시 원본 카테고리 이름 사용
        }

        val radarDataSet = RadarDataSet(radarEntries, "만족도")


//        radarDataSet.lineWidth = 2f // 기본값은 1f

        radarDataSet.color = Color.parseColor("#4c8df7")
        radarDataSet.setDrawFilled(true) // 채우기 설정
        radarDataSet.fillColor = Color.parseColor("#4c8df7") // 채우기 색상
        radarDataSet.fillAlpha = 100 // 채우기 투명도 (0-255, 0은 완전 투명, 255는 불투명)
        radarDataSet.valueTextColor = Color.BLACK
        radarDataSet.valueTextSize = 10f

        val radarData = RadarData(radarDataSet)

        // 레이더 차트에 데이터를 설정하고 다시 그리기
        sat_raderChart.data = radarData

        // Y축 범위 설정
        val yAxis = sat_raderChart.yAxis
        yAxis.axisMinimum = 0f
        yAxis.axisMaximum = 100f

        // X축 설정
        val xAxis = sat_raderChart.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(categories) // 매핑된 카테고리 이름을 x축 라벨로 설정
        xAxis.textSize = 10f
        xAxis.spaceMin = 1f
        xAxis.spaceMax = 1f
        xAxis.textColor = Color.BLACK
        sat_raderChart.legend.isEnabled = false

        // 레이아웃 파라미터 설정
        val layoutParams = sat_raderChart.layoutParams as LinearLayout.LayoutParams
        layoutParams.gravity = Gravity.CENTER // 레이더 차트를 중앙에 배치
        sat_raderChart.layoutParams = layoutParams

        // 차트 다시 그리기
        sat_raderChart.invalidate()
    }



    private fun updateBarChartCate(data: ResponseStatYear) {
        val barEntries = mutableListOf<BarEntry>()

        // 카테고리별 연간 지출 데이터 추출
//        data.category.yearly.forEachIndexed { index, item ->
//            val category = item.category
//            val totalExpense = item.total_expense // 연별 지출을 가져옴
//            println(category + " " + totalExpense.toString())
//            barEntries.add(BarEntry(index.toFloat(), totalExpense.toFloat()))
//        }

        val categoryOrder = listOf(
            "tax", "food", "housing/communication", "transportation/vehicle",
            "education", "personal event", "medical", "cultural/living",
            "shopping", "etc"
        )

        categoryOrder.forEachIndexed { index, category ->
            val item = data.category.yearly.find { it.category == category }
            val totalExpense = item?.total_expense ?: 0 // 해당 카테고리가 없으면 0으로 설정
            barEntries.add(BarEntry(index.toFloat(), totalExpense.toFloat()))
        }

        val barDataSet = BarDataSet(barEntries, "카테고리별 소비 금액")

        // 차트 스타일 등 설정
        barDataSet.color = Color.parseColor("#e49995")
        barDataSet.valueTextColor = Color.BLACK

        val barData = BarData(barDataSet)

        // X축 설정
        val xAxis = expend_barChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.labelRotationAngle = -40f
        xAxis.setDrawGridLines(false)
        xAxis.valueFormatter = IndexAxisValueFormatter(getCategory())
        xAxis.granularity = 1f // 레이블 간격을 1로 설정하여 모든 레이블이 표시되도록 함
        xAxis.labelCount = getCategory().size // 레이블의 개수를 카테고리 수와 동일하게 설정
        expend_barChart.axisRight.isEnabled = false
        expend_barChart.legend.isEnabled = false

        expend_barChart.data = barData
        expend_barChart.invalidate() // 차트 다시 그리기
    }
    private fun getCategory(): List<String> {
        return listOf("세금","음식","주거통신","교통","교육", "경조사","의료","문화생활","쇼핑","기타"
        )
    }
}


