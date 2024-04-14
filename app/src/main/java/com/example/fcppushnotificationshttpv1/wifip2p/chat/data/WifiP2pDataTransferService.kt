package com.example.fcppushnotificationshttpv1.wifip2p.chat.data

import com.example.fcppushnotificationshttpv1.bluetooth.data.chat.toBluetoothMessage
import com.example.fcppushnotificationshttpv1.wifip2p.chat.domain.WifiP2pMessage
import com.example.fcppushnotificationshttpv1.wifip2p.chat.domain.WifiP2pTransferFailedException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.Socket

class WifiP2pDataTransferService(
    private val socket: Socket
) {
    fun listenForIncomingMessages(): Flow<WifiP2pMessage> {
        return flow {
            if (!socket.isConnected) {
                return@flow
            }
            val buffer = ByteArray(1024)
            while (true) {
                val byteCount = try {
                    socket.getInputStream().read(buffer)
                } catch (e: IOException) {
                    throw WifiP2pTransferFailedException()
                }

                emit(
                    buffer.decodeToString(
                        endIndex = byteCount
                    ).toWifiP2pMessage(
                        isFromLocalUser = false
                    )
                )
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun sendMessage(bytes: ByteArray): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                socket.getOutputStream().write(bytes)
            } catch (e: IOException) {
                e.printStackTrace()
                return@withContext false
            }

            true
        }
    }
}