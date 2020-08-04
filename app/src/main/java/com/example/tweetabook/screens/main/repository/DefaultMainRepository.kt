package com.example.tweetabook.screens.main.repository

import android.net.Uri
import androidx.lifecycle.LiveData
import com.example.tweetabook.firebase.deleteAllFiles
import com.example.tweetabook.firebase.filesCount
import com.example.tweetabook.firebase.uploadFile
import com.example.tweetabook.socket.MySocket
import com.example.tweetabook.socket.responses.ServerResponse
import com.google.gson.JsonObject
import javax.inject.Inject


class DefaultMainRepository
@Inject
constructor(
    val mySocket: MySocket
): MainRepository {
    private val TAG = "AppDebug: MainRepository"

    override suspend fun uploadedImageAndSendDownloadUriToServer(
        id: String,
        fileUri: Uri
    ) {
        val downloadUri = uploadFile(fileUri)
        downloadUri?.let {
            sendDownloadUrlToServer(id, it)
        }
    }

    override fun sendDownloadUrlToServer(id: String, downloadUri: Uri) {
        downloadUri.let {
            val json = JsonObject()
            json.addProperty("id", id)
            json.addProperty("uri", it.toString())
            socketEmitEvent(json)
        }
    }

    override suspend fun getFilesCount(): Int? {
        return filesCount()
    }

    override suspend fun deleteAll() {
        deleteAllFiles()
    }

    override fun socketEmitEvent(json: JsonObject) {
        mySocket.socketEmitEvent(json)
    }

    override fun socketResponse(): LiveData<ServerResponse> {
        return mySocket.socketResponse
    }
}