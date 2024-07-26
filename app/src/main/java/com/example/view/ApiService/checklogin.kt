package com.example.view.ApiService

import com.example.view.datatype.logindata
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface checklogin {
    @FormUrlEncoded
    @POST("account/islogin") // Đường dẫn API endpoint
    fun checkloginUser(@Field("id")id : Int, @Field("token")token : String) : Call<logindata>
}