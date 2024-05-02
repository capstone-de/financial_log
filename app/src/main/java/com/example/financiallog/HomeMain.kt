package com.example.financiallog

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.CalendarView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class HomeMain: AppCompatActivity() {

    lateinit var cal : CalendarView; lateinit var datetext : TextView; lateinit var today :Date;
    lateinit var expendtext : TextView; lateinit var incometext: TextView; lateinit var mFormat :SimpleDateFormat
    lateinit var re_expend: RecyclerView; lateinit var re_income : RecyclerView;
    lateinit var data_ex : ApiObject; lateinit var data_in : ApiObject;
    lateinit var list_ex : List<ExpendAdapter.Exlist>; lateinit var list_in : List<IncomeAdapter.IncomeList>

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        cal = findViewById<CalendarView>(R.id.calView)
        datetext = findViewById<TextView>(R.id.date_text)
        expendtext = findViewById<TextView>(R.id.expend_tt)
        incometext = findViewById<TextView>(R.id.income_text)

        //날짜표시
        mFormat = SimpleDateFormat("MM월 dd일 ", Locale.KOREAN)
        datetext.setText(getTime())
        cal.setOnDateChangeListener { calendarView, year, month, day ->
            val dateText = String.format("%02d월 %02d일", month + 1, day)
            datetext.setText(dateText)
        }


        // 지출 내역 화면에 보여주기
        re_expend = findViewById<RecyclerView>(R.id.expend_re)
        re_expend.layoutManager = LinearLayoutManager(this)
        //val adapter_1 = ExpendAdapter()
        //re_expend.adapter = adapter_1
       /* data_ex.getExpend.enqueue(object : Callback<ExpendAdapter.Exlist?>() {
            fun getExpendAll(call: Call<ExpendAdapter.Exlist?>, response: Response<ExpendAdapter.Exlist?>) {
                if (response.isSuccessful()) {
                    val body: ExpendAdapter.Exlist = response.body()
                    if (body != null) {
                        Log.d("data.getUserId()", body.getUserId() + "")
                        Log.d("data.getId()", body.getId() + "")
                        Log.d("data.getTitle()", body.getTitle())
                        Log.d("data.getBody()", body.getBody())
                        Log.e("getData end", "======================================")
                    }
                }
            }

            fun onFailure(call: Call<ExpendAdapter.Exlist?>, t: Throwable) {}*/


        // 수입 내역 화면에 보여주기
        re_income = findViewById<RecyclerView>(R.id.income_re)
        re_income.layoutManager = LinearLayoutManager(this)
        //val adapter_2 = IncomeAdapter()
        //re_expend.adapter = adapter_2


        // 하단바 버튼
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_view)
        bottomNavigation.setOnItemSelectedListener { item ->
            when(item.itemId){
                R.id.home -> {
                    val intent = Intent(this, HomeMain::class.java)
                    startActivity(intent)
                    Toast.makeText(applicationContext, "home", Toast.LENGTH_SHORT).show()
                }
                R.id.financial -> {
                    val intent = Intent(this, DiaryWriteAct::class.java)
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

    private fun getTime(): String? {
        var mNow = System.currentTimeMillis()
        today = Date(mNow)
        return mFormat.format(today)
    }


}

