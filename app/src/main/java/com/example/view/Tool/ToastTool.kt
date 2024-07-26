package com.example.view.Tool

import android.content.Context
import android.widget.Toast

class ToastTool(acti: Context, text: String) {
    init {
        Toast.makeText(acti, text, Toast.LENGTH_SHORT).show()
    }
}