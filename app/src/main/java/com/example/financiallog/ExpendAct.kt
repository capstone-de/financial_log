package com.example.financiallog

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class ExpendAct : AppCompatActivity() {

    lateinit var btn_exsave : Button;
    lateinit var income_btn : Button;
    lateinit var expend_btn : Button;
    lateinit var x_btn :ImageButton;
    lateinit var pay_tv : TextView;
    lateinit var categ_tv:TextView;
    lateinit var shop_name :TextView;
    lateinit var toget_tv :TextView;
    lateinit var ed_pay:EditText;
    lateinit var ed_shop:EditText;
    lateinit var ed_toget:EditText;
    lateinit var tv_exsat:TextView;
    lateinit var alone_chip: CheckBox;
    lateinit var group_expend:ChipGroup;
    lateinit var seek_bar:SeekBar;
    lateinit var seek_zero : TextView;
    lateinit var seek_per :TextView;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.save_expend)

        x_btn = findViewById<ImageButton>(R.id.imageButton2)
        income_btn = findViewById<Button>(R.id.button)
        expend_btn = findViewById<Button>(R.id.button2)
        btn_exsave= findViewById<Button>(R.id.save_expend)
        pay_tv = findViewById<TextView>(R.id.money)
        ed_pay = findViewById<EditText>(R.id.money_ed)
        categ_tv =findViewById<TextView>(R.id.category)
        shop_name = findViewById<TextView>(R.id.name_shop)
        ed_shop = findViewById<EditText>(R.id.name_shop_ed)
        toget_tv =findViewById<TextView>(R.id.together)
        ed_toget = findViewById<EditText>(R.id.together_ed)
        tv_exsat = findViewById<TextView>(R.id.ex_satis)
        alone_chip = findViewById<CheckBox>(R.id.checkBox)
        group_expend = findViewById<ChipGroup>(R.id.expend_group)
        seek_bar =findViewById<SeekBar>(R.id.seekBar)
        seek_zero = findViewById<TextView>(R.id.progressTv)
        seek_per =findViewById<TextView>(R.id.totalTv)



        //수입 버튼
        income_btn.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, IncomeAct::class.java)
            startActivity(intent)
        })

        //지출 버튼
        expend_btn.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, ExpendAct::class.java)
            startActivity(intent)
        })


        //X 버튼
        x_btn.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, HomeMain::class.java)
            startActivity(intent)
        })


        //카테고리 선택 시


        //저장 버튼
        btn_exsave.setOnClickListener(View.OnClickListener {

            val intent = Intent(this, SaveCheck::class.java)
            startActivity(intent)
        })





    }
}