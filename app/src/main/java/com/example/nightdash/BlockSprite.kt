package com.example.nightdash

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.Log

class BlockSprite(bmp: Bitmap) {
    public enum class state
    {
        Walking, Jump, DJump, Exhausted
    }
    public var currentState = state.Walking
    private val image: Bitmap = bmp
    public var x: Int = 0

    private val screenHeight: Int = Resources.getSystem().displayMetrics.heightPixels
    public var y: Int = screenHeight-300
    private val masterList = variables()
    public var yVel: Int = masterList.gravity
    public val xVel: Int = masterList.blockXSpeed

    public fun draw(canvas: Canvas)
    {
        canvas.drawBitmap(image, x.toFloat(),y.toFloat(), null)
    }

    public fun update()
    {
        if(y<=(masterList.groundLevel)) {
            y += yVel
        }
        if(y>=(masterList.groundLevel))
        {
            currentState = state.Walking
            if(x>=0) {
                x -= masterList.gameSpeed
            }
        }

        if(y<=masterList.singleJumpHeight && currentState == state.Jump) {
            yVel = masterList.gravity
            currentState = state.DJump
        }
        if(currentState == state.Jump)
        {
            x+=xVel
        }
        if(y<=masterList.doubleJumpHeight && currentState == state.Exhausted)
        {
            yVel=masterList.gravity
        }

    }

}