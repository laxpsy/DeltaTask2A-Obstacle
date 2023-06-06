package com.example.nightdash

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.Log
import java.lang.Math.random

class LObstacleSprite(bmp: Bitmap) {
    private val image: Bitmap = bmp
    private val screenWidth: Int = Resources.getSystem().displayMetrics.widthPixels
    private val screenHeight: Int = Resources.getSystem().displayMetrics.heightPixels
    public var x = screenWidth-image.width
    public val y = screenHeight-200-image.height
    private val masterList = variables()
    public var xVel = masterList.gameSpeed

    public fun draw(canvas: Canvas)
    {
        canvas.drawBitmap(image, ((x+500).toFloat()), (y).toFloat(), null)
    }

    public fun update()
    {
        x-=xVel
        if(x <= -600) {
            var i = (1..3).random()
            if(i==1)
                x = masterList.lObsSpawn1
            else if(i==2)
                x = masterList.lObsSpawn2
            else
                x = masterList.lObsSpawn3
        }
    }
}