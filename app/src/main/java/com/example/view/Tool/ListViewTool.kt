package com.example.view.Tool

import android.app.Activity
import android.widget.ListView

class ListViewTool {
    fun getLV(activity : Activity, id : Int) : ListView{
        return activity.findViewById<ListView>(id)
    }
}