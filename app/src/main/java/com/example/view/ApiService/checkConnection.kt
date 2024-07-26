package com.example.view.ApiService

import com.example.view.datatype.logindata
import retrofit2.Call
import retrofit2.http.GET

interface checkConnection {
    @GET("check") // Đường dẫn API endpoint
    fun checkConnection(): Call<logindata>
}