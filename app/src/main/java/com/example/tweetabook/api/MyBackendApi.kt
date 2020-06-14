package com.example.tweetabook.api

import com.example.tweetabook.api.responses.BackendResponse
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface MyBackendApi {
    @Headers("Content-Type: application/json")
    @POST("/downloadUrl")
    fun sendDownloadUrl(@Body uri: JsonObject): Call<BackendResponse>
}