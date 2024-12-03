package com.example.financiallog

import FollowerAdapter
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
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
import android.provider.MediaStore
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.Manifest
import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Environment
import androidx.core.content.FileProvider
import com.google.gson.Gson
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.Locale


class ExpendAct : AppCompatActivity() {

    lateinit var btn_exsave : Button; lateinit var income_btn : Button; lateinit var expend_btn : Button; lateinit var x_btn :ImageButton;
    lateinit var pay_tv : TextView; lateinit var categ_tv:TextView; lateinit var shop_name :TextView; lateinit var toget_tv :TextView;
    lateinit var ed_pay:EditText; lateinit var ed_shop:EditText; lateinit var ed_toget:EditText; lateinit var tv_exsat:TextView;
    lateinit var alone_chip: CheckBox; lateinit var seek_bar:SeekBar; lateinit var seek_zero : TextView;private val REQUEST_CODE_PERMISSIONS = 1001
    val REQUEST_STORAGE_PERMISSION = 1001
    lateinit var seek_per :TextView; val apiobject : ApiObject by lazy { ApiObject() }; val PICK_IMAGE_REQUEST = 1002
    lateinit var textView:TextView; lateinit var group_expend : ChipGroup; private var currentPhotoPath: String? = null;
    //    var followers = listOf("User1", "User2", "User3", "User4");
    var followers: MutableList<String> = mutableListOf()
    var together = ArrayList<String>()
    //var adapter = FollowerAdapter(this, android.R.layout.simple_list_item_1, followers )

    lateinit var foodchip:Chip; lateinit var cultualchip:Chip; lateinit var taxchip:Chip; lateinit var livingchip:Chip;
    lateinit var educhip:Chip; lateinit var dueschip:Chip; lateinit var medicalchip:Chip; lateinit var shoppingchip:Chip;
    lateinit var trafficchip:Chip; lateinit var etcchip:Chip; lateinit var receiptbtn:Button;

