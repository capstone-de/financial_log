package com.example.financiallog

import FollowerAdapter
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ListView
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import java.io.InputStream


class ExpendAct : AppCompatActivity() {

    lateinit var btn_exsave : Button; lateinit var income_btn : Button; lateinit var expend_btn : Button; lateinit var x_btn :ImageButton;
    lateinit var pay_tv : TextView; lateinit var categ_tv:TextView; lateinit var shop_name :TextView; lateinit var toget_tv :TextView;
    lateinit var ed_pay:EditText; lateinit var ed_shop:EditText; lateinit var ed_toget:EditText; lateinit var tv_exsat:TextView;
    lateinit var alone_chip: CheckBox; lateinit var seek_bar:SeekBar; lateinit var seek_zero : TextView;
    lateinit var seek_per :TextView; val apiobject : ApiObject by lazy { ApiObject() };
    lateinit var textView:TextView; lateinit var group_expend : ChipGroup;
//    var followers = listOf("User1", "User2", "User3", "User4");
    var followers: MutableList<String> = mutableListOf()
    var together = ArrayList<String>()
    //var adapter = FollowerAdapter(this, android.R.layout.simple_list_item_1, followers )

    lateinit var foodchip:Chip; lateinit var cultualchip:Chip; lateinit var taxchip:Chip; lateinit var livingchip:Chip;
    lateinit var educhip:Chip; lateinit var dueschip:Chip; lateinit var medicalchip:Chip; lateinit var shoppingchip:Chip;
    lateinit var trafficchip:Chip; lateinit var etcchip:Chip; lateinit var receiptbtn:Button;


    private lateinit var getImageLauncher: ActivityResultLauncher<Intent>

    @SuppressLint("MissingInflatedId")
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
        foodchip = findViewById<Chip>(R.id.food_ex)
        cultualchip = findViewById<Chip>(R.id.cultual)
        taxchip = findViewById<Chip>(R.id.tax_ex)
        livingchip = findViewById<Chip>(R.id.living)
        educhip = findViewById<Chip>(R.id.education)
        dueschip = findViewById<Chip>(R.id.dues)
        medicalchip = findViewById<Chip>(R.id.medical_ex)
        shoppingchip = findViewById<Chip>(R.id.shopping)
        trafficchip = findViewById<Chip>(R.id.traffic)
        etcchip = findViewById<Chip>(R.id.etc)
        seek_bar =findViewById<SeekBar>(R.id.seekBar)
        seek_zero = findViewById<TextView>(R.id.progressTv)
        seek_per =findViewById<TextView>(R.id.totalTv)
        textView = findViewById<TextView>(R.id.ex_satis_ed)
        val user = intent.getIntExtra("user",0)
        receiptbtn = findViewById<Button>(R.id.receipt_expend)


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

