package com.example.view.Tool

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences

class shareDataTool {

    fun setData(activity: Activity, ID : Int, Name : String, Email: String , Img : String, token : String){
        val sharedPreferences: SharedPreferences = activity.getSharedPreferences("privateData", Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = sharedPreferences.edit()
        if(ID != 0){
            editor.putInt("IDU", ID)
        }
        if(Name.isNotEmpty()){
            editor.putString("NAME", Name)
        }
        if(Email.isNotEmpty()){
            editor.putString("EMAIL", Email)
        }
        if(Img.isNotEmpty()){
            editor.putString("IMG", Img)
        }
        if(token.isNotEmpty()){
            editor.putString("TOK", token)
        }
        editor.apply()
    }

    fun removeData(activity: Activity){
        val sharedPreferences: SharedPreferences = activity.getSharedPreferences("privateData", Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = sharedPreferences.edit()
        editor.clear().apply()
    }

}