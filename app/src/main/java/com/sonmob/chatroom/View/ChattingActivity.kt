package com.sonmob.chatroom.View

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.sonmob.chatroom.Adapter.ChattingAdapter
import com.sonmob.chatroom.LocalDatabase.Preferences
import com.sonmob.chatroom.databinding.ActivityChattingBinding
import com.sonmob.chatroom.model.MessageModel
import java.text.SimpleDateFormat
import java.util.*

class ChattingActivity : AppCompatActivity() {
    lateinit var database: FirebaseDatabase
    lateinit var databaseReference: DatabaseReference
    lateinit var preferences: Preferences
    private lateinit var binding: ActivityChattingBinding
    var reciver_id = ""
    var user_id = ""
    var name = ""
    var email = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChattingBinding.inflate(layoutInflater)
        val view = binding.root

        preferences = Preferences(applicationContext)
        user_id = preferences.getData("id")

        val bundle = intent.extras
        reciver_id = bundle!!.getString("id", "")
        name = bundle.getString("name", "")
        email = bundle.getString("email", "")


        database = FirebaseDatabase.getInstance()
        databaseReference = database.getReference("ChatRoom").child("chattings")

        binding.btnSend.setOnClickListener {
            var message = binding.edMessage.text.toString().trim()
            if (message.isNotEmpty()) {
                sendMessage(message)
                binding.edMessage.setText("")
            } else {
                showToast("Please Enter Message")
            }
        }

        getChatMessage()

        setContentView(view)
    }

    private fun sendMessage(message: String) {
        val model = MessageModel(message, user_id, reciver_id, getDateTime())
        val message_id = databaseReference.push().key
        databaseReference.child(user_id).child(reciver_id).child(message_id!!).setValue(model)
        databaseReference.child(reciver_id).child(user_id).child(message_id).setValue(model)
    }

    @SuppressLint("SimpleDateFormat")
    private fun getDateTime(): String {
        val sdf = SimpleDateFormat("dd/MM hh:mm aaa")
        val currentDate = sdf.format(Date())
        return currentDate
    }

    private fun getChatMessage(){
        var reference = database.getReference("ChatRoom").child("chattings").child(user_id).child(reciver_id)
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val list = ArrayList<MessageModel>()

                for (postsnapshot in dataSnapshot.children) {
                    var value = postsnapshot.getValue(MessageModel::class.java)

                   list.add(value!!)
                }
                var adapterChat = ChattingAdapter(applicationContext, list, user_id)
                var manager =LinearLayoutManager(applicationContext)
                manager.stackFromEnd = true
                binding.recyclerViewChats.layoutManager = manager
                binding.recyclerViewChats.adapter = adapterChat

            }

            override fun onCancelled(error: DatabaseError) {
                showToast(error.toString())
            }
        })
    }
    private fun showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }
}