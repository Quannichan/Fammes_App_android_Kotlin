package com.example.view.controller

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.util.Log
import com.example.view.ApiService.UploadAvatar
import com.example.view.AppActivity
import com.example.view.Network.Network
import com.example.view.Tool.ToastTool
import com.example.view.datatype.uploadImgdata
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream

class uploadImg {

    val network = Network().RetrofitTool()

    fun setAvatar(bitmap : Bitmap, id : RequestBody, token : RequestBody, curActivity: Activity){
        val upload : UploadAvatar = network.create(UploadAvatar::class.java)

        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        val requestFile = RequestBody.create(MediaType.parse("image/*"), byteArray)
        val body = MultipartBody.Part.createFormData("image", "image.jpg", requestFile)

        upload.UploadAvatar(body,id, token).enqueue(object : Callback<uploadImgdata> {
            override fun onResponse(call: Call<uploadImgdata>, response: Response<uploadImgdata>) {
                if(response.isSuccessful) {
                    try {
                        val res = response.body()
                        if (res != null) {
                            val intent = Intent(curActivity, AppActivity::class.java)
                            curActivity.startActivity(intent)
                            curActivity.finish()
                        }else{
                            ToastTool(curActivity,"null")
                        }
                    } catch (err : Exception) {
                        Log.d("error" , ""+ err)
                        ToastTool(curActivity,"" + err)
                    }
                }else{
                    ToastTool(curActivity,"not succ")
                    Log.d("error" , "not succ")
                }
            }

            override fun onFailure(call: Call<uploadImgdata>, t: Throwable) {
                ToastTool(curActivity,"" + t)
                Log.d("error" , t.toString())
            }

        })
    }

}