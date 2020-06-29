package com.example.tweetabook.api

import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Streaming

interface MyBackendApi {
    @Headers("Content-Type: application/json")
    @POST("/downloadUrl")
    @Streaming
    fun sendDownloadUrl(@Body uri: JsonObject): Call<ResponseBody>
}