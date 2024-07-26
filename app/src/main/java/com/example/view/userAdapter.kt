package com.example.view

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.view.LayoutInflater
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.view.config.Common
import com.example.view.controller.createChatController
import com.example.view.model.userModel
import com.squareup.picasso.Picasso

class userAdapter {
    fun bindingView(user : ArrayList<userModel>, acti : Activity){
        for(u in user){
            val view = LayoutInflater.from(acti).inflate(R.layout.user_component,null)
            val img = view.findViewById<ImageView>(R.id.img_user)
            val name = view.findViewById<TextView>(R.id.name_friend)
            val btn = view.findViewById<ImageButton>(R.id.addfrbtn)

            val imageUrl = Common().IMG_ROOT_URL + u.img
            Picasso.get().load(imageUrl).into(img)

            name.text = u.name

            btn.setOnClickListener {
                Log.d("User info" ,u.id.toString())
                val sharedPreferences: SharedPreferences = acti.getSharedPreferences("privateData", Context.MODE_PRIVATE)
                val id : Int = sharedPreferences.getInt("IDU", 0)
                val token : String? = sharedPreferences.getString("TOK", null)
                val name : String? = sharedPreferences.getString("NAME", null)
                val img_url : String? = sharedPreferences.getString("IMG", "null")
                createChatController().create(id, name.toString(), img_url.toString(), u.id, u.name, u.img, token.toString(),acti)
            }

            acti.findViewById<LinearLayout>(R.id.listFriend).addView(view)
        }
    }
}