    private lateinit var getImageLauncher: ActivityResultLauncher<Intent> ; private val REQUEST_IMAGE_CAPTURE = 101;
    private val REQUEST_CAMERA_PERMISSION = 101; private lateinit var imageUri: Uri

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

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_STORAGE_PERMISSION)
            }
        }

        // 영수증 버튼 클릭
        receiptbtn.setOnClickListener {
            // 갤러리 열기 인텐트 생성
            val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)

            // 카메라 인텐트 생성 및 저장할 파일 설정
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

            // 갤러리와 카메라 선택 창 생성
            val chooser = Intent.createChooser(galleryIntent, "Select Image")
            chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(cameraIntent))

            // 선택 창 실행
            startActivityForResult(chooser, PICK_IMAGE_REQUEST)
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
            input.put("user","3")
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
        })
    }

    // 권한 요청 결과 처리
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            REQUEST_CODE_PERMISSIONS -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    uploadImage(imageUri) // 권한이 허용된 경우, 이미지 업로드
                } else {
                    Toast.makeText(this, "권한이 필요합니다.", Toast.LENGTH_SHORT).show() // 권한 거부 처리
                }
            }
            REQUEST_CAMERA_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    uploadImage(imageUri)
                    Toast.makeText(this, "카메라 권한이 허용되었습니다.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "카메라 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
                }
            }
            // 필요한 경우 추가적인 권한 요청을 여기에 추가할 수 있습니다.
        }
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

    // onActivityResult 메서드에서 이미지 처리
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                PICK_IMAGE_REQUEST -> {
                    val isCamera = data == null || data.data == null // 카메라인지 확인
                    if (isCamera) {
                        // 카메라로 촬영한 이미지 처리
                        if (!currentPhotoPath.isNullOrEmpty()) {
                            val imageFile = File(currentPhotoPath)
                            uploadImage(Uri.fromFile(imageFile))
                        } else {
                            Log.e("Camera Error", "Current photo path is null or empty.")
                        }
                    } else {
                        // 갤러리에서 선택한 이미지 처리
                        val selectedImageUri = data?.data
                        if (selectedImageUri != null) {
                            // getFileFromUri 사용
                            val selectedFile = getFileFromUri(this, selectedImageUri)
                            if (selectedFile != null && selectedFile.exists()) {
                                uploadImage(Uri.fromFile(selectedFile)) // 파일의 URI로 업로드
                            } else {
                                Log.e("Gallery Error", "Failed to get File from URI: $selectedImageUri")
                            }
                        } else {
                            Log.e("Gallery Error", "Selected image URI is null.")
                        }
                    }
                }
            }
        } else {
            Log.e("Activity Result Error", "Result was not OK: $resultCode")
        }
    }

    // 이미지 파일 생성 메서드
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val imageFile = File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir)
        Log.d("ExpendAct", "Image file created: ${imageFile.absolutePath}")
        return imageFile
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
    private fun uploadImage(imageUri: Uri?) {
        Log.d("UploadImage", "uploadImage called with URI: $imageUri") // 로그 추가
        if (imageUri != null) {
            val filePath = if (imageUri.scheme == "file") {
                imageUri.path // file:// URI에서 경로 가져오기
            } else {
                getRealPathFromURI(imageUri) // content:// URI 처리
            }

            if (filePath != null) {
                val file = File(filePath)
                val compressedFile = compressImage(file) // 이미지 압축
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val date = dateFormat.format(Date())
                val requestFile = RequestBody.create("image/jpeg".toMediaTypeOrNull(), compressedFile)
                val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

                val userPart = RequestBody.create("text/plain".toMediaTypeOrNull(), "3") // 사용자 ID
                val datePart = RequestBody.create("text/plain".toMediaTypeOrNull(), date) // 현재 날짜

                Log.d("UploadImage", "Uploading image with user ID: 1 and date: $date") // 로그 추가

                apiobject.api.uploadImage(userPart, datePart, body)?.enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                        if (response.isSuccessful) {
                            val contentType = response.headers()["Content-Type"]
                            Log.d("Upload Data", "Response Content-Type: $contentType") // Content-Type 로그 추가
                            if (contentType != null && contentType.contains("application/json")) {
                                val responseBody = response.body()?.string()
                                Log.d("Upload Data", "Response data: $responseBody")
                                try {
                                    val jsonResponse = Gson().fromJson(responseBody, JsonResponse::class.java)
                                    ed_pay.setText(jsonResponse.amount) // EditText에 금액 설정
                                } catch (e: Exception) {
                                    Log.e("Parse Error", "Failed to parse JSON response", e)
                                }
                            } else {
                                Log.e("OCR Error_1", "Unexpected response type: $contentType")
                                Log.e("OCR Error_2", "Response Body: ${response.body()?.string()}")
                            }
                        } else {
                            val errorBody = response.errorBody()?.string()
                            Log.e("OCR Error_3", "Response not successful. Code: ${response.code()}, Body: $errorBody")
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Log.e("OCR Error_4", "Failed to upload image", t)
                    }
                })
            } else {
                Log.e("OCR Error_5", "Failed to get file path from URI")
            }
        } else {
            Log.e("OCR Error_6", "imageUri is null")
        }
    }


    // JSON 응답을 매핑할 데이터 클래스
    data class JsonResponse(
        val amount: String // 서버에서 반환하는 금액의 타입에 맞게 수정
    )

    // URI에서 실제 파일 경로를 가져오는 메서드
    private fun getRealPathFromURI(contentUri: Uri): String? {
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(contentUri, proj, null, null, null)
        return cursor?.use {
            val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            it.moveToFirst()
            it.getString(columnIndex)
        }
    }
    // 이미지 압축 메서드
    private fun compressImage(file: File): File {
        val bitmap: Bitmap? = BitmapFactory.decodeFile(file.absolutePath)

        if (bitmap == null) {
            throw IllegalArgumentException("Failed to decode file to Bitmap. File path: ${file.absolutePath}")
        }

        val compressedFile = File.createTempFile("compressed_", ".jpg", cacheDir)
        FileOutputStream(compressedFile).use { outputStream ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 60, outputStream)
        }
        return compressedFile
    }



