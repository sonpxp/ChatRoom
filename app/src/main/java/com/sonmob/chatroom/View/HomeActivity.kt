package com.sonmob.chatroom.View

import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.sonmob.chatroom.Adapter.UserListAdapter
import com.sonmob.chatroom.LocalDatabase.Preferences
import com.sonmob.chatroom.R
import com.sonmob.chatroom.databinding.ActivityHomeBinding
import com.sonmob.chatroom.model.SignupModel

class HomeActivity : AppCompatActivity() {

    lateinit var database: FirebaseDatabase
    lateinit var databaseReference: DatabaseReference
    lateinit var preferences: Preferences
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        val view = binding.root

        preferences = Preferences(applicationContext)
        database = FirebaseDatabase.getInstance()
        databaseReference = database.getReference("ChatRoom").child("users")
        getUserList()

        setContentView(view)
    }

    //get user list here
    private fun getUserList() {
        // Read from the database
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val list = ArrayList<SignupModel>()

                for (postsnapshot in dataSnapshot.children) {
                    var value = postsnapshot.getValue(SignupModel::class.java)

                    if (value!!.email != preferences.getData("email")) {
                        list.add(value)
                    }
                }
                var adapterUser = UserListAdapter(applicationContext, list)
                binding.recyclerView.adapter = adapterUser
                //SET line addItemDecoration //no
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