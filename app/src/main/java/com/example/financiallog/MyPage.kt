package com.example.financiallog

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date


class MyPage : AppCompatActivity() {

    lateinit var year_tv: TextView;
    lateinit var following: TextView;
    lateinit var following_ntv: TextView;
    lateinit var follower: TextView;
    lateinit var follower_ntv: TextView;
    lateinit var user_nickname: TextView;
    lateinit var btn_more: Button; lateinit var today :Date; lateinit var mFormat :SimpleDateFormat;
    val list_data :ApiObject by lazy { ApiObject() }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mypage_home)

        year_tv = findViewById<TextView>(R.id.year_text)
        following = findViewById<TextView>(R.id.textView6)
        following_ntv = findViewById<TextView>(R.id.following_num)
        follower = findViewById<TextView>(R.id.follower_tv)
        follower_ntv = findViewById<TextView>(R.id.follow_num)
        user_nickname = findViewById<TextView>(R.id.textView3)
        //btn_more = findViewById<Button>(R.id.imageButton4)

        // 날짜
        mFormat = SimpleDateFormat("yyyy.MM.dd")
        year_tv.setText(getTime())

        // 팔로워 사람 선택 시 조회

        //팔로잉 사람 선택 시 조회


        // 일기리스트 화면에 보여주기
        var mypage_list = findViewById<RecyclerView>(R.id.mypage_re)
        mypage_list.layoutManager = LinearLayoutManager(this)

        list_data.api.getDiaryMylist().enqueue(object :Callback<ResponseMyDiary>{
            override fun onResponse(
                call: Call<ResponseMyDiary>,
                response: Response<ResponseMyDiary>

            ) {
                Log.d("data",response.body().toString())
                if(response.isSuccessful){
                    val nicknamedata = response.body()!!.nickname
                    user_nickname.text = nicknamedata

                    val followerdata = response.body()!!.follower
                    follower_ntv.setText(followerdata.toString())

                    val followingdata = response.body()!!.following
                    following_ntv.setText(followingdata.toString())

                    val data = response.body()!!.myDiaryList
                    val mylistadapter = DiaryMyListAdapter(data)
                    mypage_list.adapter = mylistadapter

                    Toast.makeText(applicationContext, "모든 정보를 가져왔습니다.", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<ResponseMyDiary>, t: Throwable) {
                Log.d("response", "실패$t")
                Toast.makeText(applicationContext, "정보를 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
            }

        })


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
                    val intent = Intent(this, AnalyzeDayAct::class.java)
                    startActivity(intent)
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

        // 새로운 BottomNavigationView를 화면에 표시합니다.
        // 여기서는 예시로 다이얼로그 형태로 표시하였습니다.
        val dialog = AlertDialog.Builder(this)
            .setView(moreBottomView)
            .create()
        dialog.show()

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
                    // Retrofit 서비스 호출
                    list_data.api.diarywriteEx(6,getCurrentFormattedDate()).enqueue(object : Callback<List<DataEx>> {
                        override fun onResponse(call: Call<List<DataEx>>, response: Response<List<DataEx>>) {
                            if (response.isSuccessful && response.body() != null) {
                                // 네트워크 응답이 성공적이고 데이터가 있는 경우
                                val intent = Intent(this@MyPage, DiaryWriteAct::class.java)
                                startActivity(intent)
                            } else {
                                // 네트워크 응답이 실패했거나 데이터가 없는 경우
                                Toast.makeText(this@MyPage, "이미 저장된 일기가 있습니다.", Toast.LENGTH_SHORT).show()
                                dialog.dismiss() // 다이얼로그 닫기
                            }
                        }

                        override fun onFailure(call: Call<List<DataEx>>, t: Throwable) {
                            // 네트워크 요청 실패 시
                            Toast.makeText(this@MyPage, "네트워크 요청에 실패했습니다.", Toast.LENGTH_SHORT).show()
                            t.printStackTrace()  // 오류 스택 트레이스를 출력하여 디버깅에 도움을 줌
                            dialog.dismiss() // 다이얼로그 닫기
                        }
                    })
                    true
                }
                else -> false
            }
        }

    }
    private fun getTime(): String? {
        var mNow = System.currentTimeMillis()
        today = Date(mNow)
        return mFormat.format(today)
    }
    fun getCurrentFormattedDate(): String {
        val currentDate = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return currentDate.format(formatter)
    }
}