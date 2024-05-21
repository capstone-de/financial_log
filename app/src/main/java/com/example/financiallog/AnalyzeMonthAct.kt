package com.example.financiallog

import android.annotation.SuppressLint
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

        // 날짜 표시
        mFormat = SimpleDateFormat("yyyy년 MM월", Locale.KOREAN)
        currentDate = Date()
        monthText.text = mFormat.format(currentDate)

        month_btn.setOnClickListener { showMonthPickerDialog() } // 다른 달 선택 다이얼로그 표시

        //월 수입 표시



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
                //fetchMonthlyData(currentYear)
            }
            .show()
    }

    /*private fun fetchMonthlyData(year: Int) {
        val dateString = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date)

        apiobject.api.getStatisticsMonthly(userId = 6, date = dateString).enqueue(object : Callback<List<ResponseStatMonth>>{
            override fun onResponse(call: Call<ResponseStatMonth>, response: Response<ResponseStatMonth>) {
                if (response.isSuccessful) {
                    response.body()?.let { responseData ->
                        updateIncomeAndExpense(responseData)
                    }
                } else {
                    // 응답 실패 처리
                    Toast.makeText(this@AnalyzeMonthAct, "데이터를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show()
                    updateIncomeAndExpense(null)
                }
            }

            override fun onFailure(call: Call<ResponseStatMonth>, t: Throwable) {
                // 네트워크 오류 처리
                Toast.makeText(this@AnalyzeMonthAct, "네트워크 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
                updateIncomeAndExpense(null)
            }
        })
    }

    private fun updateIncomeAndExpense(data: ResponseStatMonth?) {
        val monthStatistics = getMonthStatistics(data)
        income_text.text = monthStatistics.first.toString()
        expend_text.text = monthStatistics.second.toString()
    }

    private fun getMonthStatistics(data: ResponseStatMonth?): Pair<Int, Int> {
        val currentMonth = Calendar.getInstance().get(Calendar.MONTH)
        val monthStatistics = when (currentMonth) {
            0 -> data?.jan
            1 -> data?.feb
            2 -> data?.mar
            3 -> data?.apr
            4 -> data?.may
            5 -> data?.jun
            6 -> data?.jul
            7 -> data?.aug
            8 -> data?.sep
            9 -> data?.oct
            10 -> data?.nov
            11 -> data?.dec
            else -> null
        }
        val totalIncome = monthStatistics?.totalIncome ?: 0
        val totalExpense = monthStatistics?.totalExpense ?: 0
        return Pair(totalIncome, totalExpense)
    }*/


}
