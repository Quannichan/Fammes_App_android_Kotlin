package com.example.view

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.view.Tool.ButtonTool
import com.example.view.Tool.EditTextTool
import com.example.view.Tool.ToastTool
import com.example.view.config.Common
import com.example.view.controller.ChangeImgController
import com.squareup.picasso.Picasso
import java.io.File

class ChangeimgActivity : AppCompatActivity() {

    private val PICK_IMAGE_REQUEST = 1
    private lateinit var imageView: ImageView
    private lateinit var selectedImage : Bitmap
    private lateinit var path : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.change_img_activity)
        imageView = findViewById<ImageView>(R.id.ava_choose)

        val sharedPreferences: SharedPreferences = getSharedPreferences("privateData", Context.MODE_PRIVATE)
        val img_url : String = Common().IMG_ROOT_URL + sharedPreferences.getString("IMG", null)
        Picasso.get().load(img_url).into(imageView)
        ButtonTool().getButton(R.id.acpt_change_img,this)?.setOnClickListener {
            var pass = EditTextTool().getText(R.id.pass_change_img, this)
            if(pass.trim().length > 0){
                SendReqChange(pass,path,false)
            }else{
                ToastTool(this, "Please fill the password!")
            }
        }

        ButtonTool().getButton(R.id.acpt_default, this)?.setOnClickListener {
            var pass = EditTextTool().getText(R.id.pass_change_img, this)
            if(pass.trim().length > 0){
                SendReqChange(pass,"",true)
            }else{
                ToastTool(this, "Please fill the password!")
            }
        }


        imageView.setOnClickListener {
            openGallery()
        }

        ButtonTool().getImageButton(R.id.back_change_img, this)?.setOnClickListener {
            finish()
        }
    }

    fun SendReqChange(pass : String, img : String, isdef : Boolean){
        val sharedPreferences: SharedPreferences = getSharedPreferences("privateData", Context.MODE_PRIVATE)
        val id = sharedPreferences.getInt("IDU", 0)
        val tok = sharedPreferences.getString("TOK", null)
        val email = sharedPreferences.getString("EMAIL", null)
        ChangeImgController().sendChangeReq(id,tok.toString() ,email.toString() ,pass, isdef, img, this)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
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
            ButtonTool().getButton(R.id.acpt_change_img, this)?.visibility = View.VISIBLE
            selectedImageUri?.let {
                selectedImage = BitmapFactory.decodeStream(contentResolver.openInputStream(it))
                imageView.setImageBitmap(selectedImage)
            }
            ToastTool(this, "" + selectedImageUri)
            var img_file = File(getPathFromUri(selectedImageUri))
            path = img_file.toString()
        }
    }

    private fun getPathFromUri(uri: Uri?): String? {
        var path: String? = null
        val projection = arrayOf(android.provider.MediaStore.Images.Media.DATA)
        if (uri != null) {
            contentResolver.query(uri, projection, null, null, null)?.use { cursor ->
                if (cursor.moveToFirst()) {
                    val columnIndex = cursor.getColumnIndexOrThrow(android.provider.MediaStore.Images.Media.DATA)
                    path = cursor.getString(columnIndex)
                }
            }
        }
        return path
    }

}