package com.example.view.Network

import com.example.view.config.Common
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Network {
    private val comm = Common()
    private val gson = Gson()
    fun RetrofitTool() : Retrofit{
        val retrofit = Retrofit.Builder()
            .baseUrl(comm.BASE_URL_API)
            .addConverterFactory(GsonConverterFactory.create(gson.Gson()))
            .build()
        return retrofit
    }
}