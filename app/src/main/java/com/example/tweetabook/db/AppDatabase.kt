package com.example.tweetabook.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.tweetabook.db.daos.TweetDAO
import com.example.tweetabook.db.entities.TweetEntity


@Database(entities = [TweetEntity::class], version = 2, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {

    abstract fun getTweetDao(): TweetDAO

    companion object {
        val DATABASE_NAME = "tweetABook_db"
    }
}