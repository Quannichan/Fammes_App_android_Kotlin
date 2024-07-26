package com.example.view

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.example.view.config.Common
import com.example.view.databinding.AppActivityBinding
import com.example.view.model.chatModel
import com.squareup.picasso.Picasso

class Chat_adapter{
    @SuppressLint("SetTextI18n")
    fun bindingView(chat : ArrayList<chatModel>, bind : AppActivityBinding, acti : Activity){
        for(chat in chat){
            val view = LayoutInflater.from(acti).inflate(R.layout.chat_compo,null)
            val imageView = view?.findViewById<ImageView>(R.id.ava2)
            val imageUrl = Common().IMG_ROOT_URL + chat.chat_img

            Log.d("img" , imageUrl)
            Picasso.get().load(imageUrl).into(imageView)

            val name = view?.findViewById<TextView>(R.id.mes_name)
            Log.d("name" ,chat.chat_name)
            name?.text = chat.chat_name

            val ID : Int = chat.id_user_last

            val mes_pre = view?.findViewById<TextView>(R.id.mes_pre)
            val sharedPreferences: SharedPreferences = acti.getSharedPreferences("privateData", Context.MODE_PRIVATE)
            val id : Int = sharedPreferences.getInt("IDU", 0)
            val token : String? = sharedPreferences.getString("TOK", "null")
            Log.d("debug", chat.last_mes_type.toString())
            if( chat.last_mes_type == 1){
                if(ID == id){
                    mes_pre?.text = "You: "+ chat.last_mes
                } else if(ID == 0){
                    mes_pre?.text = chat.last_mes
                }else {
                    mes_pre?.text = chat.name_user_last+ ": " + chat.last_mes
                }
            }else if(chat.last_mes_type == 2){
                Log.d("debug", chat.last_mes)
                if(ID == id){
                    mes_pre?.text = chat.last_mes
                } else if(ID == 0){
                    mes_pre?.text = chat.last_mes
                }else {
                    mes_pre?.text = chat.name_user_last+ ": " + chat.last_mes
                }
            }else if(chat.last_mes_type == 3){
                if(ID == id){
                    mes_pre?.text = "You: "+ chat.last_mes
                } else if(ID == 0){
                    mes_pre?.text = chat.last_mes
                }else {
                    mes_pre?.text = chat.name_user_last+ ": " + chat.last_mes
                }
            }else if(chat.last_mes_type == 4){
                if(ID == id){
                    mes_pre?.text = "You: "+ chat.last_mes
                } else if(ID == 0){
                    mes_pre?.text = chat.last_mes
                }else {
                    mes_pre?.text = chat.name_user_last+ ": " + chat.last_mes
                }
            }

            val time_send = view?.findViewById<TextView>(R.id.time_mes)
            time_send?.text = chat.lastest

            val seen_dot = view?.findViewById<CardView>(R.id.status_dot)
            if(chat.seen_status == 0){
                var text = SpannableString(mes_pre?.text)
                val spanbold = StyleSpan(Typeface.BOLD)
                text.setSpan(spanbold, 0, text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                mes_pre?.text = text
                seen_dot?.visibility = View.VISIBLE
            }else if(chat.seen_status == 1){
                seen_dot?.visibility = View.GONE
            }

            val id_hide = view?.findViewById<TextView>(R.id.id_hide)
            id_hide?.setText(chat.chat_id.toString() + "chat_id")

            view?.isClickable = true
            view?.setOnClickListener({
                val intent = Intent(acti, MessageActivity::class.java)
                var text = SpannableString(mes_pre?.text)
                val spanbold = StyleSpan(Typeface.NORMAL)
                text.setSpan(spanbold, 0, text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                mes_pre?.text = text
                seen_dot?.visibility = View.GONE
                intent.putExtra("ID_chat" , chat.chat_id)
                intent.putExtra("Img_chat" , imageUrl)
                intent.putExtra("chat_name" , chat.chat_name)
                intent.putExtra("ID_user" , id)
                intent.putExtra("token" , token)
                acti.startActivity(intent)
            })
            bind.listMes.addView(view)
        }
    }
}
