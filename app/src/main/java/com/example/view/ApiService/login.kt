package com.example.view.ApiService

import com.example.view.datatype.logindata
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface login {
    @FormUrlEncoded
    @POST("account/login") // Đường dẫn API endpoint
    fun loginUser(@Field("email") email : String, @Field("pass")pass : String): Call<logindata>
}