package com.example.view

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Color
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.ScrollView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.RecyclerView
import com.example.view.Tool.ButtonTool
import com.example.view.Tool.EditTextTool
import com.example.view.Tool.ToastTool
import com.example.view.controller.getMesController


class MessageActivity : AppCompatActivity(){

    var id_chat = 0
    var mes_wrap : Message_adapter? = null
    var is_next_to_first = false
    var id_last_send = 0
    lateinit var rs : Cursor
    private val STORAGE_PERMISSION_CODE = 101
    private lateinit var wrap_img_choose : RecyclerView
    private lateinit var wrap_file_choose : LinearLayout
    private lateinit var list: List<String>

    private lateinit var broadcastReceiver: BroadcastReceiver

    companion object {
        const val MESSAGE_ACTIVITY_ACTION_CHANGED = "chat_changed"
        const val EXTRA_ACTIVITY_STATE = "chat_extra_state"
        const val STATE_STARTED = "message_stated"
        const val STATE_STOPPED = "message_stoped"

        const val CHAT = "chat_id"
        var CHAT_ID = 0

        const val SEND_MESSAGE = "send_mes"
        const val SEND_MES = "send_message"
        var MESSAGE_CONTENT = ""

        const val MES_WRAP = "message_wrap"
        var MESSAGE_WRAP_ID = R.id.wrap_mess

        const val SEND_IMG = "message_img"
        var IMG_SEND = ""

        const val SEND_VIDEO = "message_video"
        var VID_SEND = ""

        const val REQUEST_SEND = "request_send"
        var REQUEST = 0

        private const val PICK_FILE_REQUEST = 1
    }


