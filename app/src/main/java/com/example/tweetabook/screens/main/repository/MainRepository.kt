package com.example.tweetabook.screens.main.repository

import androidx.lifecycle.LiveData
import com.example.tweetabook.socket.responses.ServerResponse
import com.google.gson.JsonObject

interface MainRepository {
    fun addJob(job: Jobs)
    fun removeJob()
    suspend fun executeJob(job: Jobs)

    suspend fun getFilesCount(): Int?
    suspend fun emptyStorage()
    suspend fun deleteAllTweets()

    fun socketEmitEvent(json: JsonObject)

    fun exposeServerResponse(): LiveData<ServerResponse>
    fun exposeJobList(): LiveData<ArrayList<Jobs>>
    fun exposeAnyPendingJobs(): Boolean
}