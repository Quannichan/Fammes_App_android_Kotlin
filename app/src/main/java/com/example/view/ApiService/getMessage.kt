package com.example.view.ApiService

import com.example.view.datatype.messagedata
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface getMessage {
    @GET("messages/getMes")
    fun getMesssage(@Query("id")id : Int, @Query("token")token : String, @Query("iMess")id_mes : Int ) : Call<messagedata>
}