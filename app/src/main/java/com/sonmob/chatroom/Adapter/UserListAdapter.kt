package com.sonmob.chatroom.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sonmob.chatroom.R
import com.sonmob.chatroom.View.ChattingActivity
import com.sonmob.chatroom.databinding.LayoutUserBinding
import com.sonmob.chatroom.model.SignupModel

class UserListAdapter(var context: Context, var list: ArrayList<SignupModel>) :
    RecyclerView.Adapter<UserListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.layout_user, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textName.text = list[position].name
        holder.textEmail.text = list[position].email
        holder.itemView.setOnClickListener {
            val intent = Intent(context, ChattingActivity::class.java)
            //TODO:FIX FLAG_ACTIVITY_NEW_TASK
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra("id", list[position].id)
            intent.putExtra("email", list[position].email)
            intent.putExtra("name", list[position].name)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = LayoutUserBinding.bind(itemView)
        var textName = binding.tvName
        var textEmail = binding.tvEmail
    }
}