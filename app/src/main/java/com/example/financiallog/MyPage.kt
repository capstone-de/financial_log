package com.example.financiallog

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import java.text.SimpleDateFormat
import java.util.Date


class MyPage : AppCompatActivity() {

    lateinit var year_tv: TextView;
    lateinit var following: TextView;
    lateinit var following_ntv: TextView;
    lateinit var follower: TextView;
    lateinit var follower_ntv: TextView;
    lateinit var user_id: TextView;
    lateinit var mypage_list: RecyclerView;
    lateinit var btn_more: Button; lateinit var today :Date; lateinit var mFormat :SimpleDateFormat;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mypage_home)

        year_tv = findViewById<TextView>(R.id.year_text)
        following = findViewById<TextView>(R.id.textView6)
        following_ntv = findViewById<TextView>(R.id.follow_num)
        follower = findViewById<TextView>(R.id.follower_tv)
        follower_ntv = findViewById<TextView>(R.id.follower_num)
        user_id = findViewById<TextView>(R.id.textView3)
        //btn_more = findViewById<Button>(R.id.imageButton4)

        // 날짜
        mFormat = SimpleDateFormat("yyyy.MM.dd")
        year_tv.setText(getTime())

        // mypage_list = findViewById<ScrollView>(R.id.mypage_re)

        // 팔로워 사람 선택 시 조회

        //팔로잉 사람 선택 시 조회

        // 일기리스트 화면에 보여주기
        mypage_list = findViewById<RecyclerView>(R.id.mypage_re)
        mypage_list.layoutManager = LinearLayoutManager(this)
        val adapter_my = DiaryListAdapter()
        mypage_list.adapter = adapter_my

        //하단바 버튼
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_view)
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    val intent = Intent(this, HomeMain::class.java)
                    startActivity(intent)
                    Toast.makeText(applicationContext, "home", Toast.LENGTH_SHORT).show()
                }

                R.id.financial -> {
                    Toast.makeText(applicationContext, "financial", Toast.LENGTH_SHORT).show()
                }

                R.id.add -> {
                    showMoreMenu()
                    true
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
    private fun getTime(): String? {
        var mNow = System.currentTimeMillis()
        today = Date(mNow)
        return mFormat.format(today)
    }
}