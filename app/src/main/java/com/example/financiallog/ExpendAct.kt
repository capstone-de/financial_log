package com.example.financiallog

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.chip.ChipGroup
import retrofit2.Call
import retrofit2.Callback
import java.util.Date

class ExpendAct : AppCompatActivity() {

    lateinit var btn_exsave : Button; lateinit var income_btn : Button;
    lateinit var expend_btn : Button; lateinit var x_btn :ImageButton;
    lateinit var pay_tv : TextView; lateinit var categ_tv:TextView;
    lateinit var shop_name :TextView; lateinit var toget_tv :TextView;
    lateinit var ed_pay:EditText; lateinit var ed_shop:EditText;
    lateinit var ed_toget:EditText; lateinit var tv_exsat:TextView;
    lateinit var alone_chip: CheckBox; lateinit var group_expend:ChipGroup;
    lateinit var seek_bar:SeekBar; lateinit var seek_zero : TextView;
    lateinit var seek_per :TextView; lateinit var apiobject : ApiObject;
    lateinit var data : List<ExpendAdapter.Exlist>;

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
        seek_bar =findViewById<SeekBar>(R.id.seekBar)
        seek_zero = findViewById<TextView>(R.id.progressTv)
        seek_per =findViewById<TextView>(R.id.totalTv)
        val user = intent.getIntExtra("user",0)


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

        //카테고리 선택 시
        group_expend.setOnCheckedStateChangeListener { group, checkedIds ->
            val selectedChip = group.checkedChipId
            when(selectedChip){
                R.id.food_ex -> {
                    Toast.makeText(applicationContext, "식비", Toast.LENGTH_SHORT).show()
                }
                R.id.cultual -> {
                    Toast.makeText(applicationContext, "문화생활", Toast.LENGTH_SHORT).show()
                }
                R.id.traffic -> {
                    Toast.makeText(applicationContext, "교통/차량", Toast.LENGTH_SHORT).show()
                }
                R.id.tax_ex -> {
                    Toast.makeText(applicationContext, "세금", Toast.LENGTH_SHORT).show()
                }
                R.id.living -> {
                    Toast.makeText(applicationContext, "주거/통신", Toast.LENGTH_SHORT).show()
                }
                R.id.education-> {
                    Toast.makeText(applicationContext, "교육", Toast.LENGTH_SHORT).show()
                }
                R.id.dues -> {
                    Toast.makeText(applicationContext, "경조사/회비", Toast.LENGTH_SHORT).show()
                }
                R.id.medical_ex -> {
                    Toast.makeText(applicationContext, "병원/약국", Toast.LENGTH_SHORT).show()
                }
                R.id.shopping -> {
                    Toast.makeText(applicationContext, "쇼핑", Toast.LENGTH_SHORT).show()
                }
                R.id.etc -> {
                    Toast.makeText(applicationContext, "기타", Toast.LENGTH_SHORT).show()
                }
            }

        }


        //seekbar 선택 시


        //저장 버튼
        btn_exsave.setOnClickListener(View.OnClickListener {
            val Exmoney = ed_pay.textAlignment
            val Exdate = Date()
            val Excate = group_expend.toString()
            val Exshop = ed_shop.toString()
            val ExTogether = ed_toget.toString()
            val ExCheck = alone_chip.toString()
            val Exsatis = seek_bar.toString()
            var input = HashMap<String, String>()
            input.put("user_id","4")
            input.put("Expay",Exmoney.toString())
            input.put("Exdate", Exdate.toString())
            input.put("Excate", Excate)
            input.put("Exshop", Exshop)
            input.put("ExTogether", ExTogether)
            input.put("Exsatis", Exsatis)

            apiobject.api.insertEx(input)!!.enqueue(object : Callback<PostExpend> {
                override fun onResponse(
                    call: Call<PostExpend>,
                    response: retrofit2.Response<PostExpend>
                ) {
                    if(response.isSuccessful) {
                        Log.d("test", response.body().toString())
                        var intnet = Intent(applicationContext,SaveCheck::class.java)
                        startActivity(intnet)
                        var data = response.body() // GsonConverter를 사용해 데이터매핑
                    }
                }

                override fun onFailure(call: Call<PostExpend>, t: Throwable) {
                    Log.d("test", "실패$t")
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
}