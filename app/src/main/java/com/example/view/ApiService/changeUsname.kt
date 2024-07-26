package com.example.view.ApiService

import com.example.view.datatype.changeInfo
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface changeUsname {
    @FormUrlEncoded
    @POST("accSetting/changeUN") // Đường dẫn API endpoint
    fun changeUsername(@Field("id") id: Int , @Field("token") tok : String , @Field("email") email : String, @Field("pass") pass : String, @Field("usname")username : String): Call<changeInfo>
}