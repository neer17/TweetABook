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

suspend fun uploadFile(fileUri: Uri): Uri?{
    val uploadTask = storageRef.putFile(fileUri)
    return try {
        uploadTask.await()
        storageRef.downloadUrl.await()
    }catch (e: Exception) {
        Log.e(TAG, "uploadFile: ", e)
        null
    }
}