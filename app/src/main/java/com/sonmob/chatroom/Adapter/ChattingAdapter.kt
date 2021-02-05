package com.sonmob.chatroom.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sonmob.chatroom.R
import com.sonmob.chatroom.databinding.LayoutChatBinding
import com.sonmob.chatroom.databinding.LayoutUserBinding
import com.sonmob.chatroom.model.MessageModel
import com.sonmob.chatroom.model.SignupModel

class ChattingAdapter(
    var context: Context,
    var list: ArrayList<MessageModel>,
    var user_id: String
) : RecyclerView.Adapter<ChattingAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.layout_chat, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (list[position].sender_id.equals(user_id)) {
            holder.senderLayout.visibility = View.VISIBLE
            holder.recieveLayout.visibility = View.GONE
            holder.senderTextName.text = list[position].message
            holder.senderTextTime.text = list[position].datetime
        } else {
            holder.senderLayout.visibility = View.GONE
            holder.recieveLayout.visibility = View.VISIBLE
            holder.recieveTextName.text = list[position].message
            holder.recieveTextTime.text = list[position].datetime
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //TODO: BINDING ViewHolder
        val binding = LayoutChatBinding.bind(itemView)

        var senderLayout = binding.senderLayout
        var senderTextName = binding.senderTextName
        var senderTextTime = binding.senderTextTime

        var recieveLayout = binding.recieveLayout
        var recieveTextName = binding.recieveTextName
        var recieveTextTime = binding.recieveTextTime

    }
}