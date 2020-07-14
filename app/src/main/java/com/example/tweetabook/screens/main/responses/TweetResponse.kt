package com.example.tweetabook.screens.main.responses

import android.net.Uri

data class TweetResponse(var id: String, var  imageUri: Uri, var  progress: Double = 0.0, var tweet: String? = null)