package com.example.view.model

class socketResponse (
    val id_chat : Int,
    val name_chat : String,
    val status : Int,
    val message : String,
    val id_user_send : Int,
    val name_user_send : String,
    val from : String,
    val id_isInchat : Int,
    val img : String,
    val mes_type : Int //0 là thông báo 1 tin nhắn 2 là ảnh 3 là video
)