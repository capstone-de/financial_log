package com.example.financiallog

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class AnalyzeDayAct : AppCompatActivity() {

    lateinit var dateText: TextView
    lateinit var expendTextView: TextView
    lateinit var incomeTextView: TextView
    lateinit var day_re: RecyclerView
    lateinit var currentDate: Date
    lateinit var mFormat: SimpleDateFormat

    val apiobject: ApiObject by lazy { ApiObject() };


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.analyze_day)

        val analyzeBtn = findViewById<Button>(R.id.btn_analyze)
        val analyzeDiaryBtn = findViewById<Button>(R.id.btn_diary)
        val tabAnalyze = findViewById<TabLayout>(R.id.tabLayout)
        val leftBtn = findViewById<ImageButton>(R.id.leftButton)
        val rightBtn = findViewById<ImageButton>(R.id.rightButton)

        dateText = findViewById(R.id.date_text)
        incomeTextView = findViewById(R.id.day_income_text)
        expendTextView = findViewById(R.id.day_expend_text)
        day_re = findViewById(R.id.analyday_re)

        // RecyclerView 초기화
        day_re.layoutManager = LinearLayoutManager(this)
        day_re.adapter = DayExpenseAdapter(emptyList())

        //날짜 표시
        mFormat = SimpleDateFormat("MM월 dd일", Locale.KOREAN)
        currentDate = Date()
        dateText.text = mFormat.format(currentDate)

        leftBtn.setOnClickListener {
            val calendar = Calendar.getInstance()
            calendar.time = currentDate
            calendar.add(Calendar.DAY_OF_YEAR, -1)
            currentDate = calendar.time
            dateText.text = mFormat.format(currentDate)
            fetchAllDataForDate(currentDate)
        }

        rightBtn.setOnClickListener {
            val calendar = Calendar.getInstance()
            calendar.time = currentDate
            calendar.add(Calendar.DAY_OF_YEAR, 1)
            currentDate = calendar.time
            dateText.text = mFormat.format(currentDate)
            fetchAllDataForDate(currentDate)
        }


        // 현재 날짜에 대한 데이터 가져오기
        fetchAllDataForDate(currentDate)


        // 가계부 버튼 클릭 시
        analyzeBtn.setOnClickListener {
            val intent = Intent(this, AnalyzeDayAct::class.java)
            startActivity(intent)
        }

        // 일기 버튼 클릭 시
        analyzeDiaryBtn.setOnClickListener {
            val intent = Intent(this, AnalyzeDiaryAct::class.java)
            startActivity(intent)
        }

        // 탭 전환 시
        tabAnalyze.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    when (it.position) {
                        0 -> {
                            val intent = Intent(this@AnalyzeDayAct, AnalyzeDayAct::class.java)
                            startActivity(intent)
                        }

                        1 -> {
                            val intent = Intent(this@AnalyzeDayAct, AnalyzeWeekAct::class.java)
                            startActivity(intent)
                        }

                        2 -> {
                            val intent = Intent(this@AnalyzeDayAct, AnalyzeMonthAct::class.java)
                            startActivity(intent)
                        }

                        3 -> {
                            val intent = Intent(this@AnalyzeDayAct, AnalyzeyearlyAct::class.java)
                            startActivity(intent)
                        }
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {
                tab?.let {
                    when (it.position) {
                        0 -> {
                            val intent = Intent(this@AnalyzeDayAct, AnalyzeDayAct::class.java)
                            startActivity(intent)
                        }

                        1 -> {
                            val intent = Intent(this@AnalyzeDayAct, AnalyzeWeekAct::class.java)
                            startActivity(intent)
                        }

                        2 -> {
                            val intent = Intent(this@AnalyzeDayAct, AnalyzeMonthAct::class.java)
                            startActivity(intent)
                        }

                        3 -> {
                            val intent = Intent(this@AnalyzeDayAct, AnalyzeyearlyAct::class.java)
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

        moreBottomView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.add_income -> {
                    val intent = Intent(this, IncomeAct::class.java)
                    startActivity(intent)
                    true
                }

                R.id.add_expend -> {
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
                                val intent = Intent(this@AnalyzeDayAct, DiaryWriteAct::class.java)
                                startActivity(intent)
                            } else {
                                // 네트워크 응답이 실패했거나 데이터가 없는 경우
                                Toast.makeText(this@AnalyzeDayAct, "이미 저장된 일기가 있습니다.", Toast.LENGTH_SHORT).show()
                                dialog.dismiss() // 다이얼로그 닫기
                            }
                        }

                        override fun onFailure(call: Call<List<DataEx>>, t: Throwable) {
                            // 네트워크 요청 실패 시
                            Toast.makeText(this@AnalyzeDayAct, "네트워크 요청에 실패했습니다.", Toast.LENGTH_SHORT).show()
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
        val currentDate = org.threeten.bp.LocalDate.now()
        val formatter = org.threeten.bp.format.DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return currentDate.format(formatter)
    }


    private fun fetchAllDataForDate(date: Date) {
        IncomeForDate(date)
        ExpensesForDate(date)
        ExpenseListForDate(date)
    }

    //수입 표시
    private fun IncomeForDate(date: Date) {
        val formattedDate = SimpleDateFormat("yyyy-MM-dd", Locale.KOREAN).format(date)

        apiobject.api.getStatisticsDaily(6, formattedDate).enqueue(object : Callback<ResponseStatDay> {
            override fun onResponse(call: Call<ResponseStatDay>, response: Response<ResponseStatDay>) {
                if (response.isSuccessful) {
                    val incometv = response.body()!!.totalIncome
                    incomeTextView.text = incometv.toString()
//                    response.body()?.let { data ->
//                        if (data.isNotEmpty()) {
//                            incomeTextView.text = data[0].totalIncome.toString()
//                        } else {
//                            incomeTextView.text = "0"
//                        }
//                    }
                }
            }

            override fun onFailure(call: Call<ResponseStatDay>, t: Throwable) {
                // API 호출 실패 처리
                Log.d("response", "실패$t")
                Toast.makeText(applicationContext, "정보를 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    //지출 표시
    private fun ExpensesForDate(date: Date) {
        val formattedDate = SimpleDateFormat("yyyy-MM-dd", Locale.KOREAN).format(date)

        apiobject.api.getStatisticsDaily(6, formattedDate).enqueue(object : Callback<ResponseStatDay> {
            override fun onResponse(call: Call<ResponseStatDay>, response: Response<ResponseStatDay>) {
                if (response.isSuccessful) {
                    val expendsetv = response.body()!!.totalExpenses
                    expendTextView.text = expendsetv.toString()
//                    response.body()?.let { data ->
//                        if (data.isNotEmpty()) {
//                            expendTextView.text = data[0].totalExpenses.toString()
//                        } else {
//                            expendTextView.text = "0"
//                        }
//                    }
                }
            }

            override fun onFailure(call: Call<ResponseStatDay>, t: Throwable) {
                // API 호출 실패 처리
                Log.d("response", "실패$t")
                Toast.makeText(applicationContext, "정보를 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    //지출 리스트
    private fun ExpenseListForDate(date: Date) {
        val formattedDate = SimpleDateFormat("yyyy-MM-dd", Locale.KOREAN).format(date)

        apiobject.api.getStatisticsDaily(6, formattedDate).enqueue(object : Callback<ResponseStatDay> {
            override fun onResponse(call: Call<ResponseStatDay>, response: Response<ResponseStatDay>) {
                if (response.isSuccessful) {
                    val expenselistdata = response.body()!!.expenses
                    val expenseadapter = DayExpenseAdapter(expenselistdata)
                    day_re.adapter = expenseadapter
                    Toast.makeText(applicationContext, "지출 내역을 가져옴", Toast.LENGTH_SHORT).show()
//                    response.body()?.let { data ->
//                        if (data.isNotEmpty()) {
//                            (day_re.adapter as DayExpenseAdapter).updateData(data[0].expenses)
//                            Toast.makeText(applicationContext, "지출 내역을 가져옴", Toast.LENGTH_SHORT).show()
//                        } else {
//                            (day_re.adapter as DayExpenseAdapter).updateData(emptyList())
//                            Toast.makeText(applicationContext, "지출 내역이 없습니다.", Toast.LENGTH_SHORT).show()
//                        }
//                    }
                }
            }

            override fun onFailure(call: Call<ResponseStatDay>, t: Throwable) {
                // API 호출 실패 처리
                Log.d("response", "실패$t")
                Toast.makeText(applicationContext, "정보를 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
            }
        })
    }
}





