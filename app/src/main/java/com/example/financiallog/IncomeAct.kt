package com.example.financiallog

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.chip.ChipGroup
import retrofit2.Call
import retrofit2.Callback
import java.util.Date

class IncomeAct : AppCompatActivity() {

    lateinit var btn_x: ImageButton;
    lateinit var btn_incom: Button;
    lateinit var btn_expend: Button;
    lateinit var tv_pay: TextView;
    lateinit var ed_pay: EditText;
    lateinit var tv_cate: TextView;
    lateinit var tv_memo: TextView;
    lateinit var ed_memo: EditText;
    lateinit var group_income: ChipGroup;
    lateinit var btn_save: Button;
    lateinit var apiobject: ApiObject;
    lateinit var data: List<ExpendAdapter.Exlist>;

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.save_income)

        btn_x = findViewById<ImageButton>(R.id.imageButton1)
        btn_incom = findViewById<Button>(R.id.button)
        btn_expend = findViewById<Button>(R.id.button2)
        tv_pay = findViewById<TextView>(R.id.money)
        ed_pay = findViewById<EditText>(R.id.money_ed)
        tv_cate = findViewById<TextView>(R.id.category)
        tv_memo = findViewById<TextView>(R.id.memo)
        ed_memo = findViewById<EditText>(R.id.memo_ed)
        group_income = findViewById<ChipGroup>(R.id.income_group)
        btn_save = findViewById<Button>(R.id.save_expend)

        //카테고리 선택 시
        group_income.setOnCheckedStateChangeListener { group, checkedIds ->
            val selectedChip = group.checkedChipId
            when (selectedChip) {
                R.id.pay_in -> {
                    Toast.makeText(applicationContext, "월급", Toast.LENGTH_SHORT).show()
                }

                R.id.extra_in -> {
                    Toast.makeText(applicationContext, "부수입", Toast.LENGTH_SHORT).show()
                }

                R.id.financial_in -> {
                    Toast.makeText(applicationContext, "금융수익", Toast.LENGTH_SHORT).show()
                }

                R.id.etc_1 -> {
                    Toast.makeText(applicationContext, "기타", Toast.LENGTH_SHORT).show()
                }

            }

        }


        // 저장 버튼 시
        btn_save.setOnClickListener(View.OnClickListener {
            val Inmoney = ed_pay.textAlignment
            val Incul = Date()
            val Incate = tv_cate.toString()
            val Inmemo = ed_memo.toString()

            var input = HashMap<String, String>()
            input.put("user_id", "4")
            input.put("Inpay", Inmoney.toString())
            input.put("Indate", Incul.toString())
            input.put("Incate", Incate)
            input.put("Inmemo", Inmemo)

            apiobject.api.insertIn(input)!!.enqueue(object : Callback<PostIncome> {
                override fun onResponse(
                    call: Call<PostIncome>,
                    response: retrofit2.Response<PostIncome>
                ) {
                    if (response.isSuccessful) {
                        Log.d("test", response.body().toString())
                        var data = response.body() // GsonConverter를 사용해 데이터매핑
                        var intnet = Intent(applicationContext,SaveDiary::class.java)
                        startActivity(intnet)
                    }
                }

                override fun onFailure(call: Call<PostIncome>, t: Throwable) {
                    Log.d("test", "실패$t")
                }

                /*val responseListener: Response.Listener<String?> = Response.Listener<String?>{ response ->
                try{
                    val jsonObject = JSONObject(response)
                    val success = jsonObject.getBoolean("success")
                    if(success){
                        Toast.makeText(applicationContext,"저장되었습니다.", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, SaveCheck::class.java)
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
            val IncomeInsert = InsertRequestIncome(4,Incul,Inmoney,Incate,Inmemo, responseListener)
            val queue: RequestQueue = Volley.newRequestQueue(applicationContext)
            queue.add(IncomeInsert)

            val intent = Intent(this, SaveDiary::class.java)
            startActivity(intent)*/

            })


            // 수입 버튼
            btn_incom.setOnClickListener(View.OnClickListener {
                val intent = Intent(this, IncomeAct::class.java)
                startActivity(intent)
            })


            // 지출 버튼
            btn_expend.setOnClickListener(View.OnClickListener {
                val intent = Intent(this, ExpendAct::class.java)
                startActivity(intent)
            })

            // X 버튼
            btn_x.setOnClickListener(View.OnClickListener {
                val intent = Intent(this, HomeMain::class.java)
                startActivity(intent)
            })


        })
    }
}
