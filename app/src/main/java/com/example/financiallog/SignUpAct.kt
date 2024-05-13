package com.example.financiallog

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SignUpAct : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup)

        val name_tv = findViewById<TextView>(R.id.name_tv)
        val name_ed =findViewById<EditText>(R.id.name_ed)
        val id_tv = findViewById<TextView>(R.id.siginup_id)
        val id_ed = findViewById<EditText>(R.id.ed_signupid)
        val password_tv = findViewById<TextView>(R.id.siginup_password)
        val password_ed = findViewById<EditText>(R.id.signup_edpassword)
        val passcheck_tv = findViewById<TextView>(R.id.passcheck)
        val passcheck_ed = findViewById<EditText>(R.id.signup_passwordcheck)
        val nickname_tv = findViewById<TextView>(R.id.nickname_signup)
        val nickname_ed = findViewById<EditText>(R.id.signup_nicknameed)
        val check_id  =findViewById<Button>(R.id.check_id)
        val check_nickname= findViewById<Button>(R.id.check_nickname)
        val signup_btn = findViewById<Button>(R.id.signup_btn)

        //아이디 중복확인

        //닉네임 중복확인


        //가입버튼 클릭 시
        signup_btn.setOnClickListener(View.OnClickListener {

            val intent = Intent(this, SignInAct::class.java)
            startActivity(intent)
        })


    }
}