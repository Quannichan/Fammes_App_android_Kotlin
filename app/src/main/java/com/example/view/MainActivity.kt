package com.example.view

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.view.controller.loginController

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        check()
    }

    fun check(){
        val sharedPreferences: SharedPreferences = getSharedPreferences("privateData", Context.MODE_PRIVATE)
        val id : Int = sharedPreferences.getInt("IDU", 0)
        val token : String? = sharedPreferences.getString("TOK", "null")
        loginController().checkLogin(id, token.toString(), this)
    }
}