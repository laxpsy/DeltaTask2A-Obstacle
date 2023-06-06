package com.example.nightdash

import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsetsController
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.google.android.material.button.MaterialButton
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val windowsInsetsController: WindowInsetsControllerCompat = WindowCompat.getInsetsController(window, window.decorView)

        windowsInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        windowsInsetsController.hide(WindowInsetsCompat.Type.systemBars())


        val playButton = findViewById<TextView>(R.id.playButton)
        val scoreText = findViewById<TextView>(R.id.scoreText)

        val sharedPreferences: SharedPreferences = getSharedPreferences("highScore", 0)
        val score: Int = sharedPreferences.getInt("score", 0)
        if(score!= 0) {
            scoreText.text = "Your score is $score"
        }
        playButton.setOnClickListener()
        {
            setContentView(GameView(this))
        }
    }
}