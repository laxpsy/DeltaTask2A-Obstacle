package com.example.nightdash

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.nightdash.retrofitcoil.retrofitInstance
import com.example.nightdash.retrofitcoil.tipsAPI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.UnknownHostException

class TipsSplashScreen : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tips_splash_screen)

        val windowsInsetsController: WindowInsetsControllerCompat = WindowCompat.getInsetsController(window, window.decorView)

        windowsInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        windowsInsetsController.hide(WindowInsetsCompat.Type.systemBars())

        val tipAPI = retrofitInstance.getInstance().create(tipsAPI::class.java)
        var tipText: String? = null
        val tipTextView: TextView = findViewById<TextView>(R.id.tipText)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                tipText = tipAPI.getTips().body()?.tip
                runOnUiThread{
                    tipTextView.text = tipText
                    }
                delay(3000)
                Intent(applicationContext, MainActivity::class.java).apply {
                    overridePendingTransition(androidx.appcompat.R.anim.abc_fade_in, androidx.appcompat.R.anim.abc_fade_out)
                    startActivity(this)
                }
            }
            catch(e: UnknownHostException)
            {
                tipText = "Internet Connection Failed"
                runOnUiThread{
                    tipTextView.text = tipText
                }
                delay(1500)
                Intent(applicationContext, MainActivity::class.java).apply {
                    overridePendingTransition(androidx.appcompat.R.anim.abc_fade_in, androidx.appcompat.R.anim.abc_fade_out)
                    startActivity(this)
                }
            }

        }


        val content: View = findViewById(android.R.id.content)
        content.viewTreeObserver.addOnPreDrawListener(
            object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    return if (tipText != null) {
                        content.viewTreeObserver.removeOnPreDrawListener(this)
                        true
                    } else {
                        false
                    }
                }
            }
        )


    }
}