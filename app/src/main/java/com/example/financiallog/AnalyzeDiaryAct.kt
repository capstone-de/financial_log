package com.example.financiallog

import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.github.mikephil.charting.charts.BubbleChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.ScatterChart
import com.github.mikephil.charting.data.BubbleData
import com.github.mikephil.charting.data.BubbleDataSet
import com.github.mikephil.charting.data.BubbleEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.ScatterData
import com.github.mikephil.charting.data.ScatterDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
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

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import com.google.android.gms.maps.model.BitmapDescriptorFactory


class AnalyzeDiaryAct: AppCompatActivity(), OnMapReadyCallback {
    val hashtag_data : ApiObject by lazy { ApiObject() }; lateinit var mFormat: SimpleDateFormat;
    val location_data : ApiObject by lazy { ApiObject() };
    lateinit var currentDate: Date; var selectedMonth: Date = Date(); lateinit var diary_chat1: ScatterChart;
    lateinit var mMap: GoogleMap; lateinit var monthText: TextView; lateinit var monthText1: TextView; lateinit var emotion_result: TextView;
    lateinit var btn1 :ImageButton; lateinit var btn2 :ImageButton;
    private var selectedMonthForBtn1: Date? = null
    private var selectedMonthForBtn2: Date? = null
    // 구와 좌표 매핑
    private val districtCoordinates = mapOf(
        "종로구" to LatLng(37.5702, 126.9985),
        "중구" to LatLng(37.5636, 126.9970),
        "용산구" to LatLng(37.5326, 126.9770),
        "성동구" to LatLng(37.5630, 127.0357),
        "광진구" to LatLng(37.5382, 127.0822),
        "동대문구" to LatLng(37.5744, 127.0420),
        "중랑구" to LatLng(37.6010, 127.0892),
        "성북구" to LatLng(37.6026, 127.0170),
        "강북구" to LatLng(37.6340, 127.0246),
        "도봉구" to LatLng(37.6690, 127.0437),
        "노원구" to LatLng(37.6555, 127.0772),
        "은평구" to LatLng(37.6045, 126.9331),
        "서대문구" to LatLng(37.5794, 126.9631),
        "마포구" to LatLng(37.5547, 126.9032),
        "양천구" to LatLng(37.5164, 126.8678),
        "강서구" to LatLng(37.5504, 126.8359),
        "구로구" to LatLng(37.4958, 126.8846),
        "금천구" to LatLng(37.4527, 126.8950),
        "영등포구" to LatLng(37.5262, 126.9001),
        "동작구" to LatLng(37.5048, 126.9647),
        "관악구" to LatLng(37.4784, 126.9521),
        "서초구" to LatLng(37.4838, 127.0324),
        "강남구" to LatLng(37.4979, 127.0276),
        "송파구" to LatLng(37.5042, 127.1063),
        "강동구" to LatLng(37.5307, 127.1238)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.analyze_diary)

        val analyze_btn = findViewById<Button>(R.id.btn_analyze)
        val analyzedi_btn = findViewById<Button>(R.id.btn_diary)
        val diary_text1 = findViewById<TextView>(R.id.diary_text1)
        val diary_text2 = findViewById<TextView>(R.id.diary_text2)
        val diary_view1 = findViewById<ImageView>(R.id.imageView5)
        val diary_view2 = findViewById<ImageView>(R.id.imageView6)
        btn1 = findViewById(R.id.m_underButton)
        btn2= findViewById(R.id.m_underButton_loc)

        //감정소비분석
        val diary_text3 = findViewById<TextView>(R.id.diary_text3)
        diary_chat1 = findViewById<ScatterChart>(R.id.chart1)
        emotion_result = findViewById(R.id.diary_emotion_result)

        //위치소비분석
        val diary_text4 = findViewById<TextView>(R.id.diary_text_loc)

