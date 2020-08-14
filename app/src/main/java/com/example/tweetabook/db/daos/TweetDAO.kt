package com.example.tweetabook.db.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.tweetabook.db.entities.TweetEntity

@Dao
interface TweetDAO {
    @Insert
    suspend fun insertTweet(tweetEntity: TweetEntity)

    @Query("SELECT * FROM tweets")
    fun getAllTweets(): LiveData<List<TweetEntity>?>

    @Query("SELECT * FROM tweets")
    fun getAllTweetsWithOutLiveData(): List<TweetEntity>?

    @Query("SELECT * FROM tweets WHERE id = :id")
    fun getTweetById(id: String): List<TweetEntity>?

    @Update
    fun updateTweet(vararg tweets: TweetEntity): Int

    @Delete
    fun deleteAll(tweets: List<TweetEntity>): Int
}