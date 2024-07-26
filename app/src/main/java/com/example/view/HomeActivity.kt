package com.example.view

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.view.controller.loginController


class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)
        val textView = findViewById<TextView>(R.id.register)

        val spannableString = SpannableString("Don't have account? Register")

        val blackColorSpan = ForegroundColorSpan(Color.BLACK)
        val blueColorSpan = ForegroundColorSpan(Color.parseColor("#D75339"))
        spannableString.setSpan(blackColorSpan, 0, 19, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(blueColorSpan, 20, spannableString.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        val boldSpan = StyleSpan(Typeface.BOLD)
        spannableString.setSpan(boldSpan, 20, spannableString.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        textView.text = spannableString
        textView.setOnClickListener{
            val intent = Intent(this@HomeActivity, RegisterActivity::class.java);
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }

        val loginbtn = findViewById<Button>(R.id.signin_btn)
        loginbtn.setOnClickListener {
            val loading = findViewById<ProgressBar>(R.id.loading)
            loading.setVisibility(View.VISIBLE)
            val emailfield = findViewById<EditText>(R.id.email)
            loginbtn.setVisibility(View.GONE)
            val passfield = findViewById<EditText>(R.id.pass_login)
            val email = emailfield.text.toString()
            val pass = passfield.text.toString()
            if(email.length > 0) {
                if (email.contains("@") && email.contains(".")) {
                    if (pass.length > 0) {
                        loginController().Login(email, pass, this@HomeActivity, R.id.signin_btn, R.id.loading)
                    } else {
                        loginbtn.visibility = View.VISIBLE
                        loading.visibility = View.GONE
                        Toast.makeText(
                            this@HomeActivity,
                            "Please input password!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    loginbtn.visibility = View.VISIBLE
                    loading.visibility = View.GONE
                    Toast.makeText(this@HomeActivity, "Wrong email type!", Toast.LENGTH_SHORT)
                        .show()
                }
            }else{
                loginbtn.visibility = View.VISIBLE
                loading.visibility = View.GONE
                Toast.makeText(this@HomeActivity, "Please input email!", Toast.LENGTH_SHORT)
                    .show()
            }

        }

    }



}