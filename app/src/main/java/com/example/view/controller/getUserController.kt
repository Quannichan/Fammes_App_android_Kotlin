package com.example.view.controller

import android.app.Activity
import com.example.view.Network.Network
import com.example.view.Tool.ToastTool
import com.example.view.datatype.getUserdata
import com.example.view.userAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class getUserController {
    val network = Network().RetrofitTool()

    fun getUser(ID : Int, token : String, acti: Activity){
        val getMes = network.create(com.example.view.ApiService.getUserlist::class.java)
        getMes.getUser(ID, token).enqueue(object : Callback<getUserdata> {
            override fun onResponse(p0: Call<getUserdata>, response: Response<getUserdata>) {
                if(response.isSuccessful){
                    if(response != null){
                        val res = response.body()
                        if(res?.status == 2000){
                            userAdapter().bindingView(res.user, acti)
                        }else if(res?.status == 2001){
                            ToastTool(acti, "Cannot done!")
                        }
                        else if(res?.status == 2002){
                            ToastTool(acti, "Server error!")
                        }
                    }
                }
            }

            override fun onFailure(p0: Call<getUserdata>, p1: Throwable) {
                ToastTool(acti, "Server error!")
            }
        })
    }
}