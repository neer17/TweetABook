package com.example.tweetabook.screens.main.adapter

data class AdapterDataClass(
    var id: String,
    var imageUri: String,
    var progress: Double = 0.0,
    var tweeted: Boolean = false,
    var tweet: String? = null
) {
    override fun equals(other: Any?): Boolean {
        return id == (other as AdapterDataClass).id
    }
}