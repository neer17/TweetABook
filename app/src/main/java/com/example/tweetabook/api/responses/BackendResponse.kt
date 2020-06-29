package com.example.tweetabook.api.responses

import com.google.gson.annotations.SerializedName

data class BackendResponse(
    @SerializedName("response")
    val response: String) {
}