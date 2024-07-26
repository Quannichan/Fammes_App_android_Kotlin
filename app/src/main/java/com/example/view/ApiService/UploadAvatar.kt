package com.example.view.ApiService

import android.annotation.SuppressLint
import com.example.view.datatype.uploadImgdata
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface UploadAvatar {
    @SuppressLint("NotConstructor")
    @Multipart
    @POST("images/UploadProfileImg") // Đường dẫn API endpoint
    fun UploadAvatar(@Part image: MultipartBody.Part, @Part("id") id: RequestBody, @Part("token") token: RequestBody) : Call<uploadImgdata>
}