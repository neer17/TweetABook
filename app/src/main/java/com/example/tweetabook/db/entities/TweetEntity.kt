package com.example.tweetabook.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tweets")
data class TweetEntity(
    @PrimaryKey(autoGenerate = false)
    var id: String,
    val imageUri: String,
    var tweet: String? = null,
    var tweeted: Boolean = false
) {


    override fun toString(): String {
        return "TweetsEntity values: id: $id  \t tweet: $tweet"
    }
}