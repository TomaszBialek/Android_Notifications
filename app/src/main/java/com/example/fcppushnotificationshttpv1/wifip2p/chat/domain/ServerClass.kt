package com.example.fcppushnotificationshttpv1.wifip2p.chat.domain

import android.os.Handler
import android.os.Looper
import java.io.Closeable
import java.io.InputStream
import java.io.OutputStream
import java.lang.Exception
import java.net.ServerSocket
import java.net.Socket
import java.util.concurrent.Executors

class ServerClass(
    val textListener: ((String) -> Unit)
) : Thread(), Closeable {
    private lateinit var serverSocket: ServerSocket
    private lateinit var socket: Socket
    private lateinit var inputStream: InputStream
    private lateinit var outputStream: OutputStream

    fun write(bytes: ByteArray) {
        outputStream.write(bytes)
    }

    override fun run() {
        serverSocket = ServerSocket(8888).apply {
            reuseAddress = true
        }
        socket = serverSocket.accept()
        inputStream = socket.getInputStream()
        outputStream = socket.getOutputStream()

        val executor = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())

        executor.execute {
            val buffer = ByteArray(1024)

            try {
                while (!socket.isClosed) {
                    val bytes = inputStream.read(buffer)
                    if (bytes > 0) {
                        val finalBytes = bytes
                        handler.post {
                            val tempMSG = String(buffer, 0, finalBytes)
                            textListener(tempMSG)
                        }
                    }
                }
            } catch (e: Exception) {
                // should be empty
            }
        }
    }

    override fun close() {
        serverSocket.close()
    }
}