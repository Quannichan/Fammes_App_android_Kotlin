package com.example.view

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView


class media_adapter(private val context: Context, private val dataList: List<String>) :
    RecyclerView.Adapter<media_adapter.ViewHolder>() {

    // ViewHolder holds references to the views
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.img_component1)
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.img_compo, parent, false)
        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dataItem = dataList[position]

        // Set data to views
        val bitmap = BitmapFactory.decodeFile(dataItem)
        holder.imageView.setImageBitmap(bitmap)
        val positiveButtonListener = DialogInterface.OnClickListener { dialog, which ->
            val file_accept : ArrayList<String> = arrayListOf("jpeg","png","jpg")
            if(file_accept.contains(getFileExtension(dataItem.lowercase()))){
                context.sendBroadcast(Intent(MessageActivity.MESSAGE_ACTIVITY_ACTION_CHANGED).apply {
                    MessageActivity.REQUEST = 2
                    MessageActivity.IMG_SEND = dataItem
                    putExtra(MessageActivity.EXTRA_ACTIVITY_STATE, MessageActivity.SEND_MESSAGE)
                    putExtra(MessageActivity.REQUEST_SEND, MessageActivity.REQUEST)
                    putExtra(MessageActivity.CHAT, MessageActivity.CHAT_ID)
                    putExtra(MessageActivity.SEND_IMG,MessageActivity.IMG_SEND)
                })
            }else{
                val builder = AlertDialog.Builder(context)
                builder.setMessage("Invalid type image!")
                builder.setNegativeButton("Ok") { dialog, which ->
                    dialog.dismiss()
                }
                val dialog = builder.create()
                dialog.show()
            }

        }
        holder.imageView.setOnClickListener{
            val builder = AlertDialog.Builder(context)
            builder.setMessage("Do your wanna send thís image?")
            builder.setPositiveButton("Yes", positiveButtonListener)
            builder.setNegativeButton("No") { dialog, which ->
                // Dismiss the dialog if user clicks No
                dialog.dismiss()
            }
            val dialog = builder.create()
            dialog.show()

        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount(): Int {
        return dataList.size
    }

    fun getFileExtension(filePath: String): String {
        // Lấy phần đuôi của tên tệp
        return filePath.substringAfterLast('.', "")
    }
}