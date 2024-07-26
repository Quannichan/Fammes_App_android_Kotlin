package com.example.view.Tool

import android.app.Activity
import android.widget.ProgressBar

class LoaderTool {
    fun getLoader(loader_id : Int, activity: Activity) : ProgressBar{
        return activity.findViewById<ProgressBar>(loader_id)
    }
}