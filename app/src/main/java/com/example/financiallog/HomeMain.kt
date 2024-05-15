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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
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
    val data_calender :ApiObject by lazy { ApiObject() }
    val Diarybtn : DiaryWriteAct by lazy { DiaryWriteAct() }
    val Incombtn :IncomeAct by lazy { IncomeAct() }
    private val datesWithEvent = mutableSetOf<CalendarDay>(); private val DiaryEvent = mutableSetOf<CalendarDay>()

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
                data_ex.api.getExpendAll().enqueue(object : Callback<ResponseExpend> {
                    override fun onResponse(
                        call: Call<ResponseExpend>,
                        response: Response<ResponseExpend>
                    ) {
                        if(response.isSuccessful){
                            val data = response.body()!!.expense
                            val expendadapter = ExpendAdapter(data)
                            re_expend.adapter = expendadapter
                            Toast.makeText(applicationContext, "성공", Toast.LENGTH_SHORT).show()

                        }
                    }
                    override fun onFailure(call: Call<ResponseExpend>, t: Throwable) {
                        Log.d("response", "실패$t")
                        Toast.makeText(applicationContext, "정보를 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
                    }

                })
                data_in.api.getIncomeAll().enqueue(object : Callback<ResponseIncome> {
                    override fun onResponse(
                        call: Call<ResponseIncome>,
                        response: Response<ResponseIncome>
                    ) {
                        if(response.isSuccessful){
                            val data = response.body()!!.income
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
            }
        }
        /*calender.setOnDateChangeListener { calendarView, year, month, day ->
            val dateText = String.format("%02d월 %02d일", month + 1, day)
            datetext.setText(dateText)
        }*/

        // 점 표시
        data_calender.api.getcalender().enqueue(object : Callback<ResponseCalendar>{
            override fun onResponse(
                call: Call<ResponseCalendar>,
                response: Response<ResponseCalendar>
            ) {
                if(response.isSuccessful){
                    val responseCalendar = response.body()
                       responseCalendar?.let { calendar ->
                            // 노란색 점 표시
                            calendar.diary.forEach { diary ->
                            calendarView.addDecorator(YellowPointDecorator(diary))
                            }

                            // 파란색 점 표시
                            calendar.income.forEach { income ->
                            calendarView.addDecorator(BluePointDecorator(income))
                        }

                            // 빨간색 점 표시
                            calendar.expense.forEach { expense ->
                            calendarView.addDecorator(RedPointDecorator(expense))
                            }
                        }
                    calendarView.invalidateDecorators() // 모든 Decorator 추가 후 캘린더 뷰 갱신

//                        val diaryDates = response.body()?.diary?.map { dateString ->
//                        val dateParts = dateString.split("-").map { it.toInt() }
//                        CalendarDay.from(dateParts[0], dateParts[1] - 1, dateParts[2]) // CalendarDay의 월은 0부터 시작하므로 1을 빼줍니다.
//                    }
//                        val incomeDates = response.body()?.income?.map { dateString ->
//                        val dateParts = dateString.split("-").map { it.toInt() }
//                        CalendarDay.from(dateParts[0], dateParts[1] - 1, dateParts[2])
//                    }
//                        val expenseDates = response.body()?.expense?.map { dateString ->
//                        val dateParts = dateString.split("-").map { it.toInt() }
//                        CalendarDay.from(dateParts[0], dateParts[1] - 1, dateParts[2])
//                    }
//                    diaryDates?.forEach { calendarView.addDecorator(DiaryDecorator(it)) }
//                    incomeDates?.forEach { calendarView.addDecorator(IncomeDecorator(it)) }
//                    expenseDates?.forEach { calendarView.addDecorator(ExpenseDecorator(it)) }
                }
            }
            override fun onFailure(call: Call<ResponseCalendar>, t: Throwable) {
                Log.d("get main calender", "실패$t")
                Toast.makeText(applicationContext, "정보를 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
            }

        })

//                    val responsediary = response.body()!!.diary
//                    val responseexpense = response.body()!!.expense
//                    val responseincome = response.body()!!.income
//
//                    // Convert data to CalendarDay objects
//                    val diaryDates = responsediary.map { LocalDate.parse(it).toCalendarDay() }
//                    val expenseDates = responseexpense.map { LocalDate.parse(it).toCalendarDay() }
//                    val incomeDates = responseincome.map { LocalDate.parse(it).toCalendarDay() }
//
//                    // Combine dates into a single map
//                    val dateMap = mutableMapOf<CalendarDay, IntArray>()
//                    diaryDates.forEach { dateMap.getOrPut(it) { IntArray(3) }[0] = Color.YELLOW }
//                    incomeDates.forEach { dateMap.getOrPut(it) { IntArray(3) }[1] = Color.BLUE }
//                    expenseDates.forEach { dateMap.getOrPut(it) { IntArray(3) }[2] = Color.RED }
//
//                    // Add Decorators for each date
//                    dateMap.forEach { (date, colors) ->
//                        calendarView.addDecorator(EventDecorator(colors, listOf(date)))
//                    }





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
        data_ex.api.getExpendAll().enqueue(object : Callback<ResponseExpend> {
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
                Toast.makeText(applicationContext, "정보를 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
            }

        })


        // 수입 내역 화면에 보여주기
        re_income = findViewById<RecyclerView>(R.id.income_re)
        re_income.layoutManager = LinearLayoutManager(this)
        data_in.api.getIncomeAll().enqueue(object : Callback<ResponseIncome> {
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
                Toast.makeText(applicationContext, "정보를 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
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
    fun getCurrentFormattedDate(): String {
        val currentDate = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return currentDate.format(formatter)
    }

    fun LocalDate.toCalendarDay(): CalendarDay = CalendarDay.from(year, monthValue - 1, dayOfMonth)


}

