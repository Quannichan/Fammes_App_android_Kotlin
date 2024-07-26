package com.example.view.controller

import android.app.Activity
import com.example.view.Network.Network
import com.example.view.Tool.ToastTool
import com.example.view.datatype.createchatdata
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class createChatController {
    val network = Network().RetrofitTool()

    fun create(ID : Int, name : String, img: String, idu : Int, nameu: String, imgu : String ,token : String, acti : Activity){
        val getMes = network.create(com.example.view.ApiService.createChat::class.java)
        getMes.createchat(ID, name, img, idu,nameu,imgu, token).enqueue(object : Callback<createchatdata> {
            override fun onResponse(p0: Call<createchatdata>, p1: Response<createchatdata>) {
                if(p1.isSuccessful){
                    val res = p1.body()
                    ToastTool(acti, "Create chat success!")

                }
            }

            override fun onFailure(p0: Call<createchatdata>, p1: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }
}