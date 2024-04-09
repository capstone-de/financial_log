package com.example.financiallog

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.chip.ChipGroup

class IncomeAct : AppCompatActivity() {

    lateinit var btn_x :ImageButton;
    lateinit var btn_incom : Button;
    lateinit var btn_expend : Button;
    lateinit var tv_pay : TextView;
    lateinit var ed_pay : EditText;
    lateinit var tv_cate : TextView;
    lateinit var tv_memo : TextView;
    lateinit var ed_memo :EditText;
    lateinit var group_income : ChipGroup;
    lateinit var btn_save: Button;

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.save_income)

        btn_x = findViewById<ImageButton>(R.id.imageButton1)
        btn_incom = findViewById<Button>(R.id.button)
        btn_expend = findViewById<Button>(R.id.button2)
        tv_pay = findViewById<TextView>(R.id.money)
        ed_pay = findViewById<EditText>(R.id.money_ed)
        tv_cate = findViewById<TextView>(R.id.category)
        tv_memo =findViewById<TextView>(R.id.memo)
        ed_memo =findViewById<EditText>(R.id.memo_ed)
        group_income = findViewById<ChipGroup>(R.id.income_group)
        btn_save = findViewById<Button>(R.id.save_expend)

        // 저장 버튼 시


        // 수입 버튼



        // 지출 버튼




    }
}
