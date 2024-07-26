package com.example.view.controller

import android.app.Activity
import android.util.Log
import com.example.view.ApiService.changePass
import com.example.view.Network.Network
import com.example.view.Tool.ToastTool
import com.example.view.config.Common
import com.example.view.datatype.changeInfo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChangePassController {
    val network = Network().RetrofitTool()
    fun sendChangeReq(id : Int, tok : String, email : String, p : String , np : String, acti : Activity){
        val changeus = network.create(changePass::class.java)
        changeus.changepass(id,tok,email,p,np).enqueue(object : Callback<changeInfo>{
            override fun onResponse(p0: Call<changeInfo>, response: Response<changeInfo>) {
                if(response.isSuccessful){
                    val res = response.body()
                    if(res != null){
                        when(res.status){
                            Common().OK_CODE_SV -> {
                                ToastTool(acti, "Change password successful!")
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
                    ToastTool(acti, "Error, cannot change password!")
                }
            }

            override fun onFailure(p0: Call<changeInfo>, p1: Throwable) {
                Log.d("Error change info", p1.toString())
                ToastTool(acti, "Error, Cannot connect to server!")
            }

        })
    }
}