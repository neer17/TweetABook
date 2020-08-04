package com.example.tweetabook.socket

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.tweetabook.common.Constants
import com.example.tweetabook.socket.responses.ServerResponse
import com.github.nkzawa.emitter.Emitter
import com.github.nkzawa.socketio.client.IO
import com.github.nkzawa.socketio.client.Socket
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.net.URISyntaxException

class MySocket() {
    private val TAG = "AppDebug: MySocket"

    private var socket: Socket = IO.socket(Constants.BASE_URL)

    //  Coroutine Scope
    private val job = Job()
    private val socketScope = CoroutineScope(job)

    //  listener
    private var listener: Emitter.Listener? = Emitter.Listener { args ->
        val serverResponse =
            Gson().fromJson<ServerResponse>(args[0] as String, ServerResponse::class.java)

        socketScope.launch(Dispatchers.Main) {
            socketResponse.value = serverResponse
        }
    }

    //  LIVE DATA
    var socketResponse = MutableLiveData<ServerResponse>()

    init {
        Log.d(TAG, "MySocket: ")
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
            listener = null
            job.cancel()
        } catch (e: URISyntaxException) {
            Log.e(TAG, "socketIOConnection: ", e)
        }
    }

    fun socketEmitEvent(json: JsonObject) {
        socket.emit(Constants.SOCKET_EMIT_EVENT, json)
    }

    private fun socketListenEvent() {
        socket.on(Constants.SOCKET_LISTEN_EVENT, listener)
    }
}


