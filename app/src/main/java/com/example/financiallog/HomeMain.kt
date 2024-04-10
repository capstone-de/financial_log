package com.example.financiallog

import android.content.Intent
import android.os.Bundle
import android.widget.CalendarView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeMain: AppCompatActivity() {

    lateinit var cal : CalendarView;
    lateinit var datetext : TextView;
    lateinit var expendtext : TextView;
    lateinit var incometext: TextView;
    lateinit var re_expend: RecyclerView;
    lateinit var re_income : RecyclerView;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        cal = findViewById<CalendarView>(R.id.calView)
        datetext = findViewById<TextView>(R.id.date_text)
        expendtext = findViewById<TextView>(R.id.expend_tt)
        incometext = findViewById<TextView>(R.id.income_text)

        //날짜표시
        cal.setOnDateChangeListener { calendarView, year, month, day ->
            datetext.setText(
                month.toString (month + 1) + "월" + day  + "일"
            )
        }

        // 지출 내역 화면에 보여주기
        re_expend = findViewById<RecyclerView>(R.id.expend_re)
        re_expend.layoutManager = LinearLayoutManager(this)

        // 수입 내역 화면에 보여주기
        re_income = findViewById<RecyclerView>(R.id.income_re)
        re_income.layoutManager = LinearLayoutManager(this)


        // 하단바 버튼
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
        bottomNavigation.setOnItemSelectedListener { item ->
            when(item.itemId){
                R.id.home -> {
                    val intent = Intent(this, HomeMain::class.java)
                    startActivity(intent)
                    Toast.makeText(applicationContext, "home", Toast.LENGTH_SHORT).show()
                }
                R.id.financial -> {
                    Toast.makeText(applicationContext, "financial", Toast.LENGTH_SHORT).show()
                }
                R.id.add -> {
                    when(item.itemId){
                        R.id.add_income -> {
                            val intent = Intent(this, IncomeAct::class.java)
                            startActivity(intent)
                        }
                        R.id.add_expend -> {
                            val intent = Intent(this, ExpendAct::class.java)
                            startActivity(intent)
                        }
                        R.id.add_diary -> {
                            val intent = Intent(this, DiaryWriteAct::class.java)
                            startActivity(intent)
                        }
                    }
                }
                R.id.diary -> {
                    val intent = Intent(this, DiarySns::class.java)
                    startActivity(intent)
                    Toast.makeText(applicationContext, "sns_feed", Toast.LENGTH_SHORT).show()

                }
                R.id.settings -> {
                    Toast.makeText(applicationContext, "mypage", Toast.LENGTH_SHORT).show()

                }
                else -> {
                    Toast.makeText(applicationContext, "else", Toast.LENGTH_SHORT).show()

                }
            }; true
        }




    }
}
