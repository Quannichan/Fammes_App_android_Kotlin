package com.example.view.Service

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.os.AsyncTask
import android.os.Build
import android.os.IBinder
import android.util.Base64
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.arthenica.mobileffmpeg.FFmpeg
import com.example.view.AppActivity
import com.example.view.MessageActivity
import com.example.view.R
import com.example.view.Tool.ToastTool
import com.example.view.config.Common
import com.example.view.datatype.SocketImg
import com.example.view.model.socketRequest
import com.example.view.model.socketResponse
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import java.io.File

class SocketService : Service() {

    private var webSocket: WebSocket? = null
    private var type: Int =
        1 // 0 là ở giao diện app, 1 là thoát app hoặc tắt app, 2 là đang trong tin nhắn
    private var idchat: Int = 0
    private var inChat: Boolean = false
    val intents = Intent("websocket_data")
    val chat_intent = Intent("chat_update_data")

    private val activityStateReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == AppActivity.APP_ACTIVITY_ACTION_CHANGED) {
                val state = intent.getStringExtra(AppActivity.EXTRA_ACTIVITY_STATE)
                when (state) {
                    AppActivity.STATE_STARTED -> {
                        idchat = 0
                        inChat = true
                        type = 0
                    }

                    AppActivity.STATE_STOPPED -> {
                        type = 1
                    }
                    AppActivity.STATE_LOGOUT ->{
                        webSocket?.close(1000, "logout")
                    }
                }
            } else if (intent?.action == MessageActivity.MESSAGE_ACTIVITY_ACTION_CHANGED) {
                val state = intent.getStringExtra(MessageActivity.EXTRA_ACTIVITY_STATE)
                when (state) {
                    MessageActivity.STATE_STARTED -> {
                        val sharedPreferences: SharedPreferences =
                            getSharedPreferences("privateData", Context.MODE_PRIVATE)
                        val id: Int = sharedPreferences.getInt("IDU", 0)
                        val token: String? = sharedPreferences.getString("TOK", "null")
                        val id_chat: Int = intent.getIntExtra(MessageActivity.CHAT, 0)
                        type = 2
                        idchat = id_chat
                        inChat = true
                        val da = socketRequest(
                            "join_chat",
                            id,
                            id_chat,
                            null,
                            null,
                            null,
                            token.toString()
                        )
                        val json = Gson().toJson(da)
                        webSocket?.send(json)
                    }

                    MessageActivity.STATE_STOPPED -> {
                        val sharedPreferences: SharedPreferences =
                            getSharedPreferences("privateData", Context.MODE_PRIVATE)
                        val id: Int = sharedPreferences.getInt("IDU", 0)
                        val token: String? = sharedPreferences.getString("TOK", "null")
                        val da =
                            socketRequest("out_chat", id, 0, null, null, null, token.toString())
                        type = 1
                        idchat = 0
                        inChat = false
                        val json = Gson().toJson(da)
                        webSocket?.send(json)
                    }

                    MessageActivity.SEND_MESSAGE -> {
                        val request: Int = intent.getIntExtra(MessageActivity.REQUEST_SEND, 0)
                        val sharedPreferences: SharedPreferences =
                            getSharedPreferences("privateData", Context.MODE_PRIVATE)
                        val token: String? = sharedPreferences.getString("TOK", "null")
                        val id: Int = sharedPreferences.getInt("IDU", 0)
                        val id_chat: Int = intent.getIntExtra(MessageActivity.CHAT, 0)
                        Log.d("request", request.toString())
                        when (request) {
                            1 -> {
                                val messages: String? =
                                    intent.getStringExtra(MessageActivity.SEND_MES)
                                val da = socketRequest(
                                    "send_mes",
                                    id,
                                    id_chat,
                                    null,
                                    null,
                                    messages,
                                    token.toString()
                                )
                                val json = Gson().toJson(da)
                                webSocket?.send(json)
                            }

                            2 -> {
                                val imgPath = intent.getStringExtra(MessageActivity.SEND_IMG)
                                send_img_thread().execute(imgPath, token,idchat.toString(),id.toString())
                            }

                            3 -> {
                                val video_path = intent.getStringExtra(MessageActivity.SEND_VIDEO)
                                send_video_thread().execute(video_path, token,idchat.toString(),id.toString())
                            }
                        }
                    }
                }
            }
        }
    }

    inner class send_video_thread : AsyncTask<String, Void, String>(){

        override fun onPreExecute() {
            ToastTool(applicationContext,"Sending...")
        }

        override fun doInBackground(vararg params: String?): String {
            val video_path : String = params[0].toString()
            val token : String = params[1].toString()
            val id_chat = params[2].toString().toInt()
            val id = params[3].toString().toInt()
            Log.d("test data " , id_chat.toString() + id.toString())

            val cmd = arrayOf(
                "-i", video_path,
                "-s", "${480}x${800}",
                "-b:v", "${0.1}k",
                "/storage/emulated/0/DCIM/Camera/save_reduce_quality_fammes.mp4"
            )

            try {
                FFmpeg.execute(cmd)
                println("Video reduced successfully!")

            }catch (err : Exception){
                println("Failed to reduce video. Error code: $err")

            }
            val fileBytes = File("/storage/emulated/0/DCIM/Camera/save_reduce_quality_fammes.mp4").readBytes()
            val fileData = Base64.encodeToString(fileBytes, Base64.DEFAULT)
            val data = SocketImg(
                "send_video",
                token,
                id,
                id_chat,
                fileData
            )
            val json = Gson().toJson(data)
            webSocket?.send(json)
            return ""
        }

        override fun onPostExecute(result: String?) {
            ToastTool(applicationContext,"Video sent!")
            if(File("/storage/emulated/0/DCIM/Camera/save_reduce_quality_fammes.mp4").exists()){
                File("/storage/emulated/0/DCIM/Camera/save_reduce_quality_fammes.mp4").delete()
            }
        }
    }

    inner class send_img_thread : AsyncTask<String, Void, String>(){

        override fun onPreExecute() {
            ToastTool(applicationContext,"Sending...")
        }

        override fun doInBackground(vararg params: String?): String {
            val video_path : String = params[0].toString()
            val token : String = params[1].toString()
            val id_chat = params[2].toString().toInt()
            val id = params[3].toString().toInt()
            Log.d("test data " , id_chat.toString() + id.toString())

            val fileBytes = File(video_path).readBytes()
            val fileData = Base64.encodeToString(fileBytes, Base64.CRLF)
            val data = SocketImg(
                "send_img",
                token,
                id,
                id_chat,
                fileData
            )
            val json = Gson().toJson(data)
            webSocket?.send(json)
            return ""
        }

        override fun onPostExecute(result: String?) {
            ToastTool(applicationContext,"Image Sent!")
        }

    }


    override fun onCreate() {
        super.onCreate()
        startWebSocket()
        val sharedPreferences: SharedPreferences =
            getSharedPreferences("privateData", Context.MODE_PRIVATE)
        val id: Int = sharedPreferences.getInt("IDU", 0)
        val img: String? = sharedPreferences.getString("IMG", null)
        val name: String? = sharedPreferences.getString("NAME", null)
        val token: String? = sharedPreferences.getString("TOK", "null")
        val user = socketRequest(
            "start_connect",
            id,
            idchat,
            img.toString(),
            name.toString(),
            null,
            token.toString()
        )
        webSocket?.send(Gson().toJson(user))
        startForegroundService()
        registerReceiver(
            activityStateReceiver,
            IntentFilter(AppActivity.APP_ACTIVITY_ACTION_CHANGED)
        )
        registerReceiver(
            activityStateReceiver,
            IntentFilter(MessageActivity.MESSAGE_ACTIVITY_ACTION_CHANGED)
        )
    }

    private fun startWebSocket() {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(Common().SOCKET_URL)
            .build()
        val listener = object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                super.onOpen(webSocket, response)
                // Kết nối đã được mở
                this@SocketService.webSocket = webSocket
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                super.onMessage(webSocket, text)
                val json = Gson().fromJson(text, socketResponse::class.java)
                Log.d("type", type.toString())
                chat_intent.putExtra("id_chat_change", json.id_chat.toString() + "chat_id")
                intents.putExtra("id_user_send", json.id_user_send)
                Log.d("id user send", json.id_user_send.toString())
                if (json.from == "their") {
                    if(json.mes_type == 1){
                        chat_intent.putExtra(
                            "message_change",
                            json.name_user_send + ": " + json.message
                        )
                    }else if(json.mes_type == 2){
                        chat_intent.putExtra(
                            "message_change",
                            json.name_user_send + ": " + "sent an image"
                        )
                    }else if(json.mes_type == 3){
                        chat_intent.putExtra(
                            "message_change",
                            json.name_user_send + ": " + "sent a video"
                        )
                    }

                } else if (json.from == "mine") {
                    if(json.mes_type == 1){
                        chat_intent.putExtra("message_change", "You: " + json.message)
                    }else if(json.mes_type == 2){
                        chat_intent.putExtra("message_change", "You: " + "sent an image")
                    }else if(json.mes_type == 3){
                        chat_intent.putExtra("message_change", "You: " + "sent a video")
                    }
                } else if (json.from == "noti") {
                    chat_intent.putExtra("message_change", json.message)
                }
                LocalBroadcastManager.getInstance(this@SocketService).sendBroadcast(chat_intent)
                if (type == 0) {
                    makeNotification(
                        json.name_chat,
                        json.id_isInchat,
                        json.name_user_send,
                        json.message,
                        json.img,
                        idchat
                    )
                } else if (type == 2) {
                    if (json.id_isInchat == idchat) {
                        Log.d("message", json.message)
                        intents.putExtra("message_type", json.mes_type)
                        intents.putExtra("message", json.message)
                        intents.putExtra("from", json.from)
                        Log.d("message", json.from)
                        intents.putExtra("img_user", json.img)
                        LocalBroadcastManager.getInstance(this@SocketService).sendBroadcast(intents)
                    } else {
                        if (json.from.equals("their")) {
                            if(json.mes_type == 1){
                                makeNotification(
                                    json.name_chat,
                                    json.id_isInchat,
                                    json.name_user_send,
                                    json.name_user_send + ": "+ json.message,
                                    json.img,
                                    idchat
                                )
                            }else if(json.mes_type == 2){
                                makeNotification(
                                    json.name_chat,
                                    json.id_isInchat,
                                    json.name_user_send,
                                    json.name_user_send + ": sent an image",
                                    json.img,
                                    idchat
                                )
                            }else if(json.mes_type == 3){
                                makeNotification(
                                    json.name_chat,
                                    json.id_isInchat,
                                    json.name_user_send,
                                    json.name_user_send + ": sent a video",
                                    json.img,
                                    idchat
                                )
                            }

                        } else if (json.from.equals("noti")) {
                            makeNotification(
                                json.name_chat,
                                json.id_isInchat,
                                json.name_user_send,
                                json.message,
                                json.img,
                                idchat
                            )
                        }
                    }
                } else if (type == 1) {
                    Log.d("message", json.message)
                    if (json.id_isInchat == idchat) {
                        Log.d("message", json.message)
                        intents.putExtra("message_type", json.mes_type)
                        intents.putExtra("message", json.message)
                        intents.putExtra("from", json.from)
                        Log.d("message", json.from)
                        intents.putExtra("img_user", json.img)
                        LocalBroadcastManager.getInstance(this@SocketService).sendBroadcast(intents)
                    } else {
                        if (json.from.equals("their")) {
                            if(json.mes_type == 1){
                                makeNotification(
                                    json.name_chat,
                                    json.id_isInchat,
                                    json.name_user_send,
                                    json.message,
                                    json.img,
                                    idchat
                                )
                            }else if(json.mes_type == 2){
                                makeNotification(
                                    json.name_chat,
                                    json.id_isInchat,
                                    json.name_user_send,
                                    "sent an image",
                                    json.img,
                                    idchat
                                )
                            }else if(json.mes_type == 3){
                                makeNotification(
                                    json.name_chat,
                                    json.id_isInchat,
                                    json.name_user_send,
                                    "sent an video",
                                    json.img,
                                    idchat
                                )
                            }
                        } else if (json.from.equals("noti")) {
                            makeNotification(
                                json.name_chat,
                                json.id_isInchat,
                                json.name_user_send,
                                json.message,
                                json.img,
                                idchat
                            )
                        }
                    }
                }
            }

            @SuppressLint("LaunchActivityFromNotification")
            private fun makeNotification(
                chat_name: String,
                id_chat: Int,
                user_name_send: String,
                message: String,
                imageUrl: String,
                chat_id: Int
            ) {
                val chanelID = "CHANNEL_ID_NOTI"
                var notiBuilder = NotificationCompat.Builder(
                    applicationContext,
                    chanelID
                )
                notiBuilder.setSmallIcon(R.drawable.icon)
                    .setContentTitle(chat_name)
                    .setContentText(user_name_send + ": " + message)
                    .setAutoCancel(true)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setGroup(id_chat.toString())

                var intent = Intent(applicationContext, MessageActivity::class.java)
                val sharedPreferences: SharedPreferences =
                    getSharedPreferences("privateData", Context.MODE_PRIVATE)
                val id: Int = sharedPreferences.getInt("IDU", 0)
                val token: String? = sharedPreferences.getString("TOK", "null")
                intent.putExtra("ID_chat", chat_id)
                intent.putExtra("Img_chat", imageUrl)
                intent.putExtra("chat_name", chat_name)
                intent.putExtra("ID_user", id)
                intent.putExtra("token", token)


                var pendingIntent = PendingIntent.getService(
                    applicationContext,
                    0,
                    intent,
                    PendingIntent.FLAG_MUTABLE
                )
                notiBuilder.setContentIntent(pendingIntent)

                var notiManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    var notifiChannel = notiManager.getNotificationChannel(chanelID)
                    if (notifiChannel == null) {
                        var importance = NotificationManager.IMPORTANCE_HIGH
                        notifiChannel = NotificationChannel(
                            chanelID,
                            "desc",
                            importance
                        )
                        notiManager.createNotificationChannel(notifiChannel)

                    }

                    notiBuilder = NotificationCompat.Builder(
                        applicationContext,
                        chanelID
                    )
                    notiBuilder.setSmallIcon(R.drawable.icon)
                        .setContentTitle(chat_name)
                        .setContentText(user_name_send + ": " + message)
                        .setAutoCancel(true)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setGroup(id_chat.toString())
                        .setGroupSummary(true);
                    notiManager.notify(id_chat, notiBuilder.build())

                }
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                super.onFailure(webSocket, t, response)
                // Xử lý khi kết nối thất bại
            }
        }
        webSocket = client.newWebSocket(request, listener)
    }

    @SuppressLint("ForegroundServiceType")
    private fun startForegroundService() {

        val channelId = createNotificationChannel("your_channel_id", "Your Channel Name")
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setContentTitle("")
            .setContentText("")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
        startForeground(1, notificationBuilder.build())
    }

    private fun createNotificationChannel(channelId: String, channelName: String): String {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val chan =
                NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
            val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(chan)
            return channelId
        }
        return ""
    }


    override fun onDestroy() {
        super.onDestroy()
        // Đóng kết nối WebSocket khi dịch vụ bị hủy
        webSocket?.close(1000, null)
        // Hủy đăng ký BroadcastReceiver khi dịch vụ bị hủy
        unregisterReceiver(activityStateReceiver)
    }


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}

