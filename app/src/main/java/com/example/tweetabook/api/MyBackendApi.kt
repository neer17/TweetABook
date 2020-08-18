package com.example.tweetabook.api

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface MyBackendApi {
    @POST("/tweet")
    fun sendTweet(@Body tweet: JsonObject): Call<Boolean>
}