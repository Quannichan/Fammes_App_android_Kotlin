package com.example.view.controller

import android.app.Activity
import android.content.Intent
import android.util.Log
import com.example.view.ApiService.getChatList
import com.example.view.Chat_adapter
import com.example.view.HomeActivity
import com.example.view.Network.Network
import com.example.view.Tool.ToastTool
import com.example.view.config.Common
import com.example.view.databinding.AppActivityBinding
import com.example.view.datatype.dataChat
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class chatController {
    val network = Network().RetrofitTool()
    fun getChat(ID : Int, token : String, acti : Activity, bind : AppActivityBinding){
        val getChat = network.create(getChatList::class.java)
        getChat.getChatList(ID, token).enqueue(object : Callback<dataChat>{
            override fun onResponse(call: Call<dataChat>, response: Response<dataChat>) {
                if(response.isSuccessful){
                    val res = response.body()
                    if(res != null){
                        when(res.status){
                            Common().OK_CODE_SV->{
                                if(res.chat.isNotEmpty()){
                                    Chat_adapter().bindingView(res.chat, bind,acti)
                                }
                            }
                            Common().KO_CODE_SV->{
                                if(res.cause.equals("notlogin")){
                                    val intent = Intent(acti, HomeActivity::class.java)
                                    acti.startActivity(intent)
                                    acti.finish()
                                }else{
                                    ToastTool(acti, "Something went wrong!")
                                }

                            }
                            Common().ERROR_CODE_SV->{
                                ToastTool(acti, "Error, cannot get chat!")
                            }
                        }
                    }else{
                        ToastTool(acti, "Error!")
                        Log.d("error", "response is null")
                    }
                }else{
                    Log.d("error", "response is not success")
                }
            }

            override fun onFailure(call: Call<dataChat>, t: Throwable) {
                ToastTool(acti, "Error!")
                Log.d("error", t.toString())
            }

        })
    }
}