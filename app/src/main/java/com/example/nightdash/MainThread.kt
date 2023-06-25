package com.example.nightdash

import android.app.Activity
import android.graphics.Canvas
import android.view.SurfaceHolder
import kotlin.Exception

class MainThread : Thread
{
    private lateinit var surfaceHolder: SurfaceHolder
    private lateinit var gameView: GameViewParent.GameView
    private var running: Boolean = true
    private var canvas: Canvas? = null

    constructor(surfaceHolder: SurfaceHolder, gameView: GameViewParent.GameView) : super()
    {
        this.surfaceHolder = surfaceHolder
        this.gameView = gameView
    }

    override fun run() {
        while(running) {
            canvas = null

            try{
                canvas = this.surfaceHolder.lockCanvas()
                synchronized(surfaceHolder){
                    this.gameView.update()
                    this.gameView.draw(canvas)
                }
            }
            catch (e: Exception) {} finally {
                if (canvas != null)
                    try{
                        surfaceHolder.unlockCanvasAndPost(canvas)
                    } catch (e: Exception)
                    {
                        e.printStackTrace()
                    }
            }
        }
    }

    public fun setRunning(isRunning: Boolean)
    {
        running = isRunning
    }


}