        // SupportMapFragment 초기화
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)



        monthText = findViewById(R.id.monthly_tv_emo)
        monthText1 = findViewById(R.id.monthly_tv_loc)

        // 날짜 표시
        mFormat = SimpleDateFormat("yyyy년 MM월", Locale.KOREAN)
        currentDate = Date()
        monthText.text = mFormat.format(currentDate)
        monthText1.text = mFormat.format(currentDate)

        btn1.setOnClickListener { showMonthPickerDialog(true) } // btn1 클릭 시 다이얼로그 표시
        btn2.setOnClickListener { showMonthPickerDialog(false) } // btn2 클릭 시 다이얼로그 표시


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

    // 지도 준비가 완료되면 호출됨
    override fun onMapReady(map: GoogleMap) {
        mMap = map // mMap에 지도 객체 할당
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(37.5665, 126.978), 10f))

        // 서울 중심 위치로 카메라 이동 후, API 데이터로 마커 추가
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1

        getDataForLocation(year, month) // API 데이터 사용해서 마커 추가

    }

    /*private fun setupMap() {
        // 서울 중심 위치로 카메라 이동
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(37.5665, 126.978), 10f))

        // 예시 소비 데이터 (위치와 소비 금액)
        /*val spendingData = listOf(
            Pair(LatLng(37.5665, 126.978), 10000), // 서울시청
            Pair(LatLng(37.4873, 126.8526), 20000), // 강서구
            Pair(LatLng(37.5326, 127.0292), 30000)  // 성남시
        )*/

        // 소비 데이터에 따라 마커 추가
        for ((location, amount) in spendingData) {
            addBubbleMarker(location, amount)
        }
    }*/

    private fun getDataForLocation( year: Int, month: Int) {
        val yearStr = year.toString()
        val monthStr = month.toString().padStart(2, '0')


        location_data.api.getLocationAnalysis(3, yearStr, monthStr).enqueue(object : Callback<List<ResponseLocation>> {
            override fun onResponse(call: Call<List<ResponseLocation>>, response: Response<List<ResponseLocation>>) {
                if (response.isSuccessful) {
                    response.body()?.let { responseDataList ->
                        // 여러 구 데이터를 반복문으로 처리
                        for (responseData in responseDataList) {
                            // total_expenditure가 0이 아닌 경우에만 마커 추가
                            if (responseData.total_expenditure > 0) {
                                val location = districtCoordinates[responseData.gu]
                                if (location != null) {
                                    // 지역 이름과 소비 금액을 함께 전달
                                    addBubbleMarker(location, responseData.total_expenditure, responseData.gu)

                                }
                            }
                        }
                    }
                } else {
                    Log.e("API 에러", "Response code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<ResponseLocation>>, t: Throwable) {
                Log.e("API 에러", "네트워크 요청 실패: ${t.message}")
            }
        })

    }



    //버블마커 추가 함수
    private fun addBubbleMarker(location: LatLng, spendingAmount: Int, gu: String) {
        val icon = BitmapDescriptorFactory.fromBitmap(createCircleMarker(spendingAmount))

        val markerOptions = MarkerOptions()
            .position(location)
            .icon(icon)
            .title("위치: $gu, 소비금액: $spendingAmount 원")

        mMap.addMarker(markerOptions) // mMap에 마커 추가
    }

    //마커스타일링
    private fun createCircleMarker(spendingAmount: Int): Bitmap {
        // 소비 금액에 따라 동적으로 크기 조절 (예시: 최소 50, 최대 200 사이 크기 설정)
        val minSize = 50
        val maxSize = 200
        val size = ((spendingAmount / 10000f).coerceIn(1f, 4f) * minSize).toInt().coerceAtMost(maxSize)

        val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val paint = Paint()

        // 마커 색상 설정
        paint.color = Color.parseColor("#FF6A6A");
        canvas.drawCircle((size / 2).toFloat(), (size / 2).toFloat(), (size / 2).toFloat(), paint) // 동그란 마커 그리기

        // 소비금액을 텍스트로 추가
        paint.color = Color.BLACK
        paint.textAlign = Paint.Align.CENTER
        paint.textSize = size / 5f // 크기에 따라 텍스트 사이즈 조정
        canvas.drawText(spendingAmount.toString(), (size / 2).toFloat(), (size / 2 + size / 8).toFloat(), paint)

        return bitmap
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
                    hashtag_data.api.diarywriteEx(3,getCurrentFormattedDate()).enqueue(object : Callback<List<DataEx>> {
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

    private fun showMonthPickerDialog(isForBtn1: Boolean) {
        val calendar = Calendar.getInstance()

        // 다이얼로그에 표시할 월 목록 생성
        val months = (1..12).map { month -> "${String.format("%02d", month)}월" }.toTypedArray()

        AlertDialog.Builder(this)
            .setTitle("달 선택")
            .setItems(months) { _, which ->
                calendar.set(Calendar.MONTH, which)
                val year = calendar.get(Calendar.YEAR)
                val month = which + 1 // Calendar.MONTH는 0부터 시작하므로 +1 필요

                // 선택한 달에 해당하는 데이터로 업데이트
                if (isForBtn1) {
                    selectedMonthForBtn1 = calendar.time
                    monthText.text = SimpleDateFormat("yyyy년 MM월", Locale.getDefault()).format(selectedMonthForBtn1)
                    getDataForSentiment(year, month)
                } else {
                    selectedMonthForBtn2 = calendar.time
                    monthText1.text = SimpleDateFormat("yyyy년 MM월", Locale.getDefault()).format(selectedMonthForBtn2)
                    getDataForLocation(year, month)
                }
            }
            .show()
    }
    // 데이터 가져오기
    private fun getDataForSentiment(year: Int, month: Int) {
        val yearStr = year.toString()
        val monthStr = month.toString().padStart(2, '0')

        hashtag_data.api.getsentimentAnalysis(3, yearStr, monthStr).enqueue(object : Callback<ResponseSentiment> {
            override fun onResponse(call: Call<ResponseSentiment>, response: Response<ResponseSentiment>) {
                if (response.isSuccessful) {
                    response.body()?.let { data ->
                        Log.d("데이터 받기", data.toString())
                        Log.d("Chart Data", "Coordinates: ${data.coordinate}") // 로그 추가

                        // Coordinate를 List로 가져오기
                        val coordinates = data.coordinate // 이미 List<List<Double>> 형태로 되어 있음
                        val correlation = data.correlation

                        // 차트를 업데이트
                        updateEmotionConsumptionChart(coordinates)

                        // 상관계수에 따라 코멘트 설정
                        val comment = when {
                            correlation >= 0.5 -> {
                                "분석 결과, 일기에서 긍정적인 감정을 많이 표현할 때 \n" +
                                        " 소비가 증가하는 경향이 나타납니다.\n" +
                                        "당신은 기분이 좋을 때 스스로에게 보상하는 \n"+
                                        "소비 패턴을 보일 수 있습니다.\n" +
                                        "이 패턴을 인식하고, 기분이 좋을 때 꼭 필요한 소비인지 \n"+
                                        "한 번 더 생각해보는 것도 좋습니다.\n" +
                                        "자신의 감정을 보상하는 다른 방법(예: 취미 활동)을\n "+
                                        "찾아보는 것도 추천드립니다."
                            }
                            correlation <= -0.5 -> {
                                "분석 결과, 일기에서 부정적인 감정이 많이 표현될 때\n"+
                                        "소비가 증가하는 경향이 있습니다.\n" +
                                        "스트레스나 불안감이 클 때 충동적인 소비를 하거나,\n" +
                                        "기분을 달래기 위한 소비 패턴을 보일 수 있습니다.\n" +
                                        "이 패턴을 인식하고, 스트레스를 해소하는 \n" +
                                        "다른 방법(예: 운동, 취미 활동)을 찾아보는 것이 좋습니다.\n" +
                                        "부정적인 감정에 휩쓸리지 않도록\n " +
                                        "소비 계획을 세워 두는 것도 추천드립니다."
                            }
                            else -> {
                                "분석 결과, 일기에서 긍정적인 감정을 표현하는 빈도와 \n"+
                                        "소비 금액 간에 뚜렷한 상관관계는 발견되지 않았습니다.\n\n" +
                                        "이는 감정에 관계없이 계획적인 소비를 하고 있을 가능성이 \n높습니다. " +
                                        "감정에 의한 소비보다는 생활 패턴이나 \n"+
                                        "경제적 목표에 더 큰 영향을 받을 수 있습니다.\n" +
                                        "자신의 지출 패턴을 꾸준히 관찰하고, "+
                                        "소비 습관을\n점검하면서 재정 계획을 세워보세요."
                            }
                        }

                        // 최종 결과 설정
                        val resultText = "$comment"

                        // TextView에 결과 설정
                        emotion_result.text = resultText

                    } ?: run {
                        Log.e("응답 오류", "응답 본문이 null입니다.")
                    }
                } else {
                    Log.e("sentiment API 오류", "응답 실패: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<ResponseSentiment>, t: Throwable) {
                Log.e("sentiment API 호출 실패", t.message.toString())
            }
        })
    }


    // 감정 소비 분석 차트 업데이트
    private fun updateEmotionConsumptionChart(coordinates: List<List<Double>>) {
        val entries = ArrayList<Entry>()

        // 데이터 포인트 추가
        for (point in coordinates) {
            // 포인트의 길이가 2인지 확인
            if (point.size == 2) {
                val xValue = point[0].toFloat()  // 감정 분석 결과 (x)
                val yValue = point[1].toFloat()  // 금액 (y)
                entries.add(Entry(xValue, yValue))
            } else {
                Log.e("Chart Error", "Invalid point size: ${point.size}")
            }
        }

        // entries가 비어있는지 확인
        if (entries.isEmpty()) {
            Log.e("Chart Error", "No valid entries to display on the chart.")
            // 여기에 차트를 업데이트하지 않도록 return
            return
        }

        val dataSet = ScatterDataSet(entries, "감정 소비 분석")
        dataSet.color = ColorTemplate.COLORFUL_COLORS[0]
        dataSet.setScatterShape(ScatterChart.ScatterShape.CIRCLE)
        dataSet.scatterShapeSize = 10f

        val scatterData = ScatterData(dataSet)
        diary_chat1.data = scatterData
        diary_chat1.invalidate() // 차트 업데이트
    }


}