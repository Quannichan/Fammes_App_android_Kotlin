package com.example.view

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import java.io.File

class media_adapterType_video (private val context: Context, private val dataList: List<String>) :
    RecyclerView.Adapter<media_adapterType_video.ViewHolder>() {

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
        var thumb = getThumbnail(dataItem)
        holder.imageView.setImageBitmap(thumb)
        val positiveButtonListener = DialogInterface.OnClickListener { dialog, which ->
            var file = File(dataItem)
//            if(file.length() <= 10000000){
                context.sendBroadcast(Intent(MessageActivity.MESSAGE_ACTIVITY_ACTION_CHANGED).apply {
                    MessageActivity.REQUEST = 3
                    MessageActivity.VID_SEND = dataItem
                    putExtra(MessageActivity.EXTRA_ACTIVITY_STATE, MessageActivity.SEND_MESSAGE)
                    putExtra(MessageActivity.REQUEST_SEND, MessageActivity.REQUEST)
                    putExtra(MessageActivity.CHAT, MessageActivity.CHAT_ID)
                    putExtra(MessageActivity.SEND_VIDEO,MessageActivity.VID_SEND)
                })
//            }else{
//                val builder = AlertDialog.Builder(context)
//                builder.setMessage("Video is too big, try another!")
//                builder.setNegativeButton("Ok") { dialog, which ->
//                    dialog.dismiss()
//                }
//                val dialog = builder.create()
//                dialog.show()
//            }

        }
        holder.imageView.setOnClickListener{
            val builder = AlertDialog.Builder(context)
            builder.setMessage("Do your wanna send thís video?")
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

    private fun getThumbnail(videoPath: String): Bitmap? {
        val retriever = MediaMetadataRetriever()
        retriever.setDataSource(videoPath)

        // Lấy hình ảnh đại diện từ video
        val bitmap: Bitmap?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            bitmap = retriever.getScaledFrameAtTime(
                0,
                MediaMetadataRetriever.OPTION_CLOSEST_SYNC,
                200,
                200
            )
        } else {
            bitmap = retriever.frameAtTime
        }
        retriever.release()
        return bitmap
    }
}