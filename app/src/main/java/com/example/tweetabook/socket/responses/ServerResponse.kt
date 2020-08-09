package com.example.tweetabook.socket.responses

data class ServerResponse(
    val id: String,
    val progress: Double = 0.0,
    val status: String,
    val tweet: String? = null
)