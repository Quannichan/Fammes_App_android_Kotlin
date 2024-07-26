package com.example.view

import android.app.Activity
import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.view.Service.SocketService
import com.example.view.Tool.ButtonTool
import com.example.view.config.Common
import com.example.view.controller.chatController
import com.example.view.controller.getUserController
import com.example.view.databinding.AppActivityBinding
import com.squareup.picasso.Picasso

class AppActivity : AppCompatActivity() {
    private lateinit var binding: AppActivityBinding
    private  lateinit var  acti : Activity
    private lateinit var broadcastReceiver: BroadcastReceiver

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AppActivityBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        acti = this
        val sharedPreferences: SharedPreferences = getSharedPreferences("privateData", Context.MODE_PRIVATE)
        val id : Int = sharedPreferences.getInt("IDU", 0)
        val token : String? = sharedPreferences.getString("TOK", "null")
        val name : String? = sharedPreferences.getString("NAME", "null")
        val img_url : String = Common().IMG_ROOT_URL + sharedPreferences.getString("IMG", "null")
        findViewById<TextView>(R.id.username_view).text = name
        val img_user = findViewById<ImageView>(R.id.user_img_view)
        Picasso.get().load(img_url).into(img_user)
        broadcastReceiver = object : BroadcastReceiver() {
            @RequiresApi(Build.VERSION_CODES.Q)
            override fun onReceive(context: Context, intent: Intent) {
                val id_chat_hide = intent.getStringExtra("id_chat_change")
                val message_change = intent.getStringExtra("message_change")
                var index = 0
                for(i in 0 until  binding.listMes.childCount){
                    var id_hide = binding.listMes.getChildAt(i).findViewById<TextView>(R.id.id_hide).text
                    Log.d("id_hide" , id_chat_hide.toString())
                    Log.d("id_hide_view" , id_hide.toString())
                    if(id_hide.equals(id_chat_hide)){
                        index = i
                        break
                    }
                }
                var view_change = binding.listMes.getChildAt(index)
                if(index != 0){
                    var status = view_change.findViewById<CardView>(R.id.status_dot)
                    status.visibility = View.VISIBLE
                    binding.listMes.removeView(view_change)
                    var mes_pre_change = view_change.findViewById<TextView>(R.id.mes_pre)
                    mes_pre_change.text = message_change
                    var text = SpannableString(mes_pre_change.text)
                    val spanbold = StyleSpan(Typeface.BOLD)
                    text.setSpan(spanbold, 0, text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    mes_pre_change.text = text
                     binding.listMes.addView(view_change, 0)
                }else{
                    var mes_pre_change = view_change.findViewById<TextView>(R.id.mes_pre)
                    var status = view_change.findViewById<CardView>(R.id.status_dot)
                    status.visibility = View.VISIBLE
                    mes_pre_change.text = message_change
                    var text = SpannableString(mes_pre_change.text)
                    val spanbold = StyleSpan(Typeface.BOLD)
                    text.setSpan(spanbold, 0, text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    mes_pre_change.text = text
                }
            }
        }
        val filter = IntentFilter("chat_update_data")
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, filter)
        var service = Intent(this, SocketService::class.java)
        startForegroundService(service)

        ButtonTool().getButton(R.id.chat_btn, this)?.setOnClickListener{
            findViewById<ScrollView>(R.id.wrap_mes_home).visibility = View.VISIBLE
            findViewById<ScrollView>(R.id.wrap_friends_home).visibility = View.GONE
            findViewById<ScrollView>(R.id.account_setting).visibility = View.GONE
            reloadChat()
        }

        ButtonTool().getButton(R.id.friend_btn, this)?.setOnClickListener{
            findViewById<ScrollView>(R.id.wrap_mes_home).visibility = View.GONE
            findViewById<ScrollView>(R.id.wrap_friends_home).visibility = View.VISIBLE
            findViewById<ScrollView>(R.id.account_setting).visibility = View.GONE
            reload()
        }

        ButtonTool().getButton(R.id.setting_btn, this)?.setOnClickListener{
            findViewById<ScrollView>(R.id.wrap_mes_home).visibility = View.GONE
            findViewById<ScrollView>(R.id.wrap_friends_home).visibility = View.GONE
            findViewById<ScrollView>(R.id.account_setting).visibility = View.VISIBLE
        }

        ButtonTool().getButton(R.id.logout_btn, this)?.setOnClickListener {

            val positiveButtonListener = DialogInterface.OnClickListener { dialog, which ->
                sendBroadcast(Intent(APP_ACTIVITY_ACTION_CHANGED).apply {
                    putExtra(EXTRA_ACTIVITY_STATE, STATE_LOGOUT)
                })
                val sharedPreferences: SharedPreferences = getSharedPreferences("privateData", Context.MODE_PRIVATE)
                sharedPreferences.edit().clear().apply()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()

            }
            val builder = AlertDialog.Builder(this)
            builder.setMessage("Do your wanna log out?")
            builder.setPositiveButton("Yes", positiveButtonListener)
            builder.setNegativeButton("No") { dialog, which ->
                // Dismiss the dialog if user clicks No
                dialog.dismiss()
            }
            val dialog = builder.create()
            dialog.show()

        }

        ButtonTool().getButton(R.id.change_usnamebtn, this)?.setOnClickListener {
            val intent = Intent(this, ChangeusnameActivity::class.java)
            startActivity(intent)
        }

        ButtonTool().getButton(R.id.change_pasbtn, this)?.setOnClickListener {
            val intent = Intent(this, ChangePassAcivity::class.java)
            startActivity(intent)
        }

        ButtonTool().getImageButton(R.id.change_img_btn, this)?.setOnClickListener {
            val intent = Intent(this, ChangeimgActivity::class.java)
            startActivity(intent)
        }
    }

