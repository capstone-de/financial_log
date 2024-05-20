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
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AnalyzeDayAct: AppCompatActivity() {

    lateinit var date_tv : TextView;
    lateinit var income_tv : TextView;lateinit var expend_tv : TextView;
    lateinit var slash_tv : TextView;
    lateinit var dayexp_list : RecyclerView;
    lateinit var DFormat :SimpleDateFormat;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.analyze_day)

        val analyze_btn = findViewById<Button>(R.id.btn_analyze)
        val analyzedi_btn = findViewById<Button>(R.id.btn_diary)
        val tab_analyze =findViewById<TabLayout>(R.id.tabLayout)

        val tab_day = findViewById<TabLayout>(R.id.day)
        val tab_week =findViewById<TabLayout>(R.id.week)
        val tab_month = findViewById<TabLayout>(R.id.month)
        val tab_yearly = findViewById<TabLayout>(R.id.yearly)

        val left_btn = findViewById<ImageButton>(R.id.leftButton)
        val right_btn =findViewById<ImageButton>(R.id.rightButton)

        date_tv = findViewById<TextView>(R.id.date_text)
        income_tv =findViewById<TextView>(R.id.day_income_text)
        expend_tv = findViewById<TextView>(R.id.day_expend_text)
        slash_tv = findViewById<TextView>(R.id.slash)

        dayexp_list = findViewById<RecyclerView>(R.id.analyday_re)

        //날짜 표시
        DFormat = SimpleDateFormat("MM월 dd일 ", Locale.KOREAN)
        //date_tv.setText(getTime())

        //mFormat = SimpleDateFormat("yyyy.MM.dd")
        //date_tv.setText(getTime())
        //val dateFormat = SimpleDateFormat("MM-dd-EE")

        //총 수입
        val tIncome_tv = findViewById<TextView>(R.id.day_income_text)

        // Retrofit 인스턴스 생성
        val retrofit = Retrofit.Builder()
            .baseUrl("/statistics/daily/{user?date}") // 실제 API URL로 변경
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)

        // API 호출
        apiService.getStatDay().enqueue(object : Callback<ResponseStatDay> {
            override fun onResponse(call: Call<ResponseStatDay>, response: Response<ResponseStatDay>) {
                if (response.isSuccessful) {
                    val responseStatDay = response.body()
                    if (responseStatDay != null) {
                        // totalIncome 값을 설정합니다.
                        tIncome_tv.text = "${responseStatDay.totalIncome} 원"
                    }
                }
            }

            override fun onFailure(call: Call<ResponseStatDay>, t: Throwable) {
                // 에러 처리
            }
        })



        //총 지출

        //


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
                    tab_analyze.getTabAt(it.position)?.select() //선택된 탭으로 설정
                    // 선택된 탭에 대한 표시 추가
                    it.view.isSelected = true
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
}