package com.sonmob.chatroom

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getColor

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    var handler = Handler()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        window.statusBarColor = getColor(applicationContext, R.color.white)

        // we used the postDelayed(Runnable, time) method
        // to send a message with a delayed time.
        handler.postDelayed({
            val intent = Intent(this@MainActivity, LoginScreenActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000)
    }

    override fun onDestroy() {
        handler.removeCallbacksAndMessages(null)
        super.onDestroy()
    }
}