package com.example.fcppushnotificationshttpv1.wifip2p.chat.domain

import android.os.Handler
import android.os.Looper
import java.io.Closeable
import java.io.InputStream
import java.io.OutputStream
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.Socket
import java.util.concurrent.Executors
import kotlin.Exception

class ClientClass(
    hostAddress: InetAddress,
    val textListener: ((String) -> Unit)
) : Thread(), Closeable {

    private val hostAdd: String
    private val socket: Socket
        get() {
            return field
        }
    private lateinit var inputStream: InputStream
    private lateinit var outputStream: OutputStream

    init {
        hostAdd = hostAddress.hostAddress ?: throw Exception("Empty host address")
        socket = Socket()
    }

    fun write(bytes: ByteArray) {
        outputStream.write(bytes)
    }

    override fun run() {
        socket.connect(InetSocketAddress(hostAdd, 8888), 5_000)
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
        socket.close()
    }
}