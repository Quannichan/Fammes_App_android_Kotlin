package com.example.view.ApiService

import com.example.view.datatype.registerdata
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface register {
    @FormUrlEncoded
    @POST("account/register")
    fun registerUser(@Field("name")username : String, @Field("email")email : String, @Field("pass")pass : String ) : Call<registerdata>
}