package com.example.nightdash

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.Log

class GroundSprite(bmp:Bitmap) {
    private val image: Bitmap = bmp
    private val screenHeight: Int = Resources.getSystem().displayMetrics.heightPixels

    public fun draw(canvas: Canvas)
    {
        canvas.drawBitmap(image,0F,screenHeight.toFloat()-image.height , null)
    }

    public fun update()
    {

    }

}