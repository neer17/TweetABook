package com.example.tweetabook.socket

import android.util.Log
import com.example.tweetabook.common.Constants
import com.example.tweetabook.socket.responses.ServerResponse
import com.github.nkzawa.emitter.Emitter
import com.github.nkzawa.socketio.client.IO
import com.github.nkzawa.socketio.client.Socket
import com.google.gson.Gson
import com.google.gson.JsonObject
import java.net.URISyntaxException


class MySocket {
    private val TAG = "AppDebug: MySocket"

    private var socket: Socket = IO.socket(Constants.BASE_URL)

    fun socketEmitEvent(json: JsonObject) {
        socket.emit(Constants.SOCKET_EMIT_EVENT, json)
    }

    private fun socketListenEvent() {
        socket.on(Constants.SOCKET_LISTEN_EVENT, socketEventListener())
    }

    fun socketIOConnection() {
        try {
            socket.connect()
            socketListenEvent()
        } catch (e: URISyntaxException) {
            Log.e(TAG, "socketIOConnection: ", e)
        }
    }

    fun socketIODisconnection() {
        try {
            socket.disconnect()
        } catch (e: URISyntaxException) {
            Log.e(TAG, "socketIOConnection: ", e)
        }
    }

    private fun socketEventListener(): Emitter.Listener =
        Emitter.Listener { args ->
            val response = Gson().fromJson<ServerResponse>(args[0] as String, ServerResponse::class.java)
//            Log.d(TAG, "socketEventListener: $response")
        }
}
