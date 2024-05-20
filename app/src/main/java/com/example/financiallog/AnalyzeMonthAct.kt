package com.example.financiallog

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout

class AnalyzeMonthAct: AppCompatActivity() {
    lateinit var month_tv : TextView; lateinit var month_btn : ImageButton;
    lateinit var income_tv : TextView;lateinit var expend_tv : TextView;
    lateinit var income_im : ImageView;lateinit var expend_im : ImageView;

    lateinit var income_price : TextView;lateinit var expend_price : TextView;
    //lateinit var month_chart : TextView; lateinit var cate_chart : TextView;
    lateinit var expendran_tv : TextView; lateinit var ranking1_ntv : TextView;
    lateinit var ranking1_ttv : TextView;lateinit var ranking1_ptv : TextView;
    lateinit var ranking2_ntv : TextView;lateinit var ranking2_ttv : TextView;
    lateinit var ranking2_ptv : TextView;lateinit var ranking3_ntv : TextView;
    lateinit var ranking3_ctv : TextView;lateinit var ranking3_ptv : TextView;
    //5위까지
    lateinit var catebest_tv : TextView;
    lateinit var satisbest_tv : TextView; lateinit var satisbest_cate : TextView;
    lateinit var satisbest_expen : TextView;lateinit var satisbest_ptv : TextView;

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

        month_tv = findViewById<TextView>(R.id.monthly_tv)
        month_btn = findViewById<ImageButton>(R.id.m_underButton)

        //현재 월

        //월 변경
        month_btn.setOnClickListener(View.OnClickListener{

        })

        //이번달 총수입

        //이번달 총지출

        //월 차트

        //월 소비 순위

        //월 베스트소비 카테고리

        //가장 만족한 월 소비


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

}