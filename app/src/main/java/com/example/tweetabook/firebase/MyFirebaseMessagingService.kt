package com.example.tweetabook.firebase

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService: FirebaseMessagingService() {
    private val TAG = "AppDebug: " + MyFirebaseMessagingService::class.java.simpleName

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.i(TAG, "onMessageReceived: $message")

    }

    override fun onDestroy() {
        super.onDestroy()
    }
}