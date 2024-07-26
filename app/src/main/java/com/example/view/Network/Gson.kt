package com.example.view.Network

import android.annotation.SuppressLint
import com.google.gson.Gson
import com.google.gson.GsonBuilder

@SuppressLint("NotConstructor")
class Gson{
    fun Gson() : Gson {
        val gson = GsonBuilder()
            .setLenient()
            .create()
        return gson
    }
}