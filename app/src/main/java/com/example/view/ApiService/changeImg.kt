package com.example.view.ApiService

import com.example.view.datatype.changeInfo
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface changeImg {
    @FormUrlEncoded
    @POST("accSetting/changeIMG") // Đường dẫn API endpoint
    fun changeImg(@Field("id") id: Int, @Field("token") tok : String, @Field("email") email : String, @Field("pass") pass : String, @Field("default")default : Boolean ,@Field("newImg")file : String?): Call<changeInfo>
}