package com.example.financiallog

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SignInAct:AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signin)

        val signin_tv = findViewById<TextView>(R.id.login_tv)
        val signin_id = findViewById<EditText>(R.id.ed_id)
        val signin_pass = findViewById<EditText>(R.id.ed_Password)
        val signin_btn = findViewById<Button>(R.id.login_btn)
        val signup_tv = findViewById<TextView>(R.id.signup_tv)
        val id_find = findViewById<TextView>(R.id.id_Tv)
        val password_find = findViewById<TextView>(R.id.password_Tv)
        val tv = findViewById<TextView>(R.id.textView9)

        //회원가입 클릭 시
        signup_tv.setOnClickListener(View.OnClickListener {
            val intent = Intent(this,SignUpAct::class.java)
            startActivity(intent)
        })


        //로그인 버튼 클릭 시
        signin_btn.setOnClickListener(View.OnClickListener {

            val intent = Intent(this,HomeMain::class.java)
            startActivity(intent)
        })

        //아이디 찾기 클릭 시

        //비밀번호 찾기 클릭 시

    }
}