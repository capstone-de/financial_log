package com.example.financiallog

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckedTextView
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SaveDiary : AppCompatActivity() {

    lateinit var save_tv1 : TextView;
    lateinit var check_tv1 : ImageView;
    lateinit var home_btn : Button;

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.save_check_diary)

        save_tv1 = findViewById<TextView>(R.id.saveText)
        check_tv1 = findViewById<ImageView>(R.id.checkImView)
        home_btn = findViewById<Button>(R.id.x_Imagebtn)

        // X버튼 클릭시 이동
        home_btn.setOnClickListener{
            val intent = Intent(this, HomeMain::class.java)
            startActivity(intent)
        }


    }
}