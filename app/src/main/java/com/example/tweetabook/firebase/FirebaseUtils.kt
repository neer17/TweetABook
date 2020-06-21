package com.example.tweetabook.firebase

import android.net.Uri
import android.util.Log
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await
import java.util.*

private const val TAG = "AppDebug: FirebaseUtils"

val storage = Firebase.storage
val storageRef = storage.reference.child("images/" + UUID.randomUUID())
val imagesRef = storage.reference.child("images/")

suspend fun uploadFile(fileUri: Uri): Uri? {
    val uploadTask = storageRef.putFile(fileUri)
    return try {
        uploadTask.await()
        storageRef.downloadUrl.await()
    } catch (e: Exception) {
        Log.e(TAG, "uploadFile: ", e)
        null
    }
}

suspend fun filesCount(): Int? {
    val getAllFiles = imagesRef.listAll()
    return try {
        val size = getAllFiles.await().items.size
        Log.d(TAG, "filesCount: total files in storage: $size")
        size
    } catch (e: Exception) {
        Log.e(TAG, "filesCount: ", e)
        null
    }
}

suspend fun deleteAllFiles(): Boolean {
    return try {
        val allFiles = imagesRef.listAll().await()

        allFiles.items.forEach {
            Log.d(TAG, "deleteAllFiles: storage ref $it")
            val deleteAll = it.delete()
            deleteAll.await()   //  return null
        }
        true
    } catch (e: Exception) {
        Log.e(TAG, "deleteAllFiles: ", e)
        false
    }
}