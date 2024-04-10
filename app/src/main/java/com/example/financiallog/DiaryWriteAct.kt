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
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class DiaryWriteAct : AppCompatActivity() {

    lateinit var dx_btn: ImageButton; lateinit var photo_btn :ImageButton;
    lateinit var diary_save : Button; lateinit var group_op:ChipGroup;
    lateinit var date_tv :TextView; lateinit var exdiary_tv :TextView;
    lateinit var re_expend: RecyclerView; lateinit var ed_diary : EditText;
    lateinit var tag_tv:TextView; lateinit var together_tv:TextView;
    lateinit var tag_chip:Chip; lateinit var photo_tv:ImageView;
    lateinit var photo_tv1:ImageView; lateinit var photo_tv2:ImageView;

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.diary_write)

        dx_btn = findViewById<ImageButton>(R.id.imageButton3)
        photo_btn = findViewById<ImageButton>(R.id.camera_btn)
        diary_save = findViewById<Button>(R.id.save_btn)
        group_op =findViewById<ChipGroup>(R.id.open_chip)
        date_tv = findViewById<TextView>(R.id.date_View)
        exdiary_tv = findViewById<TextView>(R.id.today_diary)
        re_expend = findViewById<RecyclerView>(R.id.diary_re)
        ed_diary = findViewById<EditText>(R.id.diary_ed)
        tag_tv = findViewById<TextView>(R.id.tag_view)
        together_tv = findViewById<TextView>(R.id.toget_view)
        tag_chip = findViewById<Chip>(R.id.tagchip)
        photo_tv = findViewById<ImageView>(R.id.diary_image1)
        photo_tv1 = findViewById<ImageView>(R.id.diary_image2)
        photo_tv2 = findViewById<ImageView>(R.id.diary_image3)


        // X 버튼을 눌렀을 때
        dx_btn.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, HomeMain::class.java)
            startActivity(intent)
        })


        // 날짜 불러오기


        // 지출 내역 불러오기


        // 공개 비공개 눌렀을 때


        //해시태크 추가하기


        // 사진 버튼 눌렀을때


        // 저장하기 버튼 눌렀을 때
        diary_save.setOnClickListener(View.OnClickListener {

            val intent = Intent(this, SaveDiary::class.java)
            startActivity(intent)
        })






    }
}