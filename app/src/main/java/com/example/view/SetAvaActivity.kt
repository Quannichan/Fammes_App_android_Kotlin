package com.example.view

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.view.Tool.ButtonTool
import com.example.view.controller.uploadImg
import okhttp3.MediaType
import okhttp3.RequestBody

class SetAvaActivity : AppCompatActivity(){

    private val PICK_IMAGE_REQUEST = 1
    private lateinit var imageView: ImageView
    private lateinit var selectedImage: Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.setavatar_activity)
        imageView = findViewById<ImageView>(R.id.avatar)
        ButtonTool().getButton(R.id.choose_img, this)?.setOnClickListener({
            openGallery()
        })

        ButtonTool().getButton(R.id.upload_img, this)?.setOnClickListener({
            val sharedPreferences: SharedPreferences = getSharedPreferences("privateData", Context.MODE_PRIVATE)
            val token : String? = sharedPreferences.getString("TOK", "null")
            val id : Int = sharedPreferences.getInt("IDU", 0)

            val idRequestBody = RequestBody.create(MediaType.parse("text/plain"), id.toString())
            val tokenRequestBody = RequestBody.create(MediaType.parse("text/plain"), token)
            if (token != null) {
                uploadImg().setAvatar(selectedImage,idRequestBody,tokenRequestBody, this)
            }
        })

        ButtonTool().getButton(R.id.skip_img, this)?.setOnClickListener({
            val intent = Intent(this, AppActivity::class.java)
            this.startActivity(intent)
            this.finish()
        })
     }
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    // Xử lý kết quả trả về từ bộ chọn ảnh
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            val selectedImageUri = data.data
            imageView.setImageURI(selectedImageUri)
            ButtonTool().getButton(R.id.upload_img, this)?.visibility = View.VISIBLE
            selectedImageUri?.let {
                selectedImage = BitmapFactory.decodeStream(contentResolver.openInputStream(it))
                imageView.setImageBitmap(selectedImage)
            }
        }
    }
}