package com.example.nightdash.retrofitcoil

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface tipsAPI {
    @GET("/tip")
    suspend fun getTips(): Response<TipDataClass>

    @POST("/character")
    suspend fun postCharacter(@Body charReq: characterRequest): Response<characterParentDataClass>

    @GET("/scores")
    suspend fun getScores(): Response<scoresParentDataClass>


}
