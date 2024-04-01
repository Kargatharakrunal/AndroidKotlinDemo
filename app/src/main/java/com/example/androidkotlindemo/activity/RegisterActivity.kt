package com.example.androidkotlindemo.activity

import com.example.androidkotlindemo.R
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        init()
    }

    private fun init(){
        val btnSubmit=findViewById<Button>(R.id.btn_submit)
        btnSubmit.setOnClickListener {
            startActivity(Intent(this,DashboardActivity::class.java))
            finish()
        }
    }
}