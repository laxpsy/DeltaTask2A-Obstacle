package com.example.nightdash

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.os.Looper
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.animation.TranslateAnimation
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import java.util.Timer
import java.util.TimerTask
import java.util.logging.Handler
import kotlin.system.exitProcess


class GameView: SurfaceView, SurfaceHolder.Callback {
    private var thread: MainThread
    private lateinit var blocksprite: BlockSprite
    private lateinit var sObstacleSprite: SObstacleSprite
    private lateinit var lObstacleSprite: LObstacleSprite
    private lateinit var groundSprite: GroundSprite
    private lateinit var animation: TranslateAnimation
    private val masterList = variables()
    var score: Int = 0
    var collided: Boolean = false
    var firstCollided: Boolean = false
    val paint = Paint()
    val timer = Timer()
    val increaseSpeed = object: TimerTask() {
        override fun run() {
            sObstacleSprite.xVel+=1
            lObstacleSprite.xVel+=1
            println("Speed Increased to ${masterList.gameSpeed}")
        }
    }
    val sharedPreferences: SharedPreferences = context.getSharedPreferences("highScore", 0)
    val editor = sharedPreferences.edit()
    var concluded = false




    public val groundHeight = 200


    constructor(context: Context) : super(context) {
        holder.addCallback(this)
        thread = MainThread(holder, this)
        isFocusable = true
        paint.setColor(Color.WHITE)
        paint.textSize = 80F
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        thread.setRunning(true)
        thread.start()
        blocksprite = BlockSprite(resizeBitmaps((BitmapFactory.decodeResource(resources, R.drawable.blocksprite)), masterList.squareSize, masterList.squareSize))
        sObstacleSprite = SObstacleSprite(resizeBitmaps((BitmapFactory.decodeResource(resources, R.drawable.sobstaclesprite)), masterList.squareSize, masterList.sObstacleHeight))
        lObstacleSprite = LObstacleSprite(resizeBitmaps((BitmapFactory.decodeResource(resources, R.drawable.lobstaclesprite)), masterList.squareSize, masterList.lObstacleHeight))
        groundSprite = GroundSprite(resizeBitmaps((BitmapFactory.decodeResource(resources, R.drawable.groundsprite)), Resources.getSystem().displayMetrics.widthPixels, groundHeight))
        score = 0
        timer.scheduleAtFixedRate(increaseSpeed, 0, 5000)
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        var retry: Boolean = true
        while (retry) {
            try {
                thread.setRunning(false)
                thread.join()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            retry = false
        }
    }

    public fun update() {

        blocksprite.update()
        sObstacleSprite.update()
        lObstacleSprite.update()
        gameLogic()

    }

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)
        if(canvas != null)
        {
            canvas.drawColor(resources.getColor(R.color.black))
           blocksprite.draw(canvas)
            groundSprite.draw(canvas)
            sObstacleSprite.draw(canvas)
            lObstacleSprite.draw(canvas)
            canvas.drawText("$score", masterList.screenWidth.toFloat()-200, 100F, paint)
        }
    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {
            if(blocksprite.currentState== BlockSprite.state.Walking && !concluded) {
                blocksprite.y -= 20
                blocksprite.yVel = masterList.jumpSpeed
                blocksprite.currentState = BlockSprite.state.Jump
            }

        if(blocksprite.currentState == BlockSprite.state.DJump) {
            blocksprite.y -= 20
            blocksprite.yVel = masterList.jumpSpeed
            blocksprite.currentState = BlockSprite.state.Exhausted
        }

        return super.onTouchEvent(event)
    }

    fun resizeBitmaps(bitmap: Bitmap, newWidth: Int, newHeight: Int): Bitmap
    {
        val width = bitmap.width
        val height = bitmap.height

        val scaleWidth = (newWidth.toFloat()/width)
        val scaleHeight = (newHeight.toFloat()/height)

        val matrix: Matrix = Matrix()
        matrix.postScale(scaleWidth, scaleHeight)
        val resizedBitmap: Bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false)
        bitmap.recycle()

        return resizedBitmap

    }

    fun gameLogic()
    {

        //collision detection
        if(blocksprite.x > sObstacleSprite.x  && blocksprite.x < sObstacleSprite.x + masterList.squareSize && blocksprite.y > sObstacleSprite.y - masterList.sObstacleHeight + 100)
        {
            if(firstCollided) {
                collided = true
            }

            if(!firstCollided && !concluded) {
                println("Collision registered")
                sObstacleSprite.x = -1523
                lObstacleSprite.x = -2000

                firstCollided = true

                collided = false

                if(score>300)
                    score-=300
                else
                    score=0

                showToast("Penalty!", 1)
            }


        }
        if(blocksprite.x > lObstacleSprite.x + 450  && blocksprite.x < lObstacleSprite.x +450+ masterList.squareSize && blocksprite.y > lObstacleSprite.y - masterList.lObstacleHeight + 300 )
        {
            collided = true
        }

        //scoring system
        if(!collided) {
            score += 1
        }
        if(collided && !concluded)
        {
            concluded = true
            showToast("Game Over! Your score is $score", 2)
            if(score > sharedPreferences.getInt("score", 0)) {
                editor.putInt("score", score)
                editor.apply()
            }
            timer.cancel()
            sObstacleSprite.xVel = 0
            lObstacleSprite.xVel = 0
           // thread.onLose()
        }
    }

    fun showToast(text: String, id: Int)
    {
        val handler: android.os.Handler = android.os.Handler(Looper.getMainLooper())
        handler.post(object: Runnable
        {
            override fun run() {
                if(id == 1)
                    Toast.makeText(context, text, Toast.LENGTH_SHORT).show()

                if(id == 2)
                    Toast.makeText(context, text, Toast.LENGTH_LONG).show()
            }
        })
    }
}