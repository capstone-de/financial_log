package com.example.financiallog

<<<<<<< HEAD
=======
import android.annotation.SuppressLint
>>>>>>> f4e1deaa07d48dd614c2f63056791c8a9f026487
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
<<<<<<< HEAD
import android.widget.ImageButton
=======
>>>>>>> f4e1deaa07d48dd614c2f63056791c8a9f026487
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout

class AnalyzeMonthAct: AppCompatActivity() {
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

        val date_text = findViewById<TextView>(R.id.monthly_tv)

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
<<<<<<< HEAD
                            //현재 액티비티 이동은 생략
=======
                            val intent = Intent(this@AnalyzeMonthAct, AnalyzeMonthAct::class.java)
                            startActivity(intent)
>>>>>>> f4e1deaa07d48dd614c2f63056791c8a9f026487
                        }
                        3 -> {
                            val intent = Intent(this@AnalyzeMonthAct, AnalyzeyearlyAct::class.java)
                            startActivity(intent)
                        }
                    }
<<<<<<< HEAD
                    tab_analyze.getTabAt(it.position)?.select() //선택된 탭으로 설정
                    // 선택된 탭에 대한 표시 추가
                    it.view.isSelected = true
=======
>>>>>>> f4e1deaa07d48dd614c2f63056791c8a9f026487
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // 탭이 선택 해제될 때 필요한 처리
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                // 탭이 다시 선택될 때 필요한 처리
            }
<<<<<<< HEAD

        })

=======
        })


>>>>>>> f4e1deaa07d48dd614c2f63056791c8a9f026487
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
<<<<<<< HEAD
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
=======

>>>>>>> f4e1deaa07d48dd614c2f63056791c8a9f026487
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
