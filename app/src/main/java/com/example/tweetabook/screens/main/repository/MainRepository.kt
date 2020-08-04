package com.example.tweetabook.screens.main.repository

import android.net.Uri
import androidx.lifecycle.LiveData
import com.example.tweetabook.socket.responses.ServerResponse
import com.google.gson.JsonObject

interface MainRepository {
    suspend fun uploadedImageAndSendDownloadUriToServer(
        id: String,
        fileUri: Uri
    )

    fun sendDownloadUrlToServer(id: String, downloadUri: Uri)
    suspend fun getFilesCount(): Int?
    suspend fun deleteAll()
    fun socketEmitEvent(json: JsonObject)
    fun socketResponse(): LiveData<ServerResponse>
}