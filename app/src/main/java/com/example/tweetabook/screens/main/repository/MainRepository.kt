package com.example.tweetabook.screens.main.repository

import android.net.Uri
import android.util.Log
import com.example.tweetabook.api.MyBackendApi
import com.example.tweetabook.firebase.deleteAllFiles
import com.example.tweetabook.firebase.filesCount
import com.example.tweetabook.firebase.uploadFile
import com.example.tweetabook.socket.MySocket
import com.google.gson.JsonObject

class MainRepository(
    private val myBackendApi: MyBackendApi,
    private val mySocket: MySocket
) {
    private val TAG = "AppDebug: MainRepository"


    suspend fun uploadFileToStorage(fileUri: Uri): Uri? {
        val downloadUrl = uploadFile(fileUri)
        Log.d(TAG, "uploadFileToStorage: download url $downloadUrl")
        return downloadUrl
    }

    fun sendDownloadUrlToServer(downloadUri: Uri) {
        downloadUri.let {
            val json = JsonObject()
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

    fun socketIOConnection() {
        mySocket.socketIOConnection()
    }

    fun socketIODisconnection() {
        mySocket.socketIODisconnection()
    }

}