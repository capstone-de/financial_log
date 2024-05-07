package com.example.financiallog

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
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
    lateinit var tag_chip:Chip; lateinit var photo_tv:ImageView; lateinit var ed_tag : EditText;
    lateinit var photo_tv1:ImageView; lateinit var photo_tv2:ImageView;
    lateinit var openchip:Chip; lateinit var privatechip:Chip; lateinit var tag_group:ChipGroup;
    val apiobject : ApiObject by lazy { ApiObject() }; val PICK_IMAGE_REQUEST = 1

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
        ed_tag = findViewById<EditText>(R.id.ed_tag)
        tag_group = findViewById<ChipGroup>(R.id.tag_group)
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
        group_op.setOnCheckedStateChangeListener { group, checkedIds ->
            val selectedChip = group.checkedChipId
            when(selectedChip){
                R.id.open_chip -> {
                    diarychip = openchip.text.toString()
                    Toast.makeText(applicationContext, "공개", Toast.LENGTH_SHORT).show()
                }
                R.id.private_chip -> {
                    diarychip = privatechip.text.toString()
                    Toast.makeText(applicationContext, "비공개", Toast.LENGTH_SHORT).show()
                }
            }
        }
        /*if (openchip.isChecked){
            diarychip = openchip.text.toString()
        }else if (privatechip.isChecked){
            diarychip = privatechip.text.toString()
        }*/


        //해시태그 추가하기
        ed_tag.setOnEditorActionListener{ _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                (event.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_ENTER)
            ) {
                val hashtag = ed_tag.text.toString().trim()
                if (hashtag.isNotEmpty()) {
                    addHashtag(hashtag)
                    ed_tag.text.clear()
                }
                return@setOnEditorActionListener true
            }
            false

        }

        // 사진 버튼 눌렀을 때
        photo_btn.setOnClickListener(View.OnClickListener{

            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_IMAGE_REQUEST)

           // val intent = Intent(Intent.ACTION_PICK)
            //intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
           // intent.setAction(Intent.ACTION_PICK);
            //launcher.launch(intent);

        })

        // 저장하기 버튼 눌렀을 때
        diary_save.setOnClickListener(View.OnClickListener {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd")
            val date = dateFormat.format(Date())
            val content = ed_diary.toString()
            val privacy = diarychip.toString()
            val hashtag = tag_group.toString()
            val Difile = photo_tv

            var input = HashMap<String, Any>()
            input.put("user_id", "4")
            input.put("date", date)
            input.put("contents", content)
            input.put("privacy", privacy)
            input.put("hastag", hashtag)
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

    private fun addHashtag(hashtag: String) {
        val chip = Chip(this)
        chip.text = hashtag
        chip.isCloseIconVisible = true
        chip.setOnCloseIconClickListener { tag_group.removeView(chip) }
        tag_group.addView(chip)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            val imageUri = data?.data
            // 이미지뷰에 이미지 설정
            photo_tv.setImageURI(imageUri)
        }
    }

    fun removeHashTag(chip: Chip) {
        tag_group.removeView(chip)
    }

    fun addHashTag(hashtag :String) {
        val tagText = ed_tag.text.toString().trim()
        if (tagText.isNotEmpty()) {
            val newChip = Chip(this)
            newChip.text = tagText
            newChip.isCloseIconVisible = true
            newChip.setOnCloseIconClickListener {
                // 해시태그 삭제
                removeHashTag(newChip)
            }
            tag_group.addView(newChip)
            ed_tag.text.clear() // 입력창 초기화
        }
    }

    private fun getTime(): String? {
        var mNow = System.currentTimeMillis()
        today = Date(mNow)
        return mFormat.format(today)
    }

}