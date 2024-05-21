package com.example.financiallog

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AnalyzeDiaryAct: AppCompatActivity() {
    val hashtag_data : ApiObject by lazy { ApiObject() };
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.analyze_diary)

        val analyze_btn = findViewById<Button>(R.id.btn_analyze)
        val analyzedi_btn = findViewById<Button>(R.id.btn_diary)
        val diary_text1 = findViewById<TextView>(R.id.diary_text1)
        val diary_text2 = findViewById<TextView>(R.id.diary_text2)
        val diary_view1 = findViewById<ImageView>(R.id.imageView5)
        val diary_view2 = findViewById<ImageView>(R.id.imageView6)

        //가계부 버튼 클릭 시
        analyze_btn.setOnClickListener(View.OnClickListener{
            val intent = Intent(this, AnalyzeDayAct::class.java)
            startActivity(intent)
        })

        //일기 버튼 클릭 시
        analyzedi_btn.setOnClickListener(View.OnClickListener{
            val intent = Intent(this, AnalyzeDiaryAct::class.java)
            startActivity(intent)
        })

        //하단바
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

        //나의 관심사
        hashtag_data.api.getStatisticsMyHashtag().enqueue(object : Callback<ResponseMyHashtag> {
            override fun onResponse(
                call: Call<ResponseMyHashtag>,
                response: Response<ResponseMyHashtag>
            ) {
                if(response.isSuccessful){
                    val data = response.body()!!
                    val imgurl = data.img.url

                    //Glide로 이미지 설정
                    Glide.with(this@AnalyzeDiaryAct)
                        .load(imgurl)
                        .into(diary_view1)

                    Toast.makeText(applicationContext, "나의 관심사 data 가져옴", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseMyHashtag>, t: Throwable) {
                Log.d("나의현재트렌드","정보를 가져오지 못함")
            }

        })

        //해시 태그 트렌드
        hashtag_data.api.getStatisticsHashtag().enqueue(object :Callback<ResponseStatHasgtag>{
            override fun onResponse(
                call: Call<ResponseStatHasgtag>,
                response: Response<ResponseStatHasgtag>
            ) {
                if(response.isSuccessful){
                    val hashtagdata = response.body()!!
                    val hashurl = hashtagdata.img.url

                    Glide.with(this@AnalyzeDiaryAct)
                        .load(hashurl)
                        .into(diary_view2)

                    Toast.makeText(applicationContext, "모든 관심사 data 가져옴", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseStatHasgtag>, t: Throwable) {
                Log.d("트렌드","정보를 가져오지 못함")
            }

        })

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

}