    @SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.message_activity)
        val scroll = findViewById<ScrollView>(R.id.scroll_wrap_mes)

        if(intent != null){
            val ID_chat : Int = intent.getIntExtra("ID_chat" , 0)
            id_chat = ID_chat
        val ID_user : Int = intent.getIntExtra("ID_user" , 0)
        val Chat_img: String? = intent.getStringExtra("Img_chat" )
        val Chat_name: String? = intent.getStringExtra("chat_name" )
        val token : String? = intent.getStringExtra("token")
        wrap_img_choose = findViewById<RecyclerView>(R.id.imge_choose)
        wrap_file_choose = findViewById<LinearLayout>(R.id.wrap_media_choosen)
        if (token != null) {
            if (Chat_img != null) {
                if (Chat_name != null) {
                    scroll.visibility = View.GONE
                    var top = findViewById<LinearLayout>(R.id.wrap_top_info_chat)
                    findViewById<LinearLayout>(R.id.wrap_mess).removeAllViews()
                    findViewById<LinearLayout>(R.id.wrap_mess).addView(top)
                    findViewById<ProgressBar>(R.id.loading_mes).visibility = View.VISIBLE
                    getMesController().getMessage(ID_user, token, ID_chat, R.id.wrap_mess,R.id.scroll_wrap_mes, this, Chat_img , Chat_name )

                }
            }
        }
            scroll.fullScroll(View.FOCUS_DOWN)
            mes_wrap = Message_adapter()
            mes_wrap?.activity = this
            CHAT_ID = id_chat
            broadcastReceiver = object : BroadcastReceiver() {
                @RequiresApi(Build.VERSION_CODES.Q)
                override fun onReceive(context: Context, intent: Intent) {
                    Log.d("id_send", id_last_send.toString() + " " + is_next_to_first.toString())
                    val data = intent.getStringExtra("message")
                    val from = intent.getStringExtra("from")
                    val id_user_send = intent.getIntExtra("id_user_send", 0)
                    val mes_type : Int = intent.getIntExtra("message_type",0)

                    val mess_adap = Message_adapter()
                    mess_adap.activity = this@MessageActivity
                    when(from){
                        "mine" -> {
                            mess_adap.addView(1, data.toString(), R.id.wrap_mess,R.id.scroll_wrap_mes,null,is_next_to_first, mes_type)
                            is_next_to_first  = false
                            id_last_send = 0
                            scroll.fullScroll(View.FOCUS_DOWN)
                        }

                        "their" -> {
                            val img_url = intent.getStringExtra("img_user")
                            if(is_next_to_first){
                                if(id_user_send == id_last_send){
                                    mess_adap.addView(2, data.toString(), R.id.wrap_mess,R.id.scroll_wrap_mes,img_url.toString(), is_next_to_first, mes_type)
                                    scroll.fullScroll(View.FOCUS_DOWN)
                                }else{
                                    is_next_to_first = false
                                    id_last_send = id_user_send
                                    mess_adap.addView(2, data.toString(), R.id.wrap_mess,R.id.scroll_wrap_mes,img_url.toString(), is_next_to_first,mes_type)
                                    scroll.fullScroll(View.FOCUS_DOWN)
                                }
                            }else{
                                if(id_user_send == id_last_send){
                                    is_next_to_first = true
                                    mess_adap.addView(2, data.toString(), R.id.wrap_mess,R.id.scroll_wrap_mes,img_url.toString(), is_next_to_first,mes_type)
                                    scroll.fullScroll(View.FOCUS_DOWN)
                                }else{
                                    is_next_to_first = false
                                    id_last_send = id_user_send
                                    mess_adap.addView(2, data.toString(), R.id.wrap_mess,R.id.scroll_wrap_mes,img_url.toString(), is_next_to_first,mes_type)
                                    scroll.fullScroll(View.FOCUS_DOWN)
                                }
                            }
                        }

                        "noti" -> {
                            mess_adap.addView(0, data.toString(), R.id.wrap_mess, R.id.scroll_wrap_mes,null,is_next_to_first,mes_type)
                            is_next_to_first = false
                            scroll.fullScroll(View.FOCUS_DOWN)
                        }
                    }
                }
            }
            scroll.fullScroll(View.FOCUS_DOWN)
            val filter = IntentFilter("websocket_data")
            LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, filter)
        } else {
            ToastTool(this, "Error")
        }

        EditTextTool().getEditText( R.id.mes_input,this@MessageActivity).setOnClickListener({
            scroll.fullScroll(View.FOCUS_DOWN)
            ButtonTool().getImageButton(R.id.file_choose, this)?.visibility = View.VISIBLE
            ButtonTool().getImageButton(R.id.close_file_choose, this)?.visibility = View.GONE
            wrap_img_choose.removeAllViews()
            wrap_file_choose.visibility = View.GONE
        })

        ButtonTool().getImageButton(R.id.send_mes_btn, this)?.setOnClickListener {
            MESSAGE_CONTENT = EditTextTool().getText(R.id.mes_input, this@MessageActivity)
            var check_string = MESSAGE_CONTENT.trim()
            if(check_string.length > 0){
                if(MESSAGE_CONTENT.equals(":)")){
                    MESSAGE_CONTENT = "abc"
                }
                EditTextTool().getEditText( R.id.mes_input,this@MessageActivity).text = null
                MessageActivity.REQUEST = 1
                sendBroadcast(Intent(MessageActivity.MESSAGE_ACTIVITY_ACTION_CHANGED).apply {
                    putExtra(MessageActivity.EXTRA_ACTIVITY_STATE, MessageActivity.SEND_MESSAGE)
                    putExtra(MessageActivity.REQUEST_SEND, MessageActivity.REQUEST)
                    putExtra(MessageActivity.SEND_MES, MessageActivity.MESSAGE_CONTENT)
                    putExtra(MessageActivity.CHAT, MessageActivity.CHAT_ID)
                    putExtra(MessageActivity.MES_WRAP, MessageActivity.MESSAGE_WRAP_ID)
                })
            }
        }

        ButtonTool().getImageButton(R.id.back_mes, this)?.setOnClickListener({
            sendBroadcast(Intent(MessageActivity.MESSAGE_ACTIVITY_ACTION_CHANGED).apply {
                putExtra(MessageActivity.EXTRA_ACTIVITY_STATE, MessageActivity.STATE_STOPPED)
            })
            finish()
        })

        ButtonTool().getImageButton(R.id.file_choose, this)?.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
            ) {
                // Request permission
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    STORAGE_PERMISSION_CODE
                )
            } else {
                ButtonTool().getImageButton(R.id.close_file_choose, this)?.visibility = View.VISIBLE
                wrap_file_choose.visibility = View.VISIBLE
                ButtonTool().getImageButton(R.id.file_choose, this)?.visibility = View.GONE
                LoadMedia_thread().execute("img")
            }
            scroll.fullScroll(View.FOCUS_DOWN)
        }

        ButtonTool().getImageButton(R.id.close_file_choose, this)?.setOnClickListener {
            ButtonTool().getImageButton(R.id.file_choose, this)?.visibility = View.VISIBLE
            ButtonTool().getImageButton(R.id.close_file_choose, this)?.visibility = View.GONE
            wrap_img_choose.removeAllViews()
            wrap_file_choose.visibility = View.GONE
            scroll.fullScroll(View.FOCUS_DOWN)
        }
        val pick_img =  ButtonTool().getImageButton(R.id.img_library, this)
        val pick_video = ButtonTool().getImageButton(R.id.video_library, this)
        pick_img?.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
            ) {
                // Request permission
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    STORAGE_PERMISSION_CODE
                )
            } else {
                pick_img.setBackgroundColor(Color.parseColor("#E8E7E7"))
                pick_video?.setBackgroundColor(Color.parseColor("#FFFFFF"))
                ButtonTool().getImageButton(R.id.file_choose, this)?.visibility = View.GONE
                LoadMedia_thread().execute("img")
            }
        }

        pick_video?.setOnClickListener {
            pick_video.setBackgroundColor(Color.parseColor("#E8E7E7"))
            pick_img?.setBackgroundColor(Color.parseColor("#FFFFFF"))
            LoadMedia_thread().execute("vid")
        }
    }

    override fun onStop() {
        super.onStop()
        sendBroadcast(Intent(MessageActivity.MESSAGE_ACTIVITY_ACTION_CHANGED).apply {
            putExtra(MessageActivity.EXTRA_ACTIVITY_STATE, MessageActivity.STATE_STOPPED)
        })
    }

    override fun onResume() {
        super.onResume()
        val scroll = findViewById<ScrollView>(R.id.scroll_wrap_mes)
        sendBroadcast(Intent(MessageActivity.MESSAGE_ACTIVITY_ACTION_CHANGED).apply {
            putExtra(MessageActivity.EXTRA_ACTIVITY_STATE, MessageActivity.STATE_STARTED)
            putExtra(MessageActivity.CHAT, MessageActivity.CHAT_ID)
        })
        scroll.fullScroll(View.FOCUS_DOWN)
    }


    fun ListImg(context: Context) : List<String>{

        val mediaPaths = mutableListOf<String>()
        val contentResolver: ContentResolver = context.contentResolver

        // Query images
        val imageProjection = arrayOf(MediaStore.Images.Media.DATA)
        val imageCursor = contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            imageProjection,
            null,
            null,
            null
        )

        imageCursor?.use {
            val imageColumnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            while (it.moveToNext()) {
                val imagePath = it.getString(imageColumnIndex)
                mediaPaths.add(imagePath)
            }
        }
        return mediaPaths

    }

    inner class LoadMedia_thread : AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg params: String?): String {
            val request : String = params[0].toString()
            when(request){
                "img"->{
                    list = ListImg(applicationContext)
                }

                "vid" ->{
                    list = listVideo(applicationContext)
                }
            }
            return request
        }

        override fun onPostExecute(result: String?) {
            when(result){
                "img"->{
                    wrap_img_choose.adapter = media_adapter(this@MessageActivity,list.reversed())
                }

                "vid" ->{
                    wrap_img_choose.adapter = media_adapterType_video(this@MessageActivity,list.reversed())
                }
            }
        }

    }

    fun listVideo(context: Context): List<String>{
        val mediaPaths = mutableListOf<String>()
        val contentResolver: ContentResolver = context.contentResolver

        // Query images
        val imageProjection = arrayOf(MediaStore.Video.Media.DATA)
        val imageCursor = contentResolver.query(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            imageProjection,
            null,
            null,
            null
        )

        imageCursor?.use {
            val imageColumnIndex = it.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
            while (it.moveToNext()) {
                val imagePath = it.getString(imageColumnIndex)
                Log.d("video", imagePath)
                mediaPaths.add(imagePath)
            }
        }
        return mediaPaths
    }
}