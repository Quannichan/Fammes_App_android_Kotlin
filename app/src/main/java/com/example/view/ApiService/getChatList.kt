package com.example.view.ApiService

import com.example.view.datatype.dataChat
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface getChatList {
    @GET("messages/All") // Đường dẫn API endpoint
    fun getChatList(@Query("id")id : Int, @Query("token")token : String) : Call<dataChat>
}