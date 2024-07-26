package com.example.view.controller

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.view.ApiService.changeUsname
import com.example.view.Network.Network
import com.example.view.Tool.ToastTool
import com.example.view.config.Common
import com.example.view.datatype.changeInfo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChangeUsnameController {
    val network = Network().RetrofitTool()
    fun sendChangeReq(id : Int, tok : String, email : String, p : String , un : String, acti : Activity){
        val changeus = network.create(changeUsname::class.java)
        changeus.changeUsername(id,tok,email,p,un).enqueue(object : Callback<changeInfo>{
            override fun onResponse(p0: Call<changeInfo>, response: Response<changeInfo>) {
                if(response.isSuccessful){
                    val res = response.body()
                    if(res != null){
                        when(res.status){
                            Common().OK_CODE_SV -> {
                                val sharedPreferences: SharedPreferences = acti.getSharedPreferences("privateData", Context.MODE_PRIVATE)
                                val editor : SharedPreferences.Editor = sharedPreferences.edit()
                                editor.putString("NAME", un).apply()
                                ToastTool(acti, "Change user name successful!")
                                acti.finish()
                            }

                            Common().KO_CODE_SV ->{
                                when(res.cause){
                                    "wrongPass" ->{
                                        ToastTool(acti, "Wrong pass!")
                                    }

                                    "notlogin" ->{
                                        ToastTool(acti, "Error, cannot change info!")
                                    }
                                }
                            }

                            Common().ERROR_CODE_SV ->{
                                ToastTool(acti, "Server error!")
                            }
                        }
                    }else{
                        ToastTool(acti, "Error")
                    }
                }else{
                    ToastTool(acti, "Error, cannot change info!")
                }
            }

            override fun onFailure(p0: Call<changeInfo>, p1: Throwable) {
                Log.d("Error change info", p1.toString())
                ToastTool(acti, "Error, Cannot connect to server!")
            }

        })
    }
}