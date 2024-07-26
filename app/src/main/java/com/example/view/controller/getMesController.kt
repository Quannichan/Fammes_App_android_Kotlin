package com.example.view.controller

import android.app.Activity
import android.content.Intent
import android.view.View
import android.widget.ScrollView
import com.example.view.ApiService.getMessage
import com.example.view.HomeActivity
import com.example.view.Message_adapter
import com.example.view.Network.Network
import com.example.view.R
import com.example.view.Tool.ToastTool
import com.example.view.config.Common
import com.example.view.datatype.messagedata
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class getMesController {
    val network = Network().RetrofitTool()

    fun getMessage(ID : Int , token : String, id_chat : Int, ID_wrap_mes : Int , ID_scroll : Int ,acti: Activity, chat_img : String , chat_name : String){
        val getMes = network.create(getMessage::class.java)
        getMes.getMesssage(ID, token, id_chat).enqueue(object : Callback<messagedata>{
            override fun onResponse(call: Call<messagedata>, response: Response<messagedata>) {
                if(response.isSuccessful){
                    val res = response.body()
                    if (res != null) {
                        when(res.status){
                            Common().OK_CODE_SV->{
                                    Message_adapter().bindingView(res.messages, ID_wrap_mes,ID_scroll , acti,chat_img, chat_name)
                                    var scroll = acti.findViewById<ScrollView>(R.id.scroll_wrap_mes)
                                    scroll.fullScroll(View.FOCUS_DOWN)
                            }
                            Common().KO_CODE_SV->{
                                ToastTool(acti,"Server error!")
                                if(res.cause.equals("notlogin")){
                                    val intent = Intent(acti, HomeActivity::class.java)
                                    acti.startActivity(intent)
                                    acti.finish()
                                }
                            }
                            Common().ERROR_CODE_SV->{
                                ToastTool(acti,"Error")
                            }
                        }
                    }
                }else{
                    ToastTool(acti, "Error. something went wrong!")
                }
            }

            override fun onFailure(call: Call<messagedata>, t: Throwable) {

            }

        })
    }
}