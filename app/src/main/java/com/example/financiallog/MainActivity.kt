package com.example.financiallog

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CalendarView
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    lateinit var mainlog: ImageView;
    lateinit var text1 : TextView;
    lateinit var text2 : TextView;
    lateinit var startbtn: Button;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_home)

        mainlog = findViewById<ImageView>(R.id.logo_image)
        text1 = findViewById<TextView>(R.id.textView5)
        text2 = findViewById<TextView>(R.id.textView4)
        startbtn = findViewById<Button>(R.id.start_btn)

        //시작 버튼 이벤트
        startbtn.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, HomeMain::class.java)
            startActivity(intent)
        })



    }
}