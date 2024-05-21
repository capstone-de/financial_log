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
import java.util.*

class AnalyzeDayAct : AppCompatActivity() {

    lateinit var dateText: TextView
    lateinit var expendTextView: TextView
    lateinit var incomeTextView: TextView
    lateinit var day_re: RecyclerView
    lateinit var currentDate: Date
    lateinit var mFormat: SimpleDateFormat

    val apiobject : ApiObject by lazy { ApiObject() }; val PICK_IMAGE_REQUEST = 1
    val list_cate : ApiObject by lazy { ApiObject() };

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


        //날짜 표시
        mFormat = SimpleDateFormat("MM월 dd일", Locale.KOREAN)
        currentDate = Date()
        dateText.text = mFormat.format(currentDate)

        leftBtn.setOnClickListener {changeDate(-1)}
        rightBtn.setOnClickListener {changeDate(1)}



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
                    val intent = Intent(this, DiaryWriteAct::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }

        val dialog = AlertDialog.Builder(this)
            .setView(moreBottomView)
            .create()
        dialog.show()
    }

    private fun changeDate(days: Int) {
        val calendar = Calendar.getInstance()
        calendar.time = currentDate
        calendar.add(Calendar.DATE, days)
        currentDate = calendar.time
        dateText.text = mFormat.format(currentDate)
        getDateData(currentDate)
    }

    private fun getDateData(date: Date) {
        val dateString = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date)

        // API 호출하여 데이터 가져오기
        apiobject.api.getStatisticsDaily(userId = 6, date = dateString).enqueue(object : Callback<List<ResponseStatDay>> {
            override fun onResponse(call: Call<List<ResponseStatDay>>, response: Response<List<ResponseStatDay>>) {
                if (response.isSuccessful) {
                    val data = response.body()?.firstOrNull()
                    data?.let { updateUI(it) }
                } else {
                    // API 호출에 실패한 경우에 대한 처리
                }
            }

            override fun onFailure(call: Call<List<ResponseStatDay>>, t: Throwable) {
                // API 호출 실패에 대한 처리
            }
        })
    }


    private fun updateUI(data: ResponseStatDay) {
        incomeTextView.text = data.totalIncome.toString()
        expendTextView.text = data.totalExpenses.toString()

        val adapter = DayExpenseAdapter(data.expenses)
        day_re.adapter = adapter
    }

}

