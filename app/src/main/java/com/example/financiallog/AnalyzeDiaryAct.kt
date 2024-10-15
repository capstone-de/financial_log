package com.example.financiallog

import android.content.Intent
import android.graphics.BitmapFactory
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
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.ScatterChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.ScatterData
import com.github.mikephil.charting.data.ScatterDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.android.material.bottomnavigation.BottomNavigationView
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class AnalyzeDiaryAct: AppCompatActivity() {
    val hashtag_data : ApiObject by lazy { ApiObject() }; lateinit var mFormat: SimpleDateFormat;
    lateinit var currentDate: Date; var selectedMonth: Date = Date(); lateinit var diary_chat1: ScatterChart;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.analyze_diary)

        val analyze_btn = findViewById<Button>(R.id.btn_analyze)
        val analyzedi_btn = findViewById<Button>(R.id.btn_diary)
        val diary_text1 = findViewById<TextView>(R.id.diary_text1)
        val diary_text2 = findViewById<TextView>(R.id.diary_text2)
        val diary_view1 = findViewById<ImageView>(R.id.imageView5)
        val diary_view2 = findViewById<ImageView>(R.id.imageView6)

        //감정소비분석
        val diary_text3 = findViewById<TextView>(R.id.diary_text3)
        diary_chat1 = findViewById<ScatterChart>(R.id.chart1)

        //위치소비분석
        //val diary_text4 = findViewById<TextView>(R.id.diary_text4)
        //val diary_chat2 = findViewById<ScatterChart>(R.id.chart2)

        //val monthText = findViewById<TextView>(R.id.monthly_tv)

        // 날짜 표시
        mFormat = SimpleDateFormat("yyyy년 MM월", Locale.KOREAN)
        currentDate = Date()
        //monthText.text = mFormat.format(currentDate)

        //month_btn.setOnClickListener { showMonthPickerDialog() } // 다른 달 선택 다이얼로그 표시

        //selectedMonth = Date() // 오늘 날짜로 초기화
        val calendar = Calendar.getInstance() // 현재 날짜와 시간으로 초기화된 Calendar 인스턴스를 가져옵니다.
        val year = calendar.get(Calendar.YEAR) // 연도를 가져옵니다.
        val month = calendar.get(Calendar.MONTH) + 1 // 월을 가져옵니다. (Calendar.MONTH는 0부터 시작하므로 +1을 해줍니다.)


        //감정소비분석차트
        getDataForSentiment(year,month) // 오늘 날짜 데이터 불러오기

        //위치소비분석차트
        getDataForLocation(year,month)


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
        hashtag_data.api.getStatisticsMyHashtag().enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    response.body()?.let { responseBody ->
                        val inputStream = responseBody.byteStream()
                        val bitmap = BitmapFactory.decodeStream(inputStream)
                        runOnUiThread {
                            diary_view1.setImageBitmap(bitmap)
                        }
                    }
                } else {
                    Toast.makeText(applicationContext, "이미지를 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("이미지로드", "실패: ${t.message}")
            }
        })

        //해시태그 트랜드
        hashtag_data.api.getStatisticsHashtag().enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    response.body()?.let { responseBody ->
                        val inputStream = responseBody.byteStream()
                        val bitmap = BitmapFactory.decodeStream(inputStream)
                        runOnUiThread {
                            diary_view2.setImageBitmap(bitmap)
                        }
                    }
                } else {
                    Toast.makeText(applicationContext, "이미지를 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("이미지로드", "실패: ${t.message}")
            }
        })

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
                    hashtag_data.api.diarywriteEx(1,getCurrentFormattedDate()).enqueue(object : Callback<List<DataEx>> {
                        override fun onResponse(call: Call<List<DataEx>>, response: Response<List<DataEx>>) {
                            if (response.isSuccessful && response.body() != null) {
                                // 네트워크 응답이 성공적이고 데이터가 있는 경우
                                val intent = Intent(this@AnalyzeDiaryAct, DiaryWriteAct::class.java)
                                startActivity(intent)
                            } else {
                                // 네트워크 응답이 실패했거나 데이터가 없는 경우
                                Toast.makeText(this@AnalyzeDiaryAct, "이미 저장된 일기가 있습니다.", Toast.LENGTH_SHORT).show()
                                dialog.dismiss() // 다이얼로그 닫기
                            }
                        }

                        override fun onFailure(call: Call<List<DataEx>>, t: Throwable) {
                            // 네트워크 요청 실패 시
                            Toast.makeText(this@AnalyzeDiaryAct, "네트워크 요청에 실패했습니다.", Toast.LENGTH_SHORT).show()
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
    fun getCurrentFormattedDate(): String {
        val currentDate = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return currentDate.format(formatter)
    }

    private fun showMonthPickerDialog() {
        val calendar = Calendar.getInstance()

        // 다이얼로그에 표시할 월 목록 생성
        val months = (1..12).map { month -> "${String.format("%02d", month)}월" }.toTypedArray()

        AlertDialog.Builder(this)
            .setTitle("달 선택")
            .setItems(months) { _, which ->
                calendar.set(Calendar.MONTH, which)
                val year = calendar.get(Calendar.YEAR)
                val month = which + 1 // Calendar.MONTH는 0부터 시작하므로 +1 필요
                // 사용자가 선택한 달로 selectedMonth 업데이트
                selectedMonth = calendar.time

                // 선택한 달에 해당하는 데이터로 업데이트
                getDataForSentiment(year, month)
                getDataForLocation(year, month)
            }
            .show()
    }
    // 데이터 가져오기
    private fun getDataForSentiment(year: Int, month: Int) {
        val yearStr = year.toString()
        val monthStr = month.toString().padStart(2, '0')

        hashtag_data.api.getsentimentAnalysis(1, yearStr, monthStr).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    response.body()?.let { data ->
                        Log.d("데이터 받기", data.toString())

                        // JSON 파싱 및 데이터 업데이트
                        val jsonObject = JSONObject(data.string())
                        val coordinates = jsonObject.getJSONArray("coordinate")
                        val correlation = jsonObject.getDouble("correlation")

                        // 차트를 업데이트
                        updateEmotionConsumptionChart(coordinates)

                    } ?: run {
                        // 응답은 성공적이지만 body가 null인 경우 처리
                    }
                } else {
                    // 응답 실패 처리
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                // API 호출 실패 처리
                Log.e("API 호출 실패", t.message.toString())
            }
        })
    }

    // 감정 소비 분석 차트 업데이트
    private fun updateEmotionConsumptionChart(coordinates: JSONArray) {
        val entries = ArrayList<Entry>()

        // 데이터 포인트 추가
        for (i in 0 until coordinates.length()) {
            val point = coordinates.getJSONArray(i)
            val xValue = point.getDouble(0).toFloat()  // 감정 분석 결과 (x)
            val yValue = point.getDouble(1).toFloat()  // 금액 (y)
            entries.add(Entry(xValue, yValue))
        }

        val dataSet = ScatterDataSet(entries, "감정 소비 분석")
        dataSet.color = ColorTemplate.COLORFUL_COLORS[0]
        dataSet.setScatterShape(ScatterChart.ScatterShape.CIRCLE)
        dataSet.scatterShapeSize = 10f

        val scatterData = ScatterData(dataSet)
        diary_chat1.data = scatterData
        diary_chat1.invalidate() // 차트 업데이트
    }

    private fun getDataForLocation(year: Int, month: Int) {
        val yearStr = year.toString()
        val monthStr = month.toString().padStart(2, '0')
    }


}