package com.example.view.Tool

import android.app.Activity
import android.widget.LinearLayout

class LinearLayoutTool {

    fun getLayout(ID : Int , activity: Activity) : LinearLayout?{
        return activity.findViewById<LinearLayout>(ID)
    }
}