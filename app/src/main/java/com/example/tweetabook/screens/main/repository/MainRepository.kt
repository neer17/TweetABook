package com.example.tweetabook.screens.main.repository

import androidx.lifecycle.LiveData
import com.example.tweetabook.socket.responses.ServerResponse
import com.google.gson.JsonObject

interface MainRepository {
    suspend fun registerJobAndExecuteOnNetworkPresent(
        id: String,
        localImageUri: String
    )

    suspend fun uploadToStorageAndTranslate()

    fun translateImageToUrl(id: String, downloadUri: String)
    suspend fun getFilesCount(): Int?
    suspend fun deleteAll()
    fun socketEmitEvent(json: JsonObject)
    fun socketResponse(): LiveData<ServerResponse>

    fun exposeJobList(): ArrayList<DefaultMainRepository.MyJob>
}