//    // OCR 처리 메서드 초안
//    private fun uploadImageToCLOVA(bitmap: Bitmap) {
//        val baos = ByteArrayOutputStream()
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos)
//        val imageBytes = baos.toByteArray()
//        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
//        val date = dateFormat.format(Date())
//
//        // MultipartBody.Part로 변환
//        val userPart = RequestBody.create("text/plain".toMediaTypeOrNull(), "1")
//        val datePart = RequestBody.create("text/plain".toMediaTypeOrNull(), date)
//        val requestFile = RequestBody.create("image/jpeg".toMediaTypeOrNull(), imageBytes)
//        val body = MultipartBody.Part.createFormData("file", "image_${System.currentTimeMillis()}.jpg", requestFile) // "file"은 서버에서 요구하는 파라미터 이름
//
//        // API 호출
//        apiobject.api.uploadImage(userPart, datePart, body)!!.enqueue(object : Callback<ResponseBody> {
//            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
//                if (response.isSuccessful) {
//                    Log.d("Upload Data", "User: ${userPart}, Date: ${datePart}, File: ${requestFile}")
//
//                    response.body()?.let { responseBody ->
//                        handleOCRResponse(responseBody) // JSON 응답 처리 메서드 호출
//                        Log.e("data", "data: ${responseBody}")
//                    }
//                } else {
//                    Log.d("Upload Data", "User: ${userPart}, Date: ${datePart}, File: ${requestFile}")
//                    Log.d("Upload Data", "User: ${userPart}, Date: ${date}, File: ${imageBytes}")
//                    Log.e("OCR Error", "Response not successful: ${response.errorBody()?.string()}")
//                    Log.e("APIERROR", "Error: ${response.code()}")
//                    // 추가적인 에러 처리 로직
//                }
//            }
//
//            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
//                Log.e("OCR Error", "Failed to upload image", t)
//            }
//        })
//    }
//
//    // 클로바 OCR 응답을 처리하는 메서드
//    private fun handleOCRResponse(response: ResponseBody) {
//        try {
//            val jsonString = response.string()
//            val jsonObject = JSONObject(jsonString)
//
//            // 영수증 데이터 추출
//            val images = jsonObject.getJSONArray("images")
//            if (images.length() > 0) {
//                val fields = images.getJSONObject(0).getJSONArray("fields")
//                val extractedData = mutableMapOf<String, String>()
//
//                for (i in 0 until fields.length()) {
//                    val field = fields.getJSONObject(i)
//                    val fieldName = field.getString("name")
//                    val fieldValue = field.getString("inferText")
//
//                    // 필요한 필드만 저장 (예: 가격, 상호명, 날짜 등)
//                    when (fieldName) {
//                        "거래일자" -> extractedData["date"] = fieldValue
//                        "가게명" -> extractedData["shopName"] = fieldValue
//                        "총액" -> extractedData["amount"] = fieldValue
//                    }
//                }
//
//                // UI 업데이트 및 데이터 입력
//                updateUIWithExtractedData(extractedData)
//            }
//        } catch (e: Exception) {
//            Log.e("OCR Error", "Error parsing OCR response", e)
//        }
//    }
//
//    private fun updateUIWithExtractedData(data: Map<String, String>) {
//        // 예시: EditText에 데이터 자동 입력
//        val amountEditText: EditText = findViewById(R.id.money_ed)
//        amountEditText.setText(data["amount"])
//
//        // 필요한 경우 다른 UI 요소 업데이트
//        // val dateEditText: EditText = findViewById(R.id.date_edit_text)
//        // val shopNameEditText: EditText = findViewById(R.id.name_shop_ed)
//        // dateEditText.setText(data["date"])
//        // shopNameEditText.setText(data["shopName"])
//    }
//
//    // 카메라 인텐트 실행을 위한 메서드
//    private fun dispatchTakePictureIntent() {
//        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        if (takePictureIntent.resolveActivity(packageManager) != null) {
//            // 파일 생성
//            val photoFile: File? = createImageFile()
//            photoFile?.also {
//                currentPhotoPath = it.absolutePath
//                val photoURI: Uri = FileProvider.getUriForFile(this, "com.example.financiallog.fileprovider", it)
//                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
//                startActivityForResult(takePictureIntent, 101)
//            }
//        }
//    }
//
//    // 이미지 파일 생성 메서드
//    private fun createImageFile(): File {
//        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
//        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
//        val imageFile = File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir)
//        Log.d("expend", "Image file created: ${imageFile.absolutePath}")
//        return imageFile
//    }
}