    companion object {
        const val APP_ACTIVITY_ACTION_CHANGED = "app_change"
        const val EXTRA_ACTIVITY_STATE = "app_extra_state"
        const val STATE_STARTED = "app_stated"
        const val STATE_STOPPED = "app_stop"
        const val STATE_LOGOUT = "log_out"
    }

    override fun onResume() {
        super.onResume()
        reloadChat()
        reload()
        sendBroadcast(Intent(APP_ACTIVITY_ACTION_CHANGED).apply {
            putExtra(EXTRA_ACTIVITY_STATE, STATE_STARTED)
        })
    }

    fun reload(){
        findViewById<LinearLayout>(R.id.listFriend).removeAllViews()
        val sharedPreferences: SharedPreferences = getSharedPreferences("privateData", Context.MODE_PRIVATE)
        val id : Int = sharedPreferences.getInt("IDU", 0)
        val token : String? = sharedPreferences.getString("TOK", null)
        val name : String? = sharedPreferences.getString("NAME", null)
        getUserController().getUser(id, token.toString(),this)
    }

    fun reloadChat(){
        binding.listMes.removeAllViews()
        val sharedPreferences: SharedPreferences = getSharedPreferences("privateData", Context.MODE_PRIVATE)
        val id : Int = sharedPreferences.getInt("IDU", 0)
        val token : String? = sharedPreferences.getString("TOK", null)
        val name : String? = sharedPreferences.getString("NAME", null)
        findViewById<TextView>(R.id.username_view).text = name
        val img_url : String = Common().IMG_ROOT_URL + sharedPreferences.getString("IMG", "null")
        val img_user = findViewById<ImageView>(R.id.user_img_view)
        Picasso.get().load(img_url).into(img_user)
        chatController().getChat(id, token.toString(),this, binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onPause() {
        super.onPause()
        sendBroadcast(Intent(APP_ACTIVITY_ACTION_CHANGED).apply {
            putExtra(EXTRA_ACTIVITY_STATE, STATE_STOPPED)
        })
    }

}