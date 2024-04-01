package com.example.androidkotlindemo.activity

import com.example.androidkotlindemo.R
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.google.android.material.textfield.TextInputEditText

class LoginActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var btnRegister: Button
    private lateinit var btnSubmit: Button
    private lateinit var edtUser: TextInputEditText
    private lateinit var edtPassword: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        init()
    }

    private fun init() {
        edtUser = findViewById(R.id.edt_username)
        edtPassword = findViewById(R.id.edt_password)
        btnSubmit = findViewById(R.id.btn_submit)
        btnRegister = findViewById(R.id.btn_register)

        onClickData()
    }

    private fun onClickData(){
        btnSubmit.setOnClickListener(this)
        btnRegister.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btn_submit->{
                startActivity(Intent(this, DashboardActivity::class.java))
                finish()
            }
            R.id.btn_register -> {
                startActivity(Intent(this, RegisterActivity::class.java))
                finish()
            }
        }
    }

}