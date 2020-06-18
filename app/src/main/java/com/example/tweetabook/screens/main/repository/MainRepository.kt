package com.example.tweetabook.screens.main.repository

import android.net.Uri
import android.util.Log
import com.example.tweetabook.api.MyBackendApi
import com.example.tweetabook.firebase.uploadFile
import com.google.gson.JsonObject

class MainRepository(private val myBackendApi: MyBackendApi) {
    private val TAG = "AppDebug: MainRepository"

    suspend fun uploadFileToStorage(fileUri: Uri): Uri? {
        val downloadUrl = uploadFile(fileUri)
        Log.d(TAG, "uploadFileToStorage: download url $downloadUrl")
        return downloadUrl
    }

    fun sendDownloadUrlToServer(downloadUri: Uri): String? {
        return downloadUri.let {
            val json = JsonObject()
            json.addProperty("url", it.toString())
            val response = myBackendApi.sendDownloadUrl(json).execute()
            Log.d(TAG, "sendDownloadUrlToServer: response.body: ${response.body()}")
            if (!response.isSuccessful) {
                Log.e(TAG, "sendDownloadUrlToServer: ", Throwable(response.errorBody().toString()))
                return null
            }

            response.body()!!.response
        }
    }
}