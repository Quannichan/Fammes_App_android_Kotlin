package com.example.view.Tool

import android.app.Activity
import android.widget.Button
import android.widget.ImageButton

class ButtonTool {

    fun getImageButton(ID : Int , activity: Activity) : ImageButton?{
        return activity.findViewById<ImageButton>(ID)
    }

    fun getButton(ID : Int , activity: Activity) : Button?{
        return activity.findViewById<Button>(ID)
    }

    fun getTextBtn(ID : Int , activity: Activity) : String{
        return activity.findViewById<Button>(ID)?.text.toString()
    }
}