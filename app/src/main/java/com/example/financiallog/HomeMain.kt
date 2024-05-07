package com.example.financiallog

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import retrofit2.Call as Call



class HomeMain: AppCompatActivity() {

    //lateinit var cal : CalendarView;
    lateinit var datetext : TextView; lateinit var today :Date;
    lateinit var expendtext : TextView; lateinit var incometext: TextView; lateinit var mFormat :SimpleDateFormat
    lateinit var re_expend: RecyclerView; lateinit var re_income : RecyclerView;
    val data_ex : ApiObject by lazy { ApiObject() }; val data_in : ApiObject by lazy { ApiObject() };
    lateinit var list_ex : List<ExpendAdapter.Exlist>; val Diarybtn : DiaryWriteAct by lazy { DiaryWriteAct() }
    val Incombtn :IncomeAct by lazy { IncomeAct() }
    private val datesWithEvent = mutableSetOf<CalendarDay>(); private val DiaryEvent = mutableSetOf<CalendarDay>()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val calender = findViewById<MaterialCalendarView>(R.id.calendar_View)
       // cal = findViewById<CalendarView>(R.id.calView)
        datetext = findViewById<TextView>(R.id.date_text)
        expendtext = findViewById<TextView>(R.id.expend_tt)
        incometext = findViewById<TextView>(R.id.income_text)

        //날짜표시
        mFormat = SimpleDateFormat("MM월 dd일 ", Locale.KOREAN)
        datetext.setText(getTime())
        calender.setOnDateChangedListener { widget, date, selected ->
            if (selected) {
                // 선택된 날짜 텍스트 뷰에 표시
                datetext.setText(formatDate(date.date))
            }
        }
        /*calender.setOnDateChangeListener { calendarView, year, month, day ->
            val dateText = String.format("%02d월 %02d일", month + 1, day)
            datetext.setText(dateText)
        }*/

        // 점 표시
        // 지출 내역에 해당하는 날짜를 저장한 리스트
       // val expenseDates = data_ex
       // val expenseDecorator = CustomDayViewDecorator(Color.RED, expenseDates.toHashSet())
       // calender.addDecorator(expenseDecorator)

        // 수입 내역에 해당하는 날짜를 저장한 리스트
        /*Incombtn.btn_save.setOnClickListener(View.OnClickListener{
            val today = CalendarDay.today()
            datesWithEvent.add(today)
            calender.addDecorator(CustomDayViewDecorator(Color.BLUE, datesWithEvent))
        })*/

       // val incomeDates =
       // val incomeDecorator = CustomDayViewDecorator(Color.BLUE, incomeDates.toHashSet())
      //  calender.addDecorator(incomeDecorator)

        // 일기에 해당하는 날짜를 저장한 리스트
       // val diaryDates =
       // val diaryDecorator = CustomDayViewDecorator(Color.YELLOW, diaryDates.toHashSet())
      //  calender.addDecorator(diaryDecorator)
        /*Diarybtn.diary_save.setOnClickListener {
            // 오늘 날짜를 datesWithEvent 집합에 추가
            val today = CalendarDay.today()
            DiaryEvent.add(today)

            // 노란색 점 표시
            calender.addDecorator(CustomDayViewDecorator(Color.YELLOW, DiaryEvent))
        }*/




        // 지출 내역 화면에 보여주기
        re_expend = findViewById<RecyclerView>(R.id.expend_re)
        re_expend.layoutManager = LinearLayoutManager(this)

       // val adapter_1 = ExpendAdapter()
      //  re_expend.adapter = adapter_1
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
        data_in.api.getIncomeAll().enqueue(object : Callback<ResponseIncome> {
            override fun onResponse(
                call: Call<ResponseIncome>,
                response: Response<ResponseIncome>
            ) {
                if(response.isSuccessful){
                    val data = response.body()?.ReIncome?: emptyList()
                    val incomeadapter = IncomeAdapter(data)
                    re_income.adapter = incomeadapter
                    Toast.makeText(applicationContext, "성공", Toast.LENGTH_SHORT).show()

                }
            }

            override fun onFailure(call: Call<ResponseIncome>, t: Throwable) {
                Log.d("response", "실패$t")
                Toast.makeText(applicationContext, "정보를 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
            }

        })

        /*val incomeAdapter = IncomeAdapter(emptyList())
        re_income.adapter = incomeAdapter

        GlobalScope.launch(Dispatchers.Main) {
            val response = data_in.api.getIncomeAll().execute()
            if (response.isSuccessful) {
                val incomeData = response.body()?.ReIncome
                incomeAdapter.setData(incomeData ?: emptyList())
                //setupRecyclerView(incomeData)
            }
        }*/
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
   /* private fun setupRecyclerView(data: List<ResponseIncome.DataIn>) {
        val recyclerView = findViewById<RecyclerView>(R.id.income_re)
        var incomeAdapter = IncomeAdapter(data)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@HomeMain)
            adapter = incomeAdapter
        }
    }*/

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

    private fun formatDate(date: LocalDate): String {
        val dateFormat = DateTimeFormatter.ofPattern("MM월 dd일", Locale.getDefault())
        return date.format(dateFormat)
    }


}

