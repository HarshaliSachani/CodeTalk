package com.example.chatapp.socket_service

import android.util.Log
import com.example.chatapp.utils.AppPrefs
import com.example.chatapp.utils.Constants
import com.example.chatapp.utils.PrefConstant
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import org.json.JSONObject


class Connection {

    private var socket: Socket? = null

    companion object {
        private val TAG = javaClass.simpleName
        private var inst: Connection? = null
        fun getInstance(): Connection {
            if (inst == null) inst = Connection()
            return inst!!
        }

    }

    fun isConnected(): Boolean {
        return if (socket != null) socket!!.connected()
        else false
    }

    fun connect(onConnected: () -> Unit) {

        val opts = IO.Options()
        opts.forceNew = true
        opts.reconnection = true
        opts.reconnectionAttempts = 1
        opts.reconnectionDelay = 1

        socket = IO.socket(Constants.SOCKET_BASE_URL, opts)
        socket?.on("connection", connection)
        Log.e(TAG, "connect:  call connect=${Constants.SOCKET_BASE_URL}")

        socket?.on(Socket.EVENT_CONNECT) {
            Log.e(TAG, "connect:  EVENT_CONNECT=${socket?.id()}")
            onConnected.invoke()

        }

        socket?.on(Socket.EVENT_DISCONNECT) {
            Log.e(TAG, "connect:  EVENT_DISCONNECT=${it[0]}")
        }

        socket?.on(Socket.EVENT_CONNECT_ERROR) {
            Log.e(TAG, "connect:  EVENT_CONNECT_ERROR=${it[0]}")

        }
        socket?.connect()


    }

    private var connection = Emitter.Listener { args ->
        val res = args[0]
        Log.e(TAG, "connect:  socket connected=${res}")

    }

    fun receiveMessageEvent(onResponse: (meg: String) -> Unit) {
        socket?.on("message") {
            Log.e(TAG, "connect:  socket msg get=${it[0]}")
            if (!it.isNullOrEmpty() && it[0] != null) onResponse(it[0].toString())
        }
    }

    fun receiveGroupAllMessages(onResponse: (msg: String) -> Unit) {
        socket?.on("messages") {
            Log.e(TAG, "receiveGroupAllMessages: event >> messages >> response >> $it")
            if (!it.isNullOrEmpty() && it[0] != null) onResponse(it[0].toString())
        }
    }

    fun sendFetchAllMessageEvent(groupName: String) {
        val jObj = JSONObject()
        jObj.put(Constants.GROUP, groupName)
        socket?.emit("messages", jObj)
    }

    fun sendMessageEvent(msg: JSONObject) {
        socket?.emit("message", msg)
    }

    fun receiveJoinEvent(onResponse: () -> Unit) {
        socket?.on("join") { args ->
            Log.e(TAG, "connect:  joinConnection  args=${args}")
            if (!args.isNullOrEmpty() && args[0] != null) {
                val res = args[0]
                onResponse()
                Log.e(TAG, "connect:  joinConnection=${res}")
            }

        }
    }

    fun sendJoinGroupEvent(groupId: String, groupName: String) {
        val jObj = JSONObject()
        jObj.put(Constants.ID, AppPrefs.getString(PrefConstant.id))
        jObj.put(Constants.USER_NAME, AppPrefs.getString(PrefConstant.user_name))
        jObj.put(Constants.GROUP, groupName)
        jObj.put(Constants.ROOM, groupId)
        Log.e(TAG, "connect:  jObj=${jObj.toString()}")
        socket?.emit("join", jObj)


    }

}