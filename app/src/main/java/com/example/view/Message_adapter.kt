package com.example.view

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.MediaController
import android.widget.ProgressBar
import android.widget.ScrollView
import android.widget.TextView
import android.widget.VideoView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.view.config.Common
import com.example.view.model.messageModel
import com.squareup.picasso.Picasso

class Message_adapter {
    var activity : Activity? = null

    @SuppressLint("MissingInflatedId")
    fun bindingView(mes : ArrayList<messageModel>, ID_wrap_mes : Int, ID_scroll : Int,acti : Activity, chat_img : String ,  chat_name: String){
        activity = acti
        val img_message = acti.findViewById<ImageView>(R.id.avatar_chat)
        Picasso.get().load(chat_img).into(img_message)
        val imge_title = acti.findViewById<ImageView>(R.id.img_title)
        Picasso.get().load(chat_img).into(imge_title)
        val name_chat = acti.findViewById<TextView>(R.id.name_chat)
        val name_title = acti.findViewById<TextView>(R.id.Name_title)
        name_title.text = chat_name
        name_chat.text = chat_name
        var last_id_send = 0
        var is_next_to_first_mes :Boolean = false
        val sharedPreferences: SharedPreferences = acti.getSharedPreferences("privateData", Context.MODE_PRIVATE)
        val id : Int = sharedPreferences.getInt("IDU", 0)
        val scroll = acti.findViewById<ScrollView>(ID_scroll)
        if(mes.isNotEmpty()){
            val wrap_mes = acti.findViewById<LinearLayout>(ID_wrap_mes)
            for(mess in mes){
                val view = LayoutInflater.from(acti).inflate(R.layout.mes_compo,null)
                val mine_mes = view?.findViewById<ConstraintLayout>(R.id.mine_mes)
                val their_mes = view?.findViewById<ConstraintLayout>(R.id.their_mes)
                val their_mes_img = view?.findViewById<ConstraintLayout>(R.id.their_mes_hasImg)
                val noti_mes = view?.findViewById<LinearLayout>(R.id.noti_mes)
                val mes_video_their_IMG = view?.findViewById<ConstraintLayout>(R.id.their_vid_hasImg)
                val mes_video_their_NOIMG = view?.findViewById<ConstraintLayout>(R.id.their_vid_noImg)
                val mes_img_their_IMG = view?.findViewById<ConstraintLayout>(R.id.their_img_hasImg)
                val mes_img_their_NOIMG = view?.findViewById<ConstraintLayout>(R.id.their_img_noImg)

                val mine_img = view?.findViewById<ConstraintLayout>(R.id.mine_img)
                val mine_video = view?.findViewById<ConstraintLayout>(R.id.mine_vid)
                when(mess.ID_user_send){
                    0 -> {
                        mine_mes?.visibility = View.GONE
                        their_mes?.visibility = View.GONE
                        their_mes_img?.visibility = View.GONE
                        mine_video?.visibility = View.GONE
                        mine_img?.visibility = View.GONE
                        mine_mes?.visibility = View.GONE
                        mes_img_their_NOIMG?.visibility = View.GONE
                        mes_img_their_IMG?.visibility = View.GONE
                        mes_video_their_NOIMG?.visibility = View.GONE
                        mes_video_their_IMG?.visibility = View.GONE

                        noti_mes?.visibility = View.VISIBLE
                        val noti_content = view?.findViewById<TextView>(R.id.noti_content)
                        noti_content?.text = mess.message
                        is_next_to_first_mes = false
                        last_id_send = 0
                        wrap_mes.addView(view)
                        scroll.fullScroll(View.FOCUS_DOWN)
                    }

                    id -> {
                        Log.d("debug 3",mess.mess_type.toString() + " " + mess.message)
                        noti_mes?.visibility = View.GONE
                        their_mes?.visibility = View.GONE
                        their_mes_img?.visibility = View.GONE

                        mes_img_their_NOIMG?.visibility = View.GONE
                        mes_img_their_IMG?.visibility = View.GONE
                        mes_video_their_NOIMG?.visibility = View.GONE
                        mes_video_their_IMG?.visibility = View.GONE

                        when(mess.mess_type){
                            1 ->{
                                mine_video?.visibility = View.GONE
                                mine_img?.visibility = View.GONE
                                mine_mes?.visibility = View.VISIBLE
                                val mineMes_content = view?.findViewById<TextView>(R.id.mine_content)
                                mineMes_content?.text = mess.message
                                last_id_send = 0
                                is_next_to_first_mes = false
                                wrap_mes.addView(view)
                                scroll.fullScroll(View.FOCUS_DOWN)
                            }

                            2->{
                                mine_video?.visibility = View.GONE
                                mine_img?.visibility = View.VISIBLE
                                mine_mes?.visibility = View.GONE
                                last_id_send = 0
                                is_next_to_first_mes = false
                                val img_view = view?.findViewById<ImageView>(R.id.mine_img_content)
                                val imageUrl = Common().IMG_ROOT_URL + mess.message
                                Log.d("mess Mine ", imageUrl)
                                Picasso.get().load(imageUrl).into(img_view)
                                wrap_mes.addView(view)
                            }

                            3->{
                                mine_video?.visibility = View.VISIBLE
                                mine_img?.visibility = View.GONE
                                mine_mes?.visibility = View.GONE
                                last_id_send = 0
                                is_next_to_first_mes = false
                                val video_view = view?.findViewById<VideoView>(R.id.mine_vid_content)
                                val uri = Uri.parse(Common().IMG_ROOT_URL + mess.message)
                                video_view?.setVideoURI(uri)
                                val mediaController = MediaController(acti)
                                mediaController.setAnchorView(video_view)
                                video_view?.setMediaController(mediaController)
                                wrap_mes.addView(view)
                            }
                        }
                    }

                    else -> {
                        Log.d("debug 3",mess.mess_type.toString() + " " + mess.message)
                        mine_video?.visibility = View.GONE
                        mine_img?.visibility = View.GONE
                        mine_mes?.visibility = View.GONE
                        noti_mes?.visibility = View.GONE
                        their_mes_img?.visibility = View.GONE
                        mine_img?.visibility = View.GONE
                        mine_video?.visibility = View.GONE
                        if(is_next_to_first_mes){
                            if(mess.ID_user_send == last_id_send){
                                is_next_to_first_mes = true
                                when(mess.mess_type){
                                    1 ->{
                                        mes_img_their_NOIMG?.visibility = View.GONE
                                        mes_img_their_IMG?.visibility = View.GONE
                                        mes_video_their_NOIMG?.visibility = View.GONE
                                        mes_video_their_IMG?.visibility = View.GONE
                                        their_mes?.visibility = View.VISIBLE
                                        val their_mes_Noimg = view?.findViewById<TextView>(R.id.their_mes_content)
                                        their_mes_Noimg?.text = mess.message
                                        wrap_mes?.addView(view)
                                    }

                                    2->{
                                        mes_img_their_NOIMG?.visibility = View.VISIBLE
                                        mes_img_their_IMG?.visibility = View.GONE
                                        mes_video_their_NOIMG?.visibility = View.GONE
                                        mes_video_their_IMG?.visibility = View.GONE
                                        their_mes?.visibility = View.GONE

                                        val image_view = view?.findViewById<ImageView>(R.id.their_imgMes_noImg_content)
                                        val imageUrl = Common().IMG_ROOT_URL + mess.message
                                        Picasso.get().load(imageUrl).into(image_view)
                                        wrap_mes?.addView(view)
                                    }

                                    3->{
                                        mes_img_their_NOIMG?.visibility = View.GONE
                                        mes_img_their_IMG?.visibility = View.GONE
                                        mes_video_their_NOIMG?.visibility = View.VISIBLE
                                        mes_video_their_IMG?.visibility = View.GONE
                                        their_mes?.visibility = View.GONE

                                        val video_view = view?.findViewById<VideoView>(R.id.theirvideonoImg_content)
                                        val uri = Uri.parse(Common().IMG_ROOT_URL + mess.message)
                                        video_view?.setVideoURI(uri)
                                        val mediaController = MediaController(acti)
                                        mediaController.setAnchorView(video_view)
                                        video_view?.setMediaController(mediaController)
                                        wrap_mes?.addView(view)
                                    }
                                }


                            }else{
                                is_next_to_first_mes = false
                                last_id_send = mess.ID_user_send
                                noti_mes?.visibility = View.GONE
                                mine_mes?.visibility = View.GONE
                                their_mes?.visibility = View.GONE
                                mine_img?.visibility = View.GONE
                                mine_video?.visibility = View.GONE

                                when(mess.mess_type){
                                    1 ->{
                                        mes_img_their_NOIMG?.visibility = View.GONE
                                        mes_img_their_IMG?.visibility = View.GONE
                                        mes_video_their_NOIMG?.visibility = View.GONE
                                        mes_video_their_IMG?.visibility = View.GONE
                                        their_mes_img?.visibility = View.VISIBLE
                                        val their_mes_hasimg = view?.findViewById<TextView>(R.id.their_content_hasImg)
                                        val imageView = view?.findViewById<ImageView>(R.id.img_their_mes)
                                        val imageUrl = Common().IMG_ROOT_URL + mess.img
                                        Picasso.get().load(imageUrl).into(imageView)
                                        their_mes_hasimg?.text = mess.message
                                        wrap_mes?.addView(view)
                                    }

                                    2->{
                                        their_mes_img?.visibility = View.GONE
                                        mes_img_their_NOIMG?.visibility = View.GONE
                                        mes_img_their_IMG?.visibility = View.VISIBLE
                                        mes_video_their_NOIMG?.visibility = View.GONE
                                        mes_video_their_IMG?.visibility = View.GONE

                                        val imageView = view?.findViewById<ImageView>(R.id.img_their_image)
                                        val image = Common().IMG_ROOT_URL + mess.img
                                        Picasso.get().load(image).into(imageView)

                                        val image_view = view?.findViewById<ImageView>(R.id.their_imgMes_hasImg_content)
                                        val imageUrl = Common().IMG_ROOT_URL + mess.message
                                        Picasso.get().load(imageUrl).into(image_view)
                                        wrap_mes?.addView(view)
                                    }

                                    3->{
                                        mes_img_their_NOIMG?.visibility = View.GONE
                                        mes_img_their_IMG?.visibility = View.GONE
                                        mes_video_their_NOIMG?.visibility = View.GONE
                                        mes_video_their_IMG?.visibility = View.VISIBLE
                                        their_mes_img?.visibility = View.GONE

                                        val imageView = view?.findViewById<ImageView>(R.id.img_their_video)
                                        val image = Common().IMG_ROOT_URL + mess.img
                                        Picasso.get().load(image).into(imageView)

                                        val video_view = view?.findViewById<VideoView>(R.id.theirvideohasImg_content)
                                        val uri = Uri.parse(Common().IMG_ROOT_URL + mess.message)
                                        video_view?.setVideoURI(uri)
                                        val mediaController = MediaController(acti)
                                        mediaController.setAnchorView(video_view)
                                        video_view?.setMediaController(mediaController)
                                        wrap_mes?.addView(view)
                                    }
                                }
                            }
                        }else{
                            if(last_id_send == mess.ID_user_send){
                                is_next_to_first_mes = true

                                when(mess.mess_type){
                                    1 ->{
                                        mes_img_their_NOIMG?.visibility = View.GONE
                                        mes_img_their_IMG?.visibility = View.GONE
                                        mes_video_their_NOIMG?.visibility = View.GONE
                                        mes_video_their_IMG?.visibility = View.GONE
                                        their_mes?.visibility = View.VISIBLE
                                        val their_mes_Noimg = view?.findViewById<TextView>(R.id.their_mes_content)
                                        their_mes_Noimg?.text = mess.message
                                        wrap_mes?.addView(view)
                                    }

                                    2->{
                                        mes_img_their_NOIMG?.visibility = View.VISIBLE
                                        mes_img_their_IMG?.visibility = View.GONE
                                        mes_video_their_NOIMG?.visibility = View.GONE
                                        mes_video_their_IMG?.visibility = View.GONE
                                        their_mes?.visibility = View.GONE

                                        val image_view = view?.findViewById<ImageView>(R.id.their_imgMes_noImg_content)
                                        val imageUrl = Common().IMG_ROOT_URL + mess.message
                                        Picasso.get().load(imageUrl).into(image_view)
                                        wrap_mes?.addView(view)

                                    }

                                    3->{
                                        mes_img_their_NOIMG?.visibility = View.GONE
                                        mes_img_their_IMG?.visibility = View.GONE
                                        mes_video_their_NOIMG?.visibility = View.VISIBLE
                                        mes_video_their_IMG?.visibility = View.GONE
                                        their_mes?.visibility = View.GONE

                                        val video_view = view?.findViewById<VideoView>(R.id.theirvideonoImg_content)
                                        val uri = Uri.parse(Common().IMG_ROOT_URL + mess.message)
                                        video_view?.setVideoURI(uri)
                                        val mediaController = MediaController(acti)
                                        mediaController.setAnchorView(video_view)
                                        video_view?.setMediaController(mediaController)
                                        wrap_mes?.addView(view)
                                    }
                                }
                            }else{
                                is_next_to_first_mes = false
                                last_id_send = mess.ID_user_send
                                when(mess.mess_type){
                                    1 ->{
                                        mes_img_their_NOIMG?.visibility = View.GONE
                                        mes_img_their_IMG?.visibility = View.GONE
                                        mes_video_their_NOIMG?.visibility = View.GONE
                                        mes_video_their_IMG?.visibility = View.GONE
                                        their_mes_img?.visibility = View.VISIBLE
                                        val their_mes_hasimg = view?.findViewById<TextView>(R.id.their_content_hasImg)
                                        val imageView = view?.findViewById<ImageView>(R.id.img_their_mes)
                                        val imageUrl = Common().IMG_ROOT_URL + mess.img
                                        Picasso.get().load(imageUrl).into(imageView)
                                        their_mes_hasimg?.text = mess.message
                                        wrap_mes?.addView(view)
                                    }

                                    2->{
                                        their_mes_img?.visibility = View.GONE
                                        mes_img_their_NOIMG?.visibility = View.GONE
                                        mes_img_their_IMG?.visibility = View.VISIBLE
                                        mes_video_their_NOIMG?.visibility = View.GONE
                                        mes_video_their_IMG?.visibility = View.GONE

                                        val imageView = view?.findViewById<ImageView>(R.id.img_their_image)
                                        val image = Common().IMG_ROOT_URL + mess.img
                                        Picasso.get().load(image).into(imageView)

                                        val image_view = view?.findViewById<ImageView>(R.id.their_imgMes_hasImg_content)
                                        val imageUrl = Common().IMG_ROOT_URL + mess.message
                                        Picasso.get().load(imageUrl).into(image_view)
                                        wrap_mes?.addView(view)
                                    }

                                    3->{
                                        mes_img_their_NOIMG?.visibility = View.GONE
                                        mes_img_their_IMG?.visibility = View.GONE
                                        mes_video_their_NOIMG?.visibility = View.GONE
                                        mes_video_their_IMG?.visibility = View.VISIBLE
                                        their_mes_img?.visibility = View.GONE

                                        val imageView = view?.findViewById<ImageView>(R.id.img_their_video)
                                        val image = Common().IMG_ROOT_URL + mess.img
                                        Picasso.get().load(image).into(imageView)

                                        val video_view = view?.findViewById<VideoView>(R.id.theirvideohasImg_content)
                                        val uri = Uri.parse(Common().IMG_ROOT_URL + mess.message)
                                        video_view?.setVideoURI(uri)
                                        val mediaController = MediaController(acti)
                                        mediaController.setAnchorView(video_view)
                                        video_view?.setMediaController(mediaController)
                                        wrap_mes?.addView(view)
                                    }
                                }
                            }
                        }
                    }
                }
            }
            scroll.fullScroll(View.FOCUS_DOWN)
        }
        acti.findViewById<ProgressBar>(R.id.loading_mes).visibility = View.GONE
        scroll.fullScroll(View.FOCUS_DOWN)
        scroll.visibility = View.VISIBLE

    }
    fun addView(type : Int, message : String, ID_wrap_mes : Int, ID_scroll: Int,img : String?, is_their_next :Boolean, mes_type : Int){
        val scroll = activity?.findViewById<ScrollView>(ID_scroll)
        val wrap_mes = activity?.findViewById<LinearLayout>(ID_wrap_mes)
        when(type){
            0 ->{
                val view = LayoutInflater.from(activity).inflate(R.layout.mes_compo,null)
                val mine_mes = view?.findViewById<ConstraintLayout>(R.id.mine_mes)
                val their_mes = view?.findViewById<ConstraintLayout>(R.id.their_mes)
                val their_mes_img = view?.findViewById<ConstraintLayout>(R.id.their_mes_hasImg)
                val noti_mes = view?.findViewById<LinearLayout>(R.id.noti_mes)
                val mes_video_their_IMG = view?.findViewById<ConstraintLayout>(R.id.their_vid_hasImg)
                val mes_video_their_NOIMG = view?.findViewById<ConstraintLayout>(R.id.their_vid_noImg)
                val mes_img_their_IMG = view?.findViewById<ConstraintLayout>(R.id.their_img_hasImg)
                val mes_img_their_NOIMG = view?.findViewById<ConstraintLayout>(R.id.their_img_noImg)

                val mine_img = view?.findViewById<ConstraintLayout>(R.id.mine_img)
                val mine_video = view?.findViewById<ConstraintLayout>(R.id.mine_vid)
                mes_video_their_IMG?.visibility = View.GONE
                mes_video_their_NOIMG?.visibility = View.GONE
                mes_img_their_IMG?.visibility = View.GONE
                mes_img_their_NOIMG?.visibility = View.GONE
                noti_mes?.visibility = View.VISIBLE
                mine_mes?.visibility = View.GONE
                their_mes?.visibility = View.GONE
                their_mes_img?.visibility = View.GONE
                mine_video?.visibility = View.GONE
                mine_img?.visibility = View.GONE
                val noti_content = view?.findViewById<TextView>(R.id.noti_content)
                noti_content?.text = message
                wrap_mes?.addView(view)
                scroll?.fullScroll(View.FOCUS_DOWN)

            }

            1->{
                val view = LayoutInflater.from(activity).inflate(R.layout.mes_compo,null)
                val mine_mes = view?.findViewById<ConstraintLayout>(R.id.mine_mes)
                val their_mes = view?.findViewById<ConstraintLayout>(R.id.their_mes)
                val their_mes_img = view?.findViewById<ConstraintLayout>(R.id.their_mes_hasImg)
                val noti_mes = view?.findViewById<LinearLayout>(R.id.noti_mes)
                val mes_video_their_IMG = view?.findViewById<ConstraintLayout>(R.id.their_vid_hasImg)
                val mes_video_their_NOIMG = view?.findViewById<ConstraintLayout>(R.id.their_vid_noImg)
                val mes_img_their_IMG = view?.findViewById<ConstraintLayout>(R.id.their_img_hasImg)
                val mes_img_their_NOIMG = view?.findViewById<ConstraintLayout>(R.id.their_img_noImg)

                val mine_img = view?.findViewById<ConstraintLayout>(R.id.mine_img)
                val mine_video = view?.findViewById<ConstraintLayout>(R.id.mine_vid)
                mes_video_their_IMG?.visibility = View.GONE
                mes_video_their_NOIMG?.visibility = View.GONE
                mes_img_their_IMG?.visibility = View.GONE
                mes_img_their_NOIMG?.visibility = View.GONE
                noti_mes?.visibility = View.GONE
                mine_mes?.visibility = View.GONE
                their_mes?.visibility = View.GONE
                their_mes_img?.visibility = View.GONE
                mine_video?.visibility = View.GONE
                mine_img?.visibility = View.GONE
                when(mes_type){
                    1 ->{
                        mine_mes?.visibility = View.VISIBLE
                        val mineMes_content = view?.findViewById<TextView>(R.id.mine_content)
                        mineMes_content?.text = message
                        wrap_mes?.addView(view)
                        scroll?.fullScroll(View.FOCUS_DOWN)
                    }

                    2->{
                        mine_img?.visibility = View.VISIBLE
                        val img_view = view?.findViewById<ImageView>(R.id.mine_img_content)
                        val imageUrl = Common().IMG_ROOT_URL + message
                        Log.d("mess Mine ", imageUrl)
                        Picasso.get().load(imageUrl).into(img_view)
                        wrap_mes?.addView(view)
                    }

                    3->{
                        mine_video?.visibility = View.VISIBLE
                        val video_view = view?.findViewById<VideoView>(R.id.mine_vid_content)
                        val uri = Uri.parse(Common().IMG_ROOT_URL + message)
                        video_view?.setVideoURI(uri)
                        val mediaController = MediaController(activity)
                        mediaController.setAnchorView(video_view)
                        video_view?.setMediaController(mediaController)
                        wrap_mes?.addView(view)
                    }
                }
                scroll?.fullScroll(View.FOCUS_DOWN)

            }

            2->{
                val view = LayoutInflater.from(activity).inflate(R.layout.mes_compo,null)
                val mine_mes = view?.findViewById<ConstraintLayout>(R.id.mine_mes)
                val their_mes = view?.findViewById<ConstraintLayout>(R.id.their_mes)
                val their_mes_img = view?.findViewById<ConstraintLayout>(R.id.their_mes_hasImg)
                val noti_mes = view?.findViewById<LinearLayout>(R.id.noti_mes)
                val mes_video_their_IMG = view?.findViewById<ConstraintLayout>(R.id.their_vid_hasImg)
                val mes_video_their_NOIMG = view?.findViewById<ConstraintLayout>(R.id.their_vid_noImg)
                val mes_img_their_IMG = view?.findViewById<ConstraintLayout>(R.id.their_img_hasImg)
                val mes_img_their_NOIMG = view?.findViewById<ConstraintLayout>(R.id.their_img_noImg)

                val mine_img = view?.findViewById<ConstraintLayout>(R.id.mine_img)
                val mine_video = view?.findViewById<ConstraintLayout>(R.id.mine_vid)
                mes_video_their_IMG?.visibility = View.GONE
                mes_video_their_NOIMG?.visibility = View.GONE
                mes_img_their_IMG?.visibility = View.GONE
                mes_img_their_NOIMG?.visibility = View.GONE
                noti_mes?.visibility = View.GONE
                mine_mes?.visibility = View.GONE
                their_mes?.visibility = View.GONE
                their_mes_img?.visibility = View.GONE
                mine_img?.visibility = View.GONE
                mine_video?.visibility = View.GONE

                if(is_their_next == true){
                    when(mes_type){
                        1 ->{
                            their_mes?.visibility = View.VISIBLE
                            val their_mes_Noimg = view?.findViewById<TextView>(R.id.their_mes_content)
                            their_mes_Noimg?.text = message
                            wrap_mes?.addView(view)
                        }

                        2->{
                            mes_img_their_NOIMG?.visibility = View.VISIBLE

                            val image_view = view?.findViewById<ImageView>(R.id.their_imgMes_noImg_content)
                            val imageUrl = Common().IMG_ROOT_URL + message
                            Picasso.get().load(imageUrl).into(image_view)
                            wrap_mes?.addView(view)
                        }

                        3->{
                            mes_video_their_NOIMG?.visibility = View.VISIBLE

                            val video_view = view?.findViewById<VideoView>(R.id.theirvideonoImg_content)
                            val uri = Uri.parse(Common().IMG_ROOT_URL + message)
                            video_view?.setVideoURI(uri)
                            val mediaController = MediaController(activity)
                            mediaController.setAnchorView(video_view)
                            video_view?.setMediaController(mediaController)
                            wrap_mes?.addView(view)
                        }
                    }


                }else{
                    when(mes_type){
                        1 ->{

                            their_mes_img?.visibility = View.VISIBLE
                            val their_mes_hasimg = view?.findViewById<TextView>(R.id.their_content_hasImg)
                            val imageView = view?.findViewById<ImageView>(R.id.img_their_mes)
                            val imageUrl = Common().IMG_ROOT_URL + img
                            Picasso.get().load(imageUrl).into(imageView)
                            their_mes_hasimg?.text = message
                            wrap_mes?.addView(view)
                        }

                        2->{
                            mes_img_their_IMG?.visibility = View.VISIBLE

                            val imageView = view?.findViewById<ImageView>(R.id.img_their_image)
                            val image = Common().IMG_ROOT_URL + img
                            Picasso.get().load(image).into(imageView)

                            val image_view = view?.findViewById<ImageView>(R.id.their_imgMes_hasImg_content)
                            val imageUrl = Common().IMG_ROOT_URL + message
                            Picasso.get().load(imageUrl).into(image_view)
                            wrap_mes?.addView(view)
                        }

                        3->{
                            mes_video_their_IMG?.visibility = View.VISIBLE

                            val imageView = view?.findViewById<ImageView>(R.id.img_their_video)
                            val image = Common().IMG_ROOT_URL + img
                            Picasso.get().load(image).into(imageView)

                            val video_view = view?.findViewById<VideoView>(R.id.theirvideohasImg_content)
                            val uri = Uri.parse(Common().IMG_ROOT_URL + message)
                            video_view?.setVideoURI(uri)
                            val mediaController = MediaController(activity)
                            mediaController.setAnchorView(video_view)
                            video_view?.setMediaController(mediaController)
                            wrap_mes?.addView(view)
                        }
                    }

                }
            }
        }
    }
}