        // ActivityResultLauncher 초기화
        getImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data: Intent? = result.data
                val selectedImageUri: Uri? = data?.data
                if (selectedImageUri != null) {
                    // URI에서 InputStream 가져오기 및 비트맵 생성
                    contentResolver.openInputStream(selectedImageUri)?.use { inputStream ->
                        val bitmap: Bitmap = BitmapFactory.decodeStream(inputStream)
                        // OCR 처리 메서드 호출
                        uploadImageToCLOVA(bitmap) // 비트맵을 Clova OCR API로 업로드
                    }
                }
            }
        }

        // 영수증 버튼 클릭 리스너
        receiptbtn.setOnClickListener {
            // 갤러리에서 이미지 선택을 위한 인텐트
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            getImageLauncher.launch(intent)
        }


        //카테고리 선택 시
        var Chipchoose: String? = null
        group_expend.setOnCheckedStateChangeListener { group, checkedIds ->
            val selectedChip = group.checkedChipId
            when(selectedChip){
                R.id.food_ex -> {
                    Chipchoose = "food"
                    Toast.makeText(applicationContext, "식비", Toast.LENGTH_SHORT).show()
                }
                R.id.cultual -> {
                    Chipchoose = "cultural/living"
                    Toast.makeText(applicationContext, "문화생활", Toast.LENGTH_SHORT).show()
                }
                R.id.traffic -> {
                    Chipchoose = "tranportation/vehicle"
                    Toast.makeText(applicationContext, "교통/차량", Toast.LENGTH_SHORT).show()
                }
                R.id.tax_ex -> {
                    Chipchoose ="tax"
                    Toast.makeText(applicationContext, "세금", Toast.LENGTH_SHORT).show()
                }
                R.id.living -> {
                    Chipchoose = "housing/communication"
                    Toast.makeText(applicationContext, "주거/통신", Toast.LENGTH_SHORT).show()
                }
                R.id.education-> {
                    Chipchoose = "education"
                    Toast.makeText(applicationContext, "교육", Toast.LENGTH_SHORT).show()
                }
                R.id.dues -> {
                    Chipchoose = "personal event"
                    Toast.makeText(applicationContext, "경조사/회비", Toast.LENGTH_SHORT).show()
                }
                R.id.medical_ex -> {
                    Chipchoose = "medical"
                    Toast.makeText(applicationContext, "병원/약국", Toast.LENGTH_SHORT).show()
                }
                R.id.shopping -> {
                    Chipchoose = "shopping"
                    Toast.makeText(applicationContext, "쇼핑", Toast.LENGTH_SHORT).show()
                }
                R.id.etc -> {
                    Chipchoose = "etc"
                    Toast.makeText(applicationContext, "기타", Toast.LENGTH_SHORT).show()
                }
            }

        }

        //seekbar 선택 시
        seek_bar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {}
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {
                textView.setText(String.format(" %d ", seekBar.progress))
            }
        })

        // 혼자 선택
        alone_chip.setOnClickListener(){
            together.clear()
            ed_toget.setText("")
        }

        // 함께 하는 사람
        apiobject.api.getFollower().enqueue(object: Callback<List<String>> {
            override fun onResponse(
                call: Call<List<String>>,
                response: Response<List<String>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        followers.addAll(it) // 직접 리스트를 추가
                        Log.d("----------------", followers.toString())
                    }
                }
            }

            override fun onFailure(call: Call<List<String>>, t: Throwable) {
                Log.e("response", "Failed to load follower information", t)
                Toast.makeText(applicationContext, "Failed to retrieve follower information. Please check your network status.", Toast.LENGTH_LONG).show()
            }
        })


        ed_toget.setOnClickListener {
            showFollowersDialog()
        }


        //저장 버튼
        btn_exsave.setOnClickListener(View.OnClickListener {
            val Exmoney = ed_pay.text.toString()
            val dateFormat = SimpleDateFormat("yyyy-MM-dd")
            val date = dateFormat.format(Date())
            val Excate = Chipchoose
            val Exshop = ed_shop.text.toString()
            val ExTogether = together
            val Exsatis = textView.text.toString()

            var input = HashMap<String, Any>()
            input.put("user","6")
            input.put("price",Exmoney)
            input.put("date", date.toString())
            input.put("category", Excate.toString())
            input.put("bname", Exshop)
//            input.put("with_whom", together)
            if (ExTogether.isEmpty()){
                input.put("with_whom", together)
            }else{
                Log.d("together is not empty", together.toString())
                input.put("with_whom", together)
            }
            input.put("satisfaction", Exsatis)

            apiobject.api.insertEx(input)!!.enqueue(object : Callback<PostExpend> {
                override fun onResponse(
                    call: Call<PostExpend>,
                    response: retrofit2.Response<PostExpend>
                ) {
                    if(response.isSuccessful) {
                        var data = response.body() // GsonConverter를 사용해 데이터매핑
                        var intnet = Intent(applicationContext,SaveCheck::class.java)
                        startActivity(intnet)
                        Log.d("test", input.toString())
                    }
                }

                override fun onFailure(call: Call<PostExpend>, t: Throwable) {
                    Log.d("test", "지출 저장 실패$t")
                }

            })

            /*val responseListener: Response.Listener<String?> = Response.Listener<String?>{ response ->
                try{
                    val jsonObject = JSONObject(response)
                    val success = jsonObject.getBoolean("success")
                    if(success){
                        Toast.makeText(applicationContext,"저장되었습니다.", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, SaveCheck::class.java)
                        intent.putExtra("user", user)
                        startActivity(intent)
                    } else{
                        Toast.makeText(applicationContext, "저장되지 않았습니다.", Toast.LENGTH_SHORT).show()
                        return@Listener
                    }
                } catch (e: JSONException){
                    e.printStackTrace()
                    Toast.makeText(applicationContext,"예외 1", Toast.LENGTH_SHORT).show()
                    return@Listener
                }
            }
            val ExpendInsert = InsertRequestExpend(4,Exdate,Exmoney,Excate,Exshop,ExTogether,Exsatis, responseListener)
            val queue: RequestQueue = Volley.newRequestQueue(applicationContext)
            queue.add(ExpendInsert)*/

            /*val exadapter = ExpendAdapter()
             exadapter.addItem(ExpendAdapter.Exlist(Excate, Exshop, Exmoney, ExTogether,ExCheck,Exsatis))
             val intent = Intent(this, SaveCheck::class.java)
             intent.putExtra("user", user)
             startActivity(intent)*/

        })


    }
     fun showFollowersDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.followers_list)
        val listView: ListView = dialog.findViewById(R.id.listView)

        // ArrayAdapter를 사용해 ListView에 팔로워 목록 데이터 연결
        var adapter = FollowerAdapter(this, android.R.layout.simple_list_item_1, followers )
        listView.adapter = adapter

         listView.setOnItemClickListener { parent, view, position, id ->
             // 클릭된 아이템의 텍스트 가져오기
             val selectedFollower = followers[position]
             Toast.makeText(this, "Selected follower: $selectedFollower", Toast.LENGTH_SHORT).show()
             // 선택된 팔로워의 텍스트를 사용할 수 있도록 처리
 //            together.add(selectedFollower)
             Log.d("----selected together----", together.toString())
             // 선택된 팔로워 목록에 추가/제거
             if (together.contains(selectedFollower)) {
                 together.remove(selectedFollower)
             } else {
                 together.add(selectedFollower)
             }
             // 선택된 팔로워 목록 표시
             val selectedFollowersText = together.joinToString(", ")
             ed_toget.setText(selectedFollowersText)
 //            ed_toget.setText(selectedFollower) // Display the selected follower on the screen
             dialog.dismiss() // 다이얼로그 닫기
         }
        dialog.show()
    }


    // OCR 처리 메서드
    private fun uploadImageToCLOVA(bitmap: Bitmap) {
        // 클로바 OCR API 호출 코드
    }
}

