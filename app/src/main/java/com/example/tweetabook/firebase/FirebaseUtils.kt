package com.example.tweetabook.firebase

import android.net.Uri
import android.util.Log
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await
import java.util.*

private const val TAG = "AppDebug: FirebaseUtils"

val storage = Firebase.storage

suspend fun uploadFile(fileUri: String): Uri? {
    val storageRef = getStorageRef()
    val uploadTask = storageRef.putFile(Uri.parse(fileUri))
    return try {
        uploadTask.await()
        val downloadUri = storageRef.downloadUrl.await()
//        Log.d(TAG, "uploadFile: image uploaded, \t download uri $downloadUri")
        downloadUri
    } catch (e: Exception) {
        Log.e(TAG, "uploadFile: ", e)
        null
    }
}

suspend fun filesCount(): Int? {
    val imagesRef = getImageRef()
    val getAllFiles = imagesRef.listAll()
    return try {
        val size = getAllFiles.await().items.size
        size
    } catch (e: Exception) {
        Log.e(TAG, "filesCount: ", e)
        null
    }
}

suspend fun deleteAllFiles(): Boolean {
    return try {
        val imagesRef = getImageRef()
        val allFiles = imagesRef.listAll().await()

        allFiles.items.forEach {
            val deleteAll = it.delete()
            deleteAll.await()   //  return null
        }
        true
    } catch (e: Exception) {
        Log.e(TAG, "deleteAllFiles: ", e)
        false
    }
}

private fun getImageRef() = storage.reference.child("images/")

private fun getStorageRef(): StorageReference =
    storage.reference.child("images/" + UUID.randomUUID())