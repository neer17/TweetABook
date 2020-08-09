package com.example.tweetabook.db.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.tweetabook.db.entities.TweetEntity

@Dao
interface TweetDAO {
    @Insert
    suspend fun insertTweet(tweetEntity: TweetEntity)

    @Query("SELECT * FROM tweets")
    fun getAllTweets(): LiveData<List<TweetEntity>?>

    @Update
    fun updateTweet(vararg tweets: TweetEntity): Int

    @Query("UPDATE tweets SET tweet = :tweet WHERE id = :id")
    fun updateTranslatedText(id: String, tweet: String): Int

    @Query("UPDATE tweets SET tweeted = :tweeted WHERE id = :id")
    fun updateTweeted(id: String, tweeted: Boolean): Int
}