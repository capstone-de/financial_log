package com.example.financiallog

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import android.content.Intent
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.widget.Toast


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









    }
}