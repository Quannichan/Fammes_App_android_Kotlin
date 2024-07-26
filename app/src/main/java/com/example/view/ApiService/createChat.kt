package com.example.view.ApiService

import com.example.view.datatype.createchatdata
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface createChat {
    @FormUrlEncoded
    @POST("getUser/createChat") // Đường dẫn API endpoint
    fun createchat(@Field("id") id: Int, @Field("name") name : String, @Field("img") img : String, @Field("idu") idu : Int, @Field("nameu") nameu : String, @Field("imgu")imgu : String, @Field("token") token : String) : Call<createchatdata>
}