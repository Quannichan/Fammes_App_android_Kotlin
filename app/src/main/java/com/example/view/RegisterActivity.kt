package com.example.view

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.view.Tool.ButtonTool
import com.example.view.Tool.EditTextTool
import com.example.view.controller.registerController
import com.example.view.model.userModel

class RegisterActivity : AppCompatActivity(){
    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_activiy)
        val textView = findViewById<TextView>(R.id.register)
        val login_text = SpannableString("Already have an account? Sign in")
        val black = ForegroundColorSpan(Color.BLACK)
        val orange = ForegroundColorSpan(Color.parseColor("#D75339"))
        login_text.setSpan(black, 0, 24, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE )
        login_text.setSpan(orange, 25, login_text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        val bold = StyleSpan(Typeface.BOLD)
        login_text.setSpan(bold, 25, login_text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        textView.text = login_text

        textView.setOnClickListener{
            val intent = Intent(this@RegisterActivity, HomeActivity::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }
        val Re_ac : Activity = this@RegisterActivity
        val ETT = EditTextTool()
        val BT = ButtonTool()
        BT.getButton(R.id.regis_btn, Re_ac)!!.setOnClickListener {
            val registerController = registerController()
            registerController.register(
                userModel(0 , ETT.getText(R.id.usname, Re_ac), ETT.getText(R.id.email_regis, Re_ac), "",ETT.getText(R.id.pass_regis, Re_ac)),
                Re_ac,
                R.id.progressBar_regist,
                R.id.regis_btn)
        }

    }
}