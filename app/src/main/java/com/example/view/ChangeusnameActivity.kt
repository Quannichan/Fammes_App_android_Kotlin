package com.example.view

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.view.Tool.ButtonTool
import com.example.view.Tool.EditTextTool
import com.example.view.Tool.ToastTool
import com.example.view.controller.ChangeUsnameController

class ChangeusnameActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.change_usname_activity)
        ButtonTool().getButton(R.id.change_usname_btn,this)?.setOnClickListener {
            val pass = EditTextTool().getText(R.id.enter_pass_changeus, this)
            val usname = EditTextTool().getText(R.id.new_usname, this)
            if(pass.length > 0 && usname.length > 0){
                SendReqChange(pass, usname)
            }else{
                ToastTool(this, "Please fill all field")
            }
        }

        ButtonTool().getImageButton(R.id.back_changeusname, this)?.setOnClickListener {
            finish()
        }
    }

    fun SendReqChange(pass : String, username : String){
        val sharedPreferences: SharedPreferences = getSharedPreferences("privateData", Context.MODE_PRIVATE)
        val id = sharedPreferences.getInt("IDU", 0)
        val tok = sharedPreferences.getString("TOK", null)
        val email = sharedPreferences.getString("EMAIL", null)
        ChangeUsnameController().sendChangeReq(id,tok.toString() ,email.toString() ,pass,username, this)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

}