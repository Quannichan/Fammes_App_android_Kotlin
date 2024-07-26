package com.example.view.controller
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Handler
import android.util.Log
import android.view.View
import com.example.view.ApiService.checklogin
import com.example.view.ApiService.login
import com.example.view.AppActivity
import com.example.view.CannotConnectAcivity
import com.example.view.HomeActivity
import com.example.view.Network.Network
import com.example.view.Tool.ButtonTool
import com.example.view.Tool.LoaderTool
import com.example.view.Tool.ToastTool
import com.example.view.Tool.shareDataTool
import com.example.view.config.Common
import com.example.view.datatype.logindata
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class loginController {
    var Network = Network().RetrofitTool()

    fun Login(email: String, pass : String, activity: Activity, btn_Id : Int, load_id : Int) {
        var ButtonTool = ButtonTool()
        var LoaderTool = LoaderTool()
        if(checkInternet(activity)){
            val login : login = Network.create(login::class.java)
            login.loginUser(email, pass).enqueue(object : Callback<logindata> {
                @SuppressLint("CommitPrefEdits")
                override fun onResponse(
                    call: Call<logindata>,
                    response: Response<logindata>
                ) {
                    if (response.isSuccessful) {
                        try {
                            val responseData = response.body()
                            if (responseData != null) {
                                if (responseData.status ==  Common().OK_CODE_SV) {
                                    shareDataTool().setData(activity,responseData.userdata.ID, responseData.userdata.name, responseData.userdata.email, responseData.userdata.img, responseData.tokenizer)
                                    LoaderTool.getLoader(load_id, activity).visibility = View.GONE
                                    ButtonTool.getButton(btn_Id, activity)?.visibility = View.VISIBLE
                                    val intent = Intent(activity, AppActivity::class.java)
                                    activity.startActivity(intent)
                                    activity.finish()
                                }else if(responseData.status ==  Common().KO_CODE_SV){
                                    LoaderTool.getLoader(load_id, activity).visibility = View.GONE
                                    ButtonTool.getButton(btn_Id, activity)?.visibility = View.VISIBLE
                                    ToastTool(activity, "Wrong information!")
                                } else if(responseData.status == Common().ERROR_CODE_SV) {
                                    Log.d(
                                        "CHECK_RESPONSE",
                                        "Server error, please try again!"
                                    )
                                    LoaderTool.getLoader(load_id, activity).visibility = View.GONE
                                    ButtonTool.getButton(btn_Id, activity)?.visibility = View.VISIBLE
                                    ToastTool(activity, "Server error!")
                                }
                            } else {
                                Log.d("CHECK_RESPONSE", "Response body is null")
                                LoaderTool.getLoader(load_id, activity).visibility = View.GONE
                                ButtonTool.getButton(btn_Id, activity)?.visibility = View.VISIBLE
                                ToastTool(activity, "Cannot login!")
                            }
                        } catch (err: Exception) {
                            LoaderTool.getLoader(load_id, activity).visibility = View.GONE
                            ButtonTool.getButton(btn_Id, activity)?.visibility = View.VISIBLE
                            ToastTool(activity, "Error!")
                            Log.d(
                                "CHECK_RESPONSE",
                                "Error parsing response: ${err.toString()}"
                            )
                        }
                    } else {
                        LoaderTool.getLoader(load_id, activity).visibility = View.GONE
                        ButtonTool.getButton(btn_Id, activity)?.visibility = View.VISIBLE
                        ToastTool(activity, "Cannot Connect server!")
                        Log.d(
                            "CHECK_RESPONSE",
                            "Unsuccessful response: ${response.code()}"
                        )
                    }
                }

                override fun onFailure(call: Call<logindata>, t: Throwable) {
                    LoaderTool.getLoader(load_id, activity).visibility = View.GONE
                    ButtonTool.getButton(btn_Id, activity)?.visibility = View.VISIBLE
                    ToastTool(activity, "Cannot Connect server!")
                    Log.d("CHECK_RESPONSE", "Request failed: ${t.message}")
                }
            })
        }else{
            cannotConnect(activity)
        }
    }

    fun checkLogin(id : Int, token : String, acti: Activity) {
        if(checkInternet(acti)){
            val checkLogin = Network.create(checklogin::class.java)
            if(token.isNotEmpty() && id != 0){
                checkLogin.checkloginUser(id, token).enqueue(object : Callback<logindata>{
                    override fun onResponse(call: Call<logindata>, response: Response<logindata>) {
                        if(response.isSuccessful){
                            val res = response.body()
                            if (res != null) {
                                when(res.status){
                                    Common().OK_CODE_SV ->{
                                        logedIn(acti)
                                    }
                                    Common().KO_CODE_SV ->{
                                        nonLogIn(acti)
                                    }
                                }
                            }
                        }else{
                            cannotConnect(acti)
                        }
                    }
                    override fun onFailure(call: Call<logindata>, t: Throwable) {
                        cannotConnect(acti)
                    }
                })
            }else{
                checkConnectAPI().checkConnectAPI(acti)
            }
        }else{
            cannotConnect(acti)
        }
    }

    private fun logedIn(acti : Activity){
        Handler().postDelayed({
            val intent = Intent(acti, AppActivity::class.java);
            acti.startActivity(intent);
            acti.overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
            acti.finish();
        },3000)
    }

    fun nonLogIn(acti : Activity){
        Handler().postDelayed({
            val intent = Intent(acti, HomeActivity::class.java);
            acti.startActivity(intent);
            acti.overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
            acti.finish();
        },3000)
    }

    fun cannotConnect(acti: Activity){
        Handler().postDelayed({
            val intent = Intent(acti, CannotConnectAcivity::class.java);
            acti.startActivity(intent);
            acti.overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
            acti.finish();
        },3000)
    }

    private fun checkInternet(context : Context) : Boolean{
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork
            val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
            return networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
        } else {
            val networkInfo = connectivityManager.activeNetworkInfo
            return networkInfo?.isConnected == true
        }
    }
}