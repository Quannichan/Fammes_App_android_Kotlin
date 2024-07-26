package com.example.view.controller

import android.app.Activity
import com.example.view.ApiService.checkConnection
import com.example.view.Network.Network
import com.example.view.datatype.logindata
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class checkConnectAPI {
    val Network  = Network().RetrofitTool()
    fun checkConnectAPI(activity: Activity){
        val check : checkConnection = Network.create(checkConnection::class.java)
        check.checkConnection().enqueue(object : Callback<logindata>{
            override fun onResponse(call: Call<logindata>, response: Response<logindata>) {
                if(response.isSuccessful){
                    val res = response.body()
                    if (res != null) {
                        if(res.status == 2000){
                            loginController().nonLogIn(activity)
                        }else{
                            loginController().cannotConnect(activity)
                        }
                    }
                }else{
                    loginController().cannotConnect(activity)
                }
            }

            override fun onFailure(call: Call<logindata>, t: Throwable) {
                loginController().cannotConnect(activity)
            }

        })
    }
}