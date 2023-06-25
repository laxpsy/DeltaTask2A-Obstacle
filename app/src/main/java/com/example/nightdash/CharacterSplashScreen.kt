package com.example.nightdash

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import coil.load
import com.example.nightdash.retrofitcoil.characterRequest
import com.example.nightdash.retrofitcoil.retrofitInstance
import com.example.nightdash.retrofitcoil.tipsAPI
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.net.UnknownHostException


class CharacterSplashScreen: AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.character_splash_screen)

        val charNameText: TextView = findViewById<TextView>(R.id.charNameText)
        val charDesText: TextView = findViewById<TextView>(R.id.charDesText)
        val charImage: ImageView = findViewById<ImageView>(R.id.charImage)


        val tipAPI = retrofitInstance.getInstance().create(tipsAPI::class.java)

        val windowsInsetsController: WindowInsetsControllerCompat = WindowCompat.getInsetsController(window, window.decorView)

        var nameText: String = "demoString"
        var desText: String = "demoString"
        var charImageUrl: String = "demoString"
        windowsInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        windowsInsetsController.hide(WindowInsetsCompat.Type.systemBars())

        CoroutineScope(Dispatchers.IO ).launch {
            try {

                nameText = tipAPI.postCharacter(characterRequest("player")).body()!!.character.name
                desText = tipAPI.postCharacter(characterRequest("player")).body()!!.character.description
                charImageUrl = tipAPI.postCharacter(characterRequest("player")).body()!!.character.imageUrl
                runOnUiThread { charNameText.text = nameText
                    charDesText.text = desText
                    charImage.load(charImageUrl)}
                    delay(5500)
                    runOnUiThread { setContentView(GameViewParent().GameView(applicationContext)) }

            }
            catch (e:UnknownHostException)
            {

                    runOnUiThread {
                        charNameText.text = "NO INTERNET"
                        charDesText.text = "NO INTERNET"
                        charImage.setImageResource(R.mipmap.steelgreyic)
                    }
                    delay(2500)
                    runOnUiThread { setContentView(GameViewParent().GameView(applicationContext)) }

            }

        }
    }
}