package com.sonmob.chatroom

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.os.HandlerCompat.postDelayed
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import com.sonmob.chatroom.LocalDatabase.Preferences
import com.sonmob.chatroom.View.HomeActivity
import com.sonmob.chatroom.databinding.ActivityLoginScreenBinding
import com.sonmob.chatroom.model.SignupModel


class LoginScreenActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var database: FirebaseDatabase
    lateinit var databaseReference: DatabaseReference
    lateinit var preferences: Preferences
    private lateinit var binding: ActivityLoginScreenBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginScreenBinding.inflate(layoutInflater)
        val view = binding.root
        window.statusBarColor = ContextCompat.getColor(applicationContext, R.color.white)


        binding.tvAllreadyAccount.setOnClickListener(this)
        binding.tvNewAccount.setOnClickListener(this)
        binding.btnLogin.setOnClickListener(this)
        binding.btnSignup.setOnClickListener(this)

        database = FirebaseDatabase.getInstance()
        databaseReference = database.getReference("ChatRoom").child("users")

        preferences = Preferences(applicationContext)
        if (preferences.getData("id") != "") {
            startActivity(Intent(applicationContext, HomeActivity::class.java))
            finish()
        }
        setContentView(view)
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.tvAllreadyAccount -> {
                binding.loginLayout.visibility = View.VISIBLE
                binding.signupLayout.visibility = View.GONE
            }
            binding.tvNewAccount -> {
                binding.loginLayout.visibility = View.GONE
                binding.signupLayout.visibility = View.VISIBLE
            }
            binding.btnLogin -> {
                login()
            }
            binding.btnSignup -> {
                signup()
            }
        }
    }

    private fun isEmailExist(email: String, password: String) {
        // Read from the database
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                var list = ArrayList<SignupModel>()
                var isemailexist = false

                for (postsnapshot in dataSnapshot.children) {
                    var value = postsnapshot.getValue<SignupModel>()

                    if (value!!.email == email && value.password == password) {
                        isemailexist = true
                        //get Preferences
                        preferences.saveData("name", value.name)
                        preferences.saveData("email", value.email)
                        preferences.saveData("id", value.id)

                    }
                    list.add(value)
                }

                if (isemailexist) {
                    showToast("Login successfull")
                    startActivity(Intent(applicationContext, HomeActivity::class.java))
                    finish()
                } else {
                    showToast("Login failed!! check your email and password")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                showToast(error.toString())
            }
        })
    }

    @Suppress("DEPRECATION")
    private fun login() {
        val email = binding.emailLogin.text.toString().trim()
        val password = binding.passwordLogin.text.toString().trim()

        val progressDialog = ProgressDialog(this@LoginScreenActivity)
        progressDialog.setMessage("Loading ...")
        progressDialog.setCancelable(true)
        progressDialog.show()

        if (email.isEmpty() || password.isEmpty()) {
            progressDialog.dismiss()
            showToast("All fields required")
        } else {
            if (isValidEmail(email)) {
                Handler().postDelayed({
                    isEmailExist(email, password)
                    progressDialog.dismiss()
                }, 2000)

            } else {
                progressDialog.dismiss()
                showToast("check your email address")
            }
        }
    }

    @Suppress("DEPRECATION")
    private fun signup() {
        val name = binding.nameSignup.text.toString().trim()
        val email = binding.emailSignup.text.toString().trim()
        val password = binding.passwordSignup.text.toString().trim()
        val confirm_password = binding.confirmPasswordSignup.text.toString().trim()

        val progressDialog = ProgressDialog(this@LoginScreenActivity)
        progressDialog.setMessage("Loading ...")
        progressDialog.setCancelable(true)
        progressDialog.show()

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirm_password.isEmpty()) {
            showToast("All fields required")
            progressDialog.dismiss()
        } else {

            if (isValidEmail(email)) {
                var id = databaseReference.push().key
                var model = SignupModel(name, email, password, id!!)

                //here data inserted
                databaseReference.child(id).setValue(model)
                //showToast("Signup successfull")

                //save Preferences
                preferences.saveData("name", name)
                preferences.saveData("email", email)
                preferences.saveData("id", id)

                Handler().postDelayed({
                    startActivity(Intent(applicationContext, HomeActivity::class.java))
                    showToast("Signup successfull")
                    finish()
                    progressDialog.dismiss()
                }, 2000)

//                startActivity(Intent(applicationContext, HomeActivity::class.java))
//                finish()
            } else {
                showToast("check your email address")
                progressDialog.dismiss()
            }
        }
    }

    private fun isValidEmail(email: String): Boolean {
        val pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(email).matches()
    }

    private fun showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }
}