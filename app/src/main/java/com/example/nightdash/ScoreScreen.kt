package com.example.nightdash

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nightdash.databinding.ActivityMainBinding
import com.example.nightdash.databinding.ScoreScreenBinding
import com.example.nightdash.recyclerview.scoreAdapter
import com.example.nightdash.retrofitcoil.retrofitInstance
import com.example.nightdash.retrofitcoil.scoreDataClass
import com.example.nightdash.retrofitcoil.scoresParentDataClass
import com.example.nightdash.retrofitcoil.tipsAPI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit
import java.net.UnknownHostException
import java.text.DecimalFormat

class ScoreScreen : AppCompatActivity(){


    private lateinit var scoreAdapter: scoreAdapter
    lateinit var binding: ScoreScreenBinding
    var listScores: List<scoreDataClass> = listOf(scoreDataClass("SomeError", 0F))
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ScoreScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreferences: SharedPreferences = getSharedPreferences("highScore", 0)
        var scoreTotal: Float = sharedPreferences.getFloat("temp", 0F)
        val dec = DecimalFormat("#.###")
        scoreTotal = dec.format(scoreTotal).toFloat()
        setupRecyclerView()

        val textView = binding.mainMenu
        textView.setOnClickListener{
            overridePendingTransition(androidx.appcompat.R.anim.abc_fade_in, androidx.appcompat.R.anim.abc_fade_out)
            Intent(this, MainActivity::class.java).apply { startActivity(this) }
        }

        val windowsInsetsController: WindowInsetsControllerCompat = WindowCompat.getInsetsController(window, window.decorView)

        windowsInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        windowsInsetsController.hide(WindowInsetsCompat.Type.systemBars())


        val tipAPI = retrofitInstance.getInstance().create(tipsAPI::class.java)

        var scores: Response<scoresParentDataClass>


        CoroutineScope(Dispatchers.IO).launch {
            try {
                scores = tipAPI.getScores()
                if(scores.body() != null)
                {
                    listScores = scores.body()!!.scores.sortedByDescending { it.score }
                    val amendedList = listScores.toMutableList()
                    amendedList.add(scoreDataClass(android.os.Build.MODEL.toString(), scoreTotal))
                    listScores = amendedList.sortedByDescending { it.score }
                }
                else {
                    println("Null Exception - ScoreBody is null")
                }

                runOnUiThread {  setupRecyclerView()}
            }
            catch(e: UnknownHostException) {
                println("InternetDead")
            }
        }

    }

    private fun setupRecyclerView() = binding.rvScores.apply {
        scoreAdapter = scoreAdapter(listScores)
        adapter = scoreAdapter
        layoutManager = LinearLayoutManager(this@ScoreScreen)
    }




}