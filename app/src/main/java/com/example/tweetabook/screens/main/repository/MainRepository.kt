package com.example.tweetabook.screens.main.repository

import android.net.Uri
import androidx.lifecycle.LiveData
import com.example.tweetabook.api.MyBackendApi
import com.example.tweetabook.firebase.deleteAllFiles
import com.example.tweetabook.firebase.filesCount
import com.example.tweetabook.firebase.uploadFile
import com.example.tweetabook.socket.MySocket
import com.example.tweetabook.socket.responses.ServerResponse
import com.google.gson.JsonObject


class MainRepository(
    private val myBackendApi: MyBackendApi,
    private val mySocket: MySocket
) {
    private val TAG = "AppDebug: MainRepository"

    suspend fun uploadedImageAndSendDownloadUriToServer(
        id: String,
        fileUri: Uri
    ){
        val downloadUri = uploadFile(fileUri)
        downloadUri?.let {
            sendDownloadUrlToServer(id, it)
        }
    }

    private fun sendDownloadUrlToServer(id: String, downloadUri: Uri) {
        downloadUri.let {
            val json = JsonObject()
            json.addProperty("id", id)
            json.addProperty("uri", it.toString())
            socketEmitEvent(json)
        }
    }

    suspend fun getFilesCount(): Int? {
        return filesCount()
    }

    suspend fun deleteAll() {
        deleteAllFiles()
    }


    //  SOCKET.IO
    private fun socketEmitEvent(json: JsonObject) {
        mySocket.socketEmitEvent(json)
    }

    fun socketResponse(): LiveData<ServerResponse> {
        return mySocket.socketResponse
    }
}