package com.example.financiallog

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.VolleyLog.TAG
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class DiaryWriteAct : AppCompatActivity() {

    lateinit var dx_btn: ImageButton; lateinit var photo_btn :ImageButton;
    lateinit var diary_save : Button; lateinit var group_op:ChipGroup;
    lateinit var date_tv :TextView; lateinit var exdiary_tv :TextView;
    lateinit var re_expend: RecyclerView; lateinit var ed_diary : EditText;
    lateinit var tag_tv:TextView; lateinit var today : Date; lateinit var mFormat : SimpleDateFormat
    lateinit var tag_chip:Chip; lateinit var photo_tv:ImageView;
    lateinit var photo_tv1:ImageView; lateinit var photo_tv2:ImageView;
    lateinit var openchip:Chip; lateinit var privatechip:Chip;
    val apiobject : ApiObject by lazy { ApiObject() };

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.diary_write)

        dx_btn = findViewById<ImageButton>(R.id.imageButton3)
        photo_btn = findViewById<ImageButton>(R.id.camera_btn)
        diary_save = findViewById<Button>(R.id.save_btn)
        group_op =findViewById<ChipGroup>(R.id.diary_chipgroup)
        date_tv = findViewById<TextView>(R.id.date_View)
        exdiary_tv = findViewById<TextView>(R.id.today_diary)
        re_expend = findViewById<RecyclerView>(R.id.diary_re)
        ed_diary = findViewById<EditText>(R.id.diary_ed)
        openchip = findViewById<Chip>(R.id.open_chip)
        privatechip = findViewById<Chip>(R.id.private_chip)
        tag_tv = findViewById<TextView>(R.id.tag_view)
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
        mFormat = SimpleDateFormat("MM월 dd일 EE요일", Locale.KOREAN)
        date_tv.setText(getTime())


        // 지출 내역 불러오기


        // 공개 비공개 눌렀을 때
        var diarychip : String? = null
        if (openchip.isChecked){
            diarychip = openchip.text.toString()
        }else if (privatechip.isChecked){
            diarychip = privatechip.text.toString()
        }


        //해시태그 추가하기


        // 사진 버튼 눌렀을 때
        photo_btn.setOnClickListener(View.OnClickListener{
            val intent = Intent(Intent.ACTION_PICK)
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
            intent.setAction(Intent.ACTION_PICK);
            //launcher.launch(intent);

        })


        // 저장하기 버튼 눌렀을 때
        diary_save.setOnClickListener(View.OnClickListener {
            val Didate = Date()
            val Dicontent = ed_diary.toString()
            val Diprivacy = diarychip.toString()
            val Dihashtag = tag_chip.toString()
            val Difile = photo_tv

            var input = HashMap<String, String>()
            input.put("user_id", "4")
            input.put("date", Didate.toString())
            input.put("contents", Dicontent)
            input.put("privacy", Diprivacy)
            input.put("hastag", Dihashtag)
            input.put("file", Difile.toString())

            apiobject.api.insertDi(input)!!.enqueue(object : Callback<PostDiary>{
                override fun onResponse(call: Call<PostDiary>, response: Response<PostDiary>) {
                    if (response.isSuccessful) {
                        Log.d("test", response.body().toString())
                        var data = response.body() // GsonConverter를 사용해 데이터매핑
                        var intnet = Intent(applicationContext,SaveDiary::class.java)
                        startActivity(intnet)
                    }
                }

                override fun onFailure(call: Call<PostDiary>, t: Throwable) {
                    Log.d("test", "실패$t")
                }

            })


           // val intent = Intent(this, SaveDiary::class.java)
          //  startActivity(intent)
        })


    }

    private fun getTime(): String? {
        var mNow = System.currentTimeMillis()
        today = Date(mNow)
        return mFormat.format(today)
    }

}