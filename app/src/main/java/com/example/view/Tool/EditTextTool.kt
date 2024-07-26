package com.example.view.Tool

import android.app.Activity
import android.widget.EditText


class EditTextTool{
    fun getText(ID : Int, activity: Activity) : String{
         return activity.findViewById<EditText>(ID).text.toString()
    }
    fun getEditText(ID : Int, activity: Activity) : EditText{
        return activity.findViewById<EditText>(ID)
    }
}