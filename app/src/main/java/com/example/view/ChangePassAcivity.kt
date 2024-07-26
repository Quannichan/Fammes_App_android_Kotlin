package com.example.view

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.view.Tool.ButtonTool
import com.example.view.Tool.EditTextTool
import com.example.view.Tool.ToastTool
import com.example.view.controller.ChangePassController

class ChangePassAcivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.change_pass_activity)
        ButtonTool().getButton(R.id.acpt_changepass,this)?.setOnClickListener {
            val pass = EditTextTool().getText(R.id.oldPass, this)
            val newpass = EditTextTool().getText(R.id.newPass, this)
            if(pass.length > 0 && newpass.length > 0){
                SendReqChange(pass, newpass)
            }else{
                ToastTool(this, "Please fill all field")
            }
        }

        ButtonTool().getImageButton(R.id.back_changepass, this)?.setOnClickListener {
            finish()
        }
    }

    fun SendReqChange(pass : String, newpass : String){
        val sharedPreferences: SharedPreferences = getSharedPreferences("privateData", Context.MODE_PRIVATE)
        val id = sharedPreferences.getInt("IDU", 0)
        val tok = sharedPreferences.getString("TOK", null)
        val email = sharedPreferences.getString("EMAIL", null)
        ChangePassController().sendChangeReq(id,tok.toString() ,email.toString() ,pass, newpass, this)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

}