package com.example.financiallog

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CalendarView
import android.widget.TextView
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    lateinit var cal : CalendarView;
    lateinit var datetext : TextView;
    lateinit var expendtext : TextView;
    lateinit var plusbtn: Button;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        cal = findViewById<CalendarView>(R.id.calView)
        datetext = findViewById<TextView>(R.id.date_text)
        expendtext = findViewById<TextView>(R.id.expend_text)
        plusbtn = findViewById<Button>(R.id.plus_btn)

        val bottom = findViewById<BottomNavigationView>(R.id.bottom_navigation_view)


        /*val formatter: DateFormat = SimpleDateFormat("yyyy년 MM월 dd일")
        val date = Date(cal.getDate())
        datetext.setText(formatter.format(date))

        cal.setOnDateChangeListener(OnDateChangeListener { view, year, month, dayOfMonth ->
            val day: String
            day = year.toString() + "년" + (month + 1) + "월" + dayOfMonth + "일"
            datetext.setText(day)
        })*/


    }
}