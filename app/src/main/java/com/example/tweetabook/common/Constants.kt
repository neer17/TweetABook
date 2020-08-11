package com.example.tweetabook.common

object Constants {
    const val BASE_URL = "https://5c0051ae95e0.ngrok.io"

    //  MySocket
    const val SOCKET_LISTEN_EVENT = "server response"
    const val SOCKET_EMIT_EVENT = "image uri"

    //  STATUS FROM SERVER
    const val RECOGNIZING_TEXT = "recognizing text"
    const val IMAGE_CONVERSION_COMPLETED = "image conversion completed"

    //  NetworkBoundResource class
    const val NETWORK_TIMEOUT = 6000L
    const val TESTING_NETWORK_DELAY = 0L // fake network delay for testing
}
