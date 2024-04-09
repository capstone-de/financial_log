package com.example.financiallog

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckedTextView
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SaveCheck : AppCompatActivity() {

    lateinit var tv_1 : TextView;
    lateinit var tv_2 : TextView;
    lateinit var tv_3 : CheckedTextView;
    lateinit var iv_1 : ImageView;
    lateinit var btn_home :Button;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.save_check)

        tv_1 = findViewById<TextView>(R.id.textView)
        tv_2 = findViewById<TextView>(R.id.textView2)
        tv_3 = findViewById<CheckedTextView >(R.id.checkedTextView)
        iv_1 = findViewById<ImageView>(R.id.imageView)
        btn_home = findViewById<Button>(R.id.imageButton)

        // 텍스트 클릭시 이동
        tv_3.setOnClickListener{
            val inent_1 = Intent(this, DiaryWriteAct::class.java)
            startActivity(inent_1)

        }

        // X버튼 클릭시 이동
        btn_home.setOnClickListener{
            val intent_2 = Intent(this, HomeMain::class.java)
            startActivity(intent_2)
        }




    }
}