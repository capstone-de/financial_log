package com.example.financiallog

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaScannerConnection
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
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
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
    val list_ex : ApiObject by lazy { ApiObject() }; lateinit var ed_loc: EditText;
    private var currentPhotoPath: String? = null;  private lateinit var text_location: TextView
    var hashtaglist = ArrayList<String>()
    private val REQUEST_CAMERA_PERMISSION = 101
    private val districts = arrayOf(
        "종로구", "중구", "용산구", "성동구", "광진구",
        "동대문구", "중랑구", "성북구", "강북구", "도봉구",
        "노원구", "은평구", "서대문구", "마포구", "양천구",
        "강서구", "구로구", "금천구", "영등포구", "동작구",
        "관악구", "서초구", "강남구", "송파구", "강동구"
    )

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
        list_ex.api.getExpendAll(3,getCurrentFormattedDate()).enqueue(object : Callback<ResponseExpend> {
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

        // 위치 추가하기
        text_location = findViewById(R.id.text_location)
        ed_loc = findViewById(R.id.ed_loc)
        ed_loc.setOnClickListener {
            showDistrictDialog()
        }


        // 카메라 권한 요청
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION)
        }

        // 사진 버튼 눌렀을 때
        photo_btn.setOnClickListener {
            // 갤러리 열기
            val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)

            // 카메라 열기
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            val photoFile: File? = createImageFile() // 파일 생성
            if (photoFile != null) {
                currentPhotoPath = photoFile.absolutePath
                val photoURI: Uri = FileProvider.getUriForFile(
                    this,
                    "com.example.financiallog.fileprovider",
                    photoFile
                )
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI) // 파일 저장 URI 전달
            }

            // 두 가지 Intent 실행
            val chooser = Intent.createChooser(galleryIntent, "Select Image")
            chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(cameraIntent))
            startActivityForResult(chooser, PICK_IMAGE_REQUEST)
        }

        diary_save.setOnClickListener {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd")
            val date = dateFormat.format(Date())
            val content = ed_diary.text.toString()
            val privacy = diarychip.toString()
            // List<String> 형태로 변환
            val hashtags = hashtaglist.joinToString(", ") // 리스트를 문자열로 변환
            val gu = ed_loc.text.toString() // TextView의 텍스트를 location에 저장

            // RequestBody 생성
            val userPart = RequestBody.create("text/plain".toMediaTypeOrNull(), "3")
            val datePart = RequestBody.create("text/plain".toMediaTypeOrNull(), date)
            val contentsPart = RequestBody.create("text/plain".toMediaTypeOrNull(), content)
            val privacyPart = RequestBody.create("text/plain".toMediaTypeOrNull(), privacy)
            val hashtagsPart = RequestBody.create("text/plain".toMediaTypeOrNull(), hashtags) // RequestBody로 생성
            val guPart = RequestBody.create("text/plain".toMediaTypeOrNull(), gu)


            // 이미지 URI를 Bitmap으로 변환
            val imageUris = listOfNotNull(photo_tv.tag as? Uri, photo_tv1.tag as? Uri, photo_tv2.tag as? Uri)

            // 파일들을 전송하기 위한 리스트
            val files = imageUris.mapNotNull { uri ->
                val bitmap = uriToBitmap(uri)
                bitmap?.let {
                    // 비트맵을 압축하여 파일로 변환
                    val requestFile = createPartFromBitmap(it, "image")
                    MultipartBody.Part.createFormData("image", "image_${System.currentTimeMillis()}.jpg", requestFile) // MultipartBody.Part 생성
                }
            }

            // API 호출
            apiobject.api.insertDi(userPart, datePart, contentsPart, privacyPart, hashtagsPart, files, guPart)!!.enqueue(object : Callback<PostDiary> {
                override fun onResponse(call: Call<PostDiary>, response: Response<PostDiary>) {
                    if (response.isSuccessful) {
                        Log.d("test", response.body().toString())
                        val intent = Intent(applicationContext, SaveDiary::class.java)
                        startActivity(intent)
                    } else {
                        // 에러 처리
                        Log.e("API_ERROR", "Error: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<PostDiary>, t: Throwable) {
                    Log.d("test", "실패$t")
                }
            })
        }

    }
    private fun showDistrictDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("서울시 구 선택")
            .setItems(districts) { dialog, which ->
                // 선택한 구를 EditText에 설정
                ed_loc.setText(districts[which])
            }
        builder.create().show()
    }

    private fun addHashtag(hashtag: String) {
        val chip = Chip(this)
        chip.text = hashtag
        chip.isCloseIconVisible = true
        chip.setOnCloseIconClickListener { tag_group.removeView(chip) }
        tag_group.addView(chip)

    }

    // 사진 촬영 후 ImageView에 설정하기 위한 메서드
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                PICK_IMAGE_REQUEST -> {
                    val isCamera = data == null || data.data == null // 카메라인지 확인
                    if (isCamera) {
                        if (!currentPhotoPath.isNullOrEmpty()) {
                            val imageFile = File(currentPhotoPath!!)
                            // 카메라 촬영 후 이미지 뷰에 표시
                            val imageBitmap = decodeSampledBitmapFromFile(currentPhotoPath!!, photo_tv.width, photo_tv.height)
                            if (imageBitmap != null) {
                                photo_tv.setImageBitmap(imageBitmap)
                                photo_tv.tag = Uri.fromFile(imageFile)

                                // 갤러리에 이미지 추가
                                MediaScannerConnection.scanFile(this, arrayOf(currentPhotoPath), null) { path, uri ->
                                    Log.d("DiaryWriteAct", "Image scanned into gallery: $path")
                                }
                            } else {
                                Log.e("DiaryWriteAct", "Image bitmap is null")
                            }
                        } else {
                            Log.e("Camera Error", "Current photo path is null or empty.")
                        }
                    } else {
                        // 갤러리에서 이미지를 가져온 경우
                        if (data?.clipData != null) {
                            val count = data.clipData!!.itemCount
                            for (i in 0 until count) {
                                if (i < 3) { // 최대 3장까지만 처리
                                    val imageUri = data.clipData!!.getItemAt(i).uri
                                    when (i) {
                                        0 -> {
                                            val bitmap = decodeSampledBitmapFromUri(imageUri, photo_tv.width, photo_tv.height)
                                            photo_tv.setImageBitmap(bitmap)
                                            photo_tv.tag = imageUri
                                        }
                                        1 -> {
                                            val bitmap = decodeSampledBitmapFromUri(imageUri, photo_tv1.width, photo_tv1.height)
                                            photo_tv1.setImageBitmap(bitmap)
                                            photo_tv1.tag = imageUri
                                        }
                                        2 -> {
                                            val bitmap = decodeSampledBitmapFromUri(imageUri, photo_tv2.width, photo_tv2.height)
                                            photo_tv2.setImageBitmap(bitmap)
                                            photo_tv2.tag = imageUri
                                        }
                                    }
                                } else {
                                    Toast.makeText(this, "최대 3장까지만 선택할 수 있습니다.", Toast.LENGTH_SHORT).show()
                                }
                            }
                        } else if (data != null) {
                            // 단일 이미지 선택
                            val selectedImageUri = data.data
                            selectedImageUri?.let {
                                val bitmap = decodeSampledBitmapFromUri(it, photo_tv.width, photo_tv.height)
                                photo_tv.setImageBitmap(bitmap)
                                photo_tv.tag = it
                            }
                        }
                    }
                }
            }
        } else {
            Log.e("Activity Result Error", "Result was not OK: $resultCode")
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

    // URI를 Bitmap으로 변환하는 메서드
    private fun uriToBitmap(uri: Uri): Bitmap? {
        return try {
            val inputStream = contentResolver.openInputStream(uri)
            BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    // Bitmap을 RequestBody로 변환하는 메서드
    private fun createPartFromBitmap(bitmap: Bitmap, partName: String): RequestBody {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 60, byteArrayOutputStream)
        val requestFile = RequestBody.create(
            "image/jpeg".toMediaTypeOrNull(),
            byteArrayOutputStream.toByteArray()
        )
        return requestFile
    }
    // Bitmap을 파일로 변환하는 메서드
    private fun createFileFromBitmap(bitmap: Bitmap): File {
        val file = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "${System.currentTimeMillis()}.jpg")
        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, out)
        }
        return file
    }

    // 카메라로 촬영한 이미지 파일을 비트맵으로 변환하여 ImageView 크기에 맞게 조정하는 함수
    private fun decodeSampledBitmapFromFile(filePath: String, reqWidth: Int, reqHeight: Int): Bitmap? {
        // 첫 번째 디코딩을 통해 이미지 사이즈만 가져오기
        val options = BitmapFactory.Options().apply {
            inJustDecodeBounds = true
        }
        BitmapFactory.decodeFile(filePath, options)

        // 적절한 inSampleSize 계산
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)

        // inSampleSize 설정 후 실제 디코딩
        options.inJustDecodeBounds = false
        return BitmapFactory.decodeFile(filePath, options)
    }

    //갤러리 사진
    private fun decodeSampledBitmapFromUri(uri: Uri, reqWidth: Int, reqHeight: Int): Bitmap? {
        val options = BitmapFactory.Options().apply {
            inJustDecodeBounds = true
        }
        BitmapFactory.decodeStream(contentResolver.openInputStream(uri), null, options)

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false
        return BitmapFactory.decodeStream(contentResolver.openInputStream(uri), null, options)
    }

    private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {
            val halfHeight = height / 2
            val halfWidth = width / 2

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }
    // 카메라 인텐트 실행을 위한 메서드
    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            // 파일 생성
            val photoFile: File? = createImageFile()
            photoFile?.also {
                currentPhotoPath = it.absolutePath
                val photoURI: Uri = FileProvider.getUriForFile(this, "com.example.financiallog.fileprovider", it)
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(takePictureIntent, 101)
            }
        }
    }

    // 이미지 파일 생성 메서드
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val imageFile = File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir)
        Log.d("DiaryWriteAct", "Image file created: ${imageFile.absolutePath}")
        return imageFile
    }

}