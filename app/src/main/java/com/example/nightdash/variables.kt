package com.example.nightdash

import android.content.res.Resources
import android.os.Handler

class variables {
    public val screenHeight: Int = Resources.getSystem().displayMetrics.heightPixels
    public val screenWidth: Int = Resources.getSystem().displayMetrics.widthPixels

    public var gameSpeed: Int = 9
    public var singleJumpHeight: Int = screenHeight - 625
    public var groundLevel: Int = screenHeight - 300
    public var doubleJumpHeight: Int = singleJumpHeight-150
    public var sObsSpawn1: Int = screenWidth + 1*screenWidth/300
    public var sObsSpawn2: Int = screenWidth + 6*screenWidth/300
    public var sObsSpawn3: Int = screenWidth + 11*screenWidth/300
    public var lObsSpawn1: Int = screenWidth + 3*screenWidth/300
    public var lObsSpawn2: Int = screenWidth + 8*screenWidth/300
    public var lObsSpawn3: Int = screenWidth + 13*screenWidth/300
    public val sObstacleHeight = 200
    public val lObstacleHeight = 300
    public val squareSize = 100
    public var gravity = 10
    public var jumpSpeed = -15
    public val blockXSpeed: Int = 10

}