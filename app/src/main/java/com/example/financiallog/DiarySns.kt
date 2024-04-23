package com.example.financiallog

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView


class DiarySns : AppCompatActivity(){

    lateinit var year_tv : TextView;
    lateinit var month_tv : TextView;
    lateinit var day_tv : TextView;
    lateinit var feed_list: RecyclerView;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sns_feed)

        year_tv = findViewById<TextView>(R.id.year_text)
        month_tv = findViewById<TextView>(R.id.month_text)
        day_tv = findViewById<TextView>(R.id.day_text)

        // 일기리스트 화면에 보여주기
        feed_list = findViewById<RecyclerView>(R.id.feed_re)
        feed_list.layoutManager = LinearLayoutManager(this)
        val adapter_my = DiaryListAdapter()
        feed_list.adapter = adapter_my


        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_view)
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
                    val intent = Intent(this, ExpendAct::class.java)
                    startActivity(intent)
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
}