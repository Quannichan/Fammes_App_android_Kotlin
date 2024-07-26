package com.example.view.controller

import android.app.Activity
import android.content.Intent
import android.view.View
import com.example.view.ApiService.register
import com.example.view.Network.Network
import com.example.view.SetAvaActivity
import com.example.view.Tool.ButtonTool
import com.example.view.Tool.LoaderTool
import com.example.view.Tool.ToastTool
import com.example.view.Tool.shareDataTool
import com.example.view.config.Common
import com.example.view.datatype.registerdata
import com.example.view.model.userModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class registerController {
    val common = Common()
    val Network = Network().RetrofitTool()

    fun register(userModel: userModel, acti : Activity, loadID : Int, btnID : Int){
        LoaderTool().getLoader(loadID, acti).visibility = View.VISIBLE
        ButtonTool().getButton(btnID, acti)?.visibility = View.GONE
        val register : register = Network.create(register::class.java)
        register.registerUser(userModel.name, userModel.email, userModel.pass).enqueue(object : Callback<registerdata>{
            override fun onResponse(call: Call<registerdata>, response: Response<registerdata>) {
                if(response.isSuccessful){
                    try{
                        val res = response.body()
                        if(res != null){
                            when(res.status){
                                2000 -> {
                                    LoaderTool().getLoader(loadID, acti).visibility = View.GONE
                                    ButtonTool().getButton(btnID, acti)?.visibility = View.VISIBLE
                                    shareDataTool().setData(acti,res.userdata.id, res.userdata.name, res.userdata.email, res.userdata.img, res.tokenizer)
                                    val intent = Intent(acti, SetAvaActivity::class.java)
                                    acti.startActivity(intent)
                                    acti.finish()
                                }
                                2001 ->{
                                    when(res.command){
                                        "Cannotcreateaccount" ->{
                                            ToastTool(acti, "Error, cannot create account!")
                                        }
                                        "Userexist" ->{
                                            ToastTool(acti, "Email is in use by other user, please try other email!")
                                        }
                                    }
                                    LoaderTool().getLoader(loadID, acti).visibility = View.GONE
                                    ButtonTool().getButton(btnID, acti)?.visibility = View.VISIBLE
                                }

                                2002 ->{
                                    ToastTool(acti, "Server error!")
                                    LoaderTool().getLoader(loadID, acti).visibility = View.GONE
                                    ButtonTool().getButton(btnID, acti)?.visibility = View.VISIBLE
                                }
                            }
                        }else{
                            ToastTool(acti, "error")
                            LoaderTool().getLoader(loadID, acti).visibility = View.GONE
                            ButtonTool().getButton(btnID, acti)?.visibility = View.VISIBLE
                        }
                    }catch (err : Exception){
                        LoaderTool().getLoader(loadID, acti).visibility = View.GONE
                        ButtonTool().getButton(btnID, acti)?.visibility = View.VISIBLE
                    }
                }else{
                    LoaderTool().getLoader(loadID, acti).visibility = View.GONE
                    ButtonTool().getButton(btnID, acti)?.visibility = View.VISIBLE

                    ToastTool(acti, "Cannot connect to server!")
                }
            }

            override fun onFailure(call: Call<registerdata>, t: Throwable) {
                LoaderTool().getLoader(loadID, acti).visibility = View.GONE
                ButtonTool().getButton(btnID, acti)?.visibility = View.VISIBLE
            }
        })
    }

}