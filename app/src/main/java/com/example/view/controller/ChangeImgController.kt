package com.example.view.controller

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.util.Base64
import android.util.Log
import com.example.view.ApiService.changeImg
import com.example.view.Network.Network
import com.example.view.Tool.ToastTool
import com.example.view.config.Common
import com.example.view.datatype.changeInfo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class ChangeImgController {
    val network = Network().RetrofitTool()
    fun sendChangeReq(id : Int, tok : String, email : String, p : String , default : Boolean ,filepath : String, acti : Activity){
        val changeImg = network.create(changeImg::class.java)
        var fileData : String? = null
        Log.d("file path ",filepath)
        if(!default){
            val fileBytes = File(filepath).readBytes()
            fileData = Base64.encodeToString(fileBytes, Base64.CRLF)
        }

        changeImg.changeImg(id,tok,email,p,default,fileData).enqueue(object : Callback<changeInfo>{
            override fun onResponse(p0: Call<changeInfo>, response: Response<changeInfo>) {
                if(response.isSuccessful){
                    val res = response.body()
                    if(res != null){
                        when(res.status){
                            Common().OK_CODE_SV -> {
                                val sharedPreferences: SharedPreferences = acti.getSharedPreferences("privateData", Context.MODE_PRIVATE)
                                sharedPreferences.edit().putString("IMG", res.newImg).apply()
                                ToastTool(acti, "Change image successful!")
                                acti.finish()
                            }

                            Common().KO_CODE_SV ->{
                                when(res.cause){
                                    "wrongPass" ->{
                                        ToastTool(acti, "Wrong pass!")
                                    }

                                    "notlogin" ->{
                                        ToastTool(acti, "Error, cannot change image!")
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
                    ToastTool(acti, "Error, cannot change image!")
                }
            }

            override fun onFailure(p0: Call<changeInfo>, p1: Throwable) {
                Log.d("Error change info", p1.toString())
                ToastTool(acti, "Error, Cannot connect to server!")
            }

        })
    }
}