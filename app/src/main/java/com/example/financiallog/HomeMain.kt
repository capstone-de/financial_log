package com.example.financiallog

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
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
    val data_calender :ApiObject by lazy { ApiObject() }
    val data_list:ApiObject by lazy { ApiObject() };


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val calendarView = findViewById<MaterialCalendarView>(R.id.calendar_View)
        // cal = findViewById<CalendarView>(R.id.calView)
        datetext = findViewById<TextView>(R.id.date_text)
        expendtext = findViewById<TextView>(R.id.expend_tt)
        incometext = findViewById<TextView>(R.id.income_text)

        //날짜표시
        mFormat = SimpleDateFormat("MM월 dd일 ", Locale.KOREAN)
        datetext.setText(getTime())
        calendarView.setOnDateChangedListener { widget, date, selected ->
            if (selected) {
                // 선택된 날짜 텍스트 뷰에 표시
                datetext.setText(formatDate(date.date))
                // 선택 시 내역 가져오기
                data_ex.api.getExpendAll(3,getFormattedDate(date.date)).enqueue(object : Callback<ResponseExpend> {
                    override fun onResponse(
                        call: Call<ResponseExpend>,
                        response: Response<ResponseExpend>
                    ) {
                        if(response.isSuccessful){
                            val data = response.body()!!.expense
                            val expendadapter = ExpendAdapter(data)
                            re_expend.adapter = expendadapter
                            //Toast.makeText(applicationContext, "성공", Toast.LENGTH_SHORT).show()

                        }
                    }
                    override fun onFailure(call: Call<ResponseExpend>, t: Throwable) {
                        Log.d("response", "실패$t")
                        Toast.makeText(applicationContext, "선택된 지출 정보를 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
                    }

                })
                data_in.api.getIncomeAll(3,getFormattedDate(date.date)).enqueue(object : Callback<ResponseIncome> {
                    override fun onResponse(
                        call: Call<ResponseIncome>,
                        response: Response<ResponseIncome>
                    ) {
                        if(response.isSuccessful){
                            val data = response.body()!!.income
                            val incomeadapter = IncomeAdapter(data)
                            re_income.adapter = incomeadapter
                            //Toast.makeText(applicationContext, "성공", Toast.LENGTH_SHORT).show()

                        }
                    }
                    override fun onFailure(call: Call<ResponseIncome>, t: Throwable) {
                        Log.d("response", "실패$t")
                        Toast.makeText(applicationContext, "선택된 수입 정보를 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
                    }

                })
            }
        }
        /*calender.setOnDateChangeListener { calendarView, year, month, day ->
            val dateText = String.format("%02d월 %02d일", month + 1, day)
            datetext.setText(dateText)
        }*/

        // 점 표시
        data_calender.api.getcalender(3,FormattedYear(),FormattedMonth()).enqueue(object : Callback<ResponseCalendar>{
            @SuppressLint("SuspiciousIndentation")
            override fun onResponse(
                call: Call<ResponseCalendar>,
                response: Response<ResponseCalendar>
            ) {
                if(response.isSuccessful){
                    val responseCalendar = response.body()
                    responseCalendar?.let { calendar ->
                        // 노란색 점 표시
                        calendar.diary.forEach { diary ->
                            calendarView.addDecorator(RedPointDecorator(diary))
                        }

                        // 파란색 점 표시
                        calendar.income.forEach { income ->
                            calendarView.addDecorator(BluePointDecorator(income))
                        }

                        // 빨간색 점 표시
                        calendar.expense.forEach { expense ->
                            calendarView.addDecorator(YellowPointDecorator(expense))
                        }
                    }
                    calendarView.invalidateDecorators() // 모든 Decorator 추가 후 캘린더 뷰 갱신

                }
            }
            override fun onFailure(call: Call<ResponseCalendar>, t: Throwable) {
                Log.d("get main calender", "실패$t")
                Toast.makeText(applicationContext, "달력의 정보를 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
            }

        })

        // 달 변경 리스너 설정
        calendarView.setOnMonthChangedListener { widget, date ->
            val year = date.year.toString()
            val month = date.month.toString()// `MaterialCalendarView`의 month는 0부터 시작하므로 +1 해줍니다.

            data_calender.api.getcalender(3,year, month).enqueue(object : Callback<ResponseCalendar>{
                @SuppressLint("SuspiciousIndentation")
                override fun onResponse(
                    call: Call<ResponseCalendar>,
                    response: Response<ResponseCalendar>
                ) {
                    if(response.isSuccessful){
                        val responseCalendar = response.body()
                        responseCalendar?.let { calendar ->
                            // 노란색 점 표시
                            calendar.diary.forEach { diary ->
                                calendarView.addDecorator(RedPointDecorator(diary))
                            }

                            // 파란색 점 표시
                            calendar.income.forEach { income ->
                                calendarView.addDecorator(BluePointDecorator(income))
                            }

                            // 빨간색 점 표시
                            calendar.expense.forEach { expense ->
                                calendarView.addDecorator(YellowPointDecorator(expense))
                            }
                        }
                        calendarView.invalidateDecorators() // 모든 Decorator 추가 후 캘린더 뷰 갱신

                    }
                }
                override fun onFailure(call: Call<ResponseCalendar>, t: Throwable) {
                    Log.d("get main calender", "실패$t")
                    Toast.makeText(applicationContext, "정보를 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
                }

            })

        }

        // 지출 내역 화면에 보여주기
        re_expend = findViewById<RecyclerView>(R.id.expend_re)
        re_expend.layoutManager = LinearLayoutManager(this)
        data_ex.api.getExpendAll(3, getCurrentFormattedDate()).enqueue(object : Callback<ResponseExpend> {
            override fun onResponse(
                call: Call<ResponseExpend>,
                response: Response<ResponseExpend>
            ) {
                if(response.isSuccessful){
                    val data = response.body()!!.expense
                    val expendadapter = ExpendAdapter(data)
                    re_expend.adapter = expendadapter
                    Toast.makeText(applicationContext, "지출 내역을 가져옴", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<ResponseExpend>, t: Throwable) {
                Log.d("response", "실패$t")
                Toast.makeText(applicationContext, "지출 정보를 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
            }

        })

        // 수입 내역 화면에 보여주기
        re_income = findViewById<RecyclerView>(R.id.income_re)
        re_income.layoutManager = LinearLayoutManager(this)
        data_in.api.getIncomeAll(3,getCurrentFormattedDate()).enqueue(object : Callback<ResponseIncome> {
            override fun onResponse(
                call: Call<ResponseIncome>,
                response: Response<ResponseIncome>
            ) {
                if(response.isSuccessful){
                    val data = response.body()!!.income
                    val incomeadapter = IncomeAdapter(data)
                    re_income.adapter = incomeadapter
                    Toast.makeText(applicationContext, "수입 내역 가져옴", Toast.LENGTH_SHORT).show()

                }
            }
            override fun onFailure(call: Call<ResponseIncome>, t: Throwable) {
                Log.d("response", "실패$t")
                Toast.makeText(applicationContext, "수입 정보를 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
            }

        })

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
                    data_list.api.diarywriteEx(3,getCurrentFormattedDate()).enqueue(object : Callback<List<DataEx>> {
                        override fun onResponse(call: Call<List<DataEx>>, response: Response<List<DataEx>>) {
                            if (response.isSuccessful && response.body() != null) {
                                // 네트워크 응답이 성공적이고 데이터가 있는 경우
                                val intent = Intent(this@HomeMain, DiaryWriteAct::class.java)
                                startActivity(intent)
                            } else {
                                // 네트워크 응답이 실패했거나 데이터가 없는 경우
                                Toast.makeText(this@HomeMain, "이미 저장된 일기가 있습니다.", Toast.LENGTH_SHORT).show()
                                dialog.dismiss() // 다이얼로그 닫기
                            }
                        }

                        override fun onFailure(call: Call<List<DataEx>>, t: Throwable) {
                            // 네트워크 요청 실패 시
                            Toast.makeText(this@HomeMain, "네트워크 요청에 실패했습니다.", Toast.LENGTH_SHORT).show()
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

    private fun getTime(): String? {
        var mNow = System.currentTimeMillis()
        today = Date(mNow)
        return mFormat.format(today)
    }

    private fun formatDate(date: LocalDate): String {
        val dateFormat = DateTimeFormatter.ofPattern("MM월 dd일", Locale.getDefault())
        return date.format(dateFormat)
    }
    fun getCurrentFormattedDate(): String {
        val currentDate = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return currentDate.format(formatter)
    }
    fun FormattedYear(): String {
        val currentDate =  LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy")
        return currentDate.format(formatter)
    }
    fun FormattedMonth(): String {
        val currentDate =  LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("M")
        return currentDate.format(formatter)
    }

    fun getFormattedDate(date: LocalDate): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.getDefault())
        return date.format(formatter)
    }
    fun LocalDate.toCalendarDay(): CalendarDay = CalendarDay.from(year, monthValue - 1, dayOfMonth)


}
