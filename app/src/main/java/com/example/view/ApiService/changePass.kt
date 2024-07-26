package com.example.view.ApiService

import com.example.view.datatype.changeInfo
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface changePass {
    @FormUrlEncoded
    @POST("accSetting/changePA") // Đường dẫn API endpoint
    fun changepass(@Field("id") id: Int, @Field("token") tok : String,@Field("email") email : String, @Field("pass") oldpass : String, @Field("newpass")username : String): Call<changeInfo>
}