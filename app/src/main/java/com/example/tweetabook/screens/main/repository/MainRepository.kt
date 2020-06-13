package com.example.tweetabook.screens.main.repository

import android.net.Uri
import com.example.tweetabook.firebase.uploadFile

class MainRepository {

    suspend fun uploadFileToStorage(fileUri: Uri): Uri? {
        return uploadFile(fileUri)
    }
}