package com.example.view.ApiService

import com.example.view.datatype.getUserdata
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface getUserlist {
    @FormUrlEncoded
    @POST("getUser/listUser") // Đường dẫn API endpoint
    fun getUser(@Field("id") id: Int,@Field("token") token : String): Call<getUserdata>
}