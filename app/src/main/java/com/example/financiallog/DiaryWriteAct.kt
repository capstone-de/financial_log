package com.example.financiallog

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
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
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
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
    val apiobject : ApiObject by lazy { ApiObject() }; val PICK_IMAGE_REQUEST = 1002
    val list_ex : ApiObject by lazy { ApiObject() };
    private var currentPhotoPath: String? = null
    var hashtaglist = ArrayList<String>()

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
        re_expend.layoutManager = LinearLayoutManager(this)
        list_ex.api.getExpendAll(6,getCurrentFormattedDate()).enqueue(object : Callback<ResponseExpend> {
            override fun onResponse(
                call: Call<ResponseExpend>,
                response: Response<ResponseExpend>
            ) {
                if(response.isSuccessful){
                    val data = response.body()!!.expense
                    val expendadapter = ExpendAdapter(data)
                    re_expend.adapter = expendadapter
                    Toast.makeText(applicationContext, "성공", Toast.LENGTH_SHORT).show()

                }
            }
            override fun onFailure(call: Call<ResponseExpend>, t: Throwable) {
                Log.d("response", "실패$t")
                Toast.makeText(applicationContext, "정보를 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
            }

        })


        // 공개 비공개 눌렀을 때
        var diarychip : String? = null
        group_op.setOnCheckedStateChangeListener { group, checkedIds ->
            val selectedChip = group.checkedChipId
            when(selectedChip){
                R.id.open_chip -> {
                    diarychip = "1"
                    Toast.makeText(applicationContext, "공개", Toast.LENGTH_SHORT).show()
                }
                R.id.private_chip -> {
                    diarychip = "0"
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
                    hashtaglist.add(hashtag)
                    Log.d("hashtag add ", hashtaglist.toString())
                    ed_tag.text.clear()
                }
                return@setOnEditorActionListener true
            }
            false

        }

        // 사진 버튼 눌렀을 때
        photo_btn.setOnClickListener {
            // 갤러리 열기
            val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)

            // 카메라 열기
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

            // 두 가지 Intent 실행
            val chooser = Intent.createChooser(galleryIntent, "Select Image")
            chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(cameraIntent))
            startActivityForResult(chooser, PICK_IMAGE_REQUEST)
        }

        // 저장하기 버튼 눌렀을 때
        diary_save.setOnClickListener {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd")
            val date = dateFormat.format(Date())
            val content = ed_diary.text.toString()
            val privacy = diarychip
            val hashtag = hashtaglist

            val input = HashMap<String, Any>()
            input["user"] = "6"
            input["date"] = date.toString()
            input["contents"] = content
            input["privacy"] = privacy.toString()
            input["hashtag"] = hashtag

            val imageUris = listOfNotNull(photo_tv.tag as? Uri, photo_tv1.tag as? Uri, photo_tv2.tag as? Uri)
            val files = imageUris.mapNotNull { createPartFromFile(this, it, "files") }

            input["file"] = files

            apiobject.api.insertDi(input)!!.enqueue(object : Callback<PostDiary> {
                override fun onResponse(call: Call<PostDiary>, response: Response<PostDiary>) {
                    if (response.isSuccessful) {
                        Log.d("test", response.body().toString())
                        val intent = Intent(applicationContext, SaveDiary::class.java)
                        startActivity(intent)
                    }
                }

                override fun onFailure(call: Call<PostDiary>, t: Throwable) {
                    Log.d("test", "실패$t")
                }
            })
        }

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
            if (data?.clipData != null) {
                val count = data.clipData?.itemCount ?: 0
                for (i in 0 until count) {
                    val imageUri = data.clipData?.getItemAt(i)?.uri
                    when (i) {
                        0 -> {
                            photo_tv.setImageURI(imageUri)
                            photo_tv.tag = imageUri
                        }
                        1 -> {
                            photo_tv1.setImageURI(imageUri)
                            photo_tv1.tag = imageUri
                        }
                        2 -> {
                            photo_tv2.setImageURI(imageUri)
                            photo_tv2.tag = imageUri
                        }
                    }
                }
            } else {
                val imageUri = data?.data
                photo_tv.setImageURI(imageUri)
                photo_tv.tag = imageUri
            }
        } else if (requestCode == 101 && resultCode == Activity.RESULT_OK) {
            val imageBitmap = BitmapFactory.decodeFile(currentPhotoPath)
            photo_tv.setImageBitmap(imageBitmap)
            photo_tv.tag = Uri.fromFile(File(currentPhotoPath))
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
    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            val photoFile = createImageFile()
            currentPhotoPath = photoFile.absolutePath
            val photoURI = FileProvider.getUriForFile(this, "com.example.android.fileprovider", photoFile)
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            startActivityForResult(takePictureIntent, 101)
        }
    }
    private fun createImageFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir).apply {
            currentPhotoPath = absolutePath
        }
    }
    fun getCurrentFormattedDate(): String {
        val currentDate = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return currentDate.format(formatter)
    }

    fun getFileFromUri(context: Context, uri: Uri): File? {
        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = context.contentResolver.query(uri, filePathColumn, null, null, null)
        cursor?.moveToFirst()
        val columnIndex = cursor?.getColumnIndex(filePathColumn[0])
        val filePath = columnIndex?.let { cursor.getString(it) }
        cursor?.close()
        return if (filePath != null) File(filePath) else null
    }
    fun createPartFromFile(context: Context, uri: Uri, partName: String): MultipartBody.Part? {
        val file = getFileFromUri(context, uri)
        return file?.let {
            val mediaType = context.contentResolver.getType(uri)?.toMediaTypeOrNull() ?: "image/*".toMediaTypeOrNull()
            val requestFile = RequestBody.create(mediaType, it)
            MultipartBody.Part.createFormData(partName, it.name, requestFile)
        }
    }

}