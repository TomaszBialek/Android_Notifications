package com.example.fcppushnotificationshttpv1.wifip2p.chat.domain.viewmodel

import android.net.wifi.p2p.WifiP2pDevice
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.fcppushnotificationshttpv1.wifip2p.chat.domain.ClientClass
import com.example.fcppushnotificationshttpv1.wifip2p.chat.domain.ServerClass
import java.net.InetAddress

class WifiP2pViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val peers = savedStateHandle.getStateFlow("peers", listOf<WifiP2pDevice>())
    val deviceNameArray = savedStateHandle.getStateFlow("deviceNameArray", arrayOf<String>())
    val deviceArray = savedStateHandle.getStateFlow("deviceArray", arrayOf<WifiP2pDevice>())

    val connectionStatus = savedStateHandle.getStateFlow("connectionStatus", false)
    val message = savedStateHandle.getStateFlow("message", "Received Message")

    val text = savedStateHandle.getStateFlow("text", "")

    lateinit var serverClass: ServerClass
    lateinit var clientClass: ClientClass
    var isHost: Boolean = false


    fun setPeers(peers: List<WifiP2pDevice>) {
        savedStateHandle["peers"] = peers
    }

    fun setDeviceNameArray(deviceNames: Array<String>) {
        savedStateHandle["deviceNameArray"] = deviceNames
    }

    fun setDeviceArray(devices: Array<WifiP2pDevice>) {
        savedStateHandle["deviceArray"] = devices
    }

    fun setConnectionStatus(status: Boolean) {
        savedStateHandle["connectionStatus"] = status
    }

    fun setMessage(message: String) {
        savedStateHandle["message"] = message
    }

    fun setText(text: String) {
        savedStateHandle["text"] = text
    }

    fun write(text: ByteArray) {
        if (isHost) {
            serverClass.write(text)
        } else {
            clientClass.write(text)
        }
    }

    fun closeSocket() {
        if (isHost) {
            serverClass.close()
        } else {
            clientClass.close()
        }
    }

    fun startServerSocket() {
        createServerSocket()
        serverClass.start()
    }

    fun startClientSocket(hostAddress: InetAddress) {
        createClientSocket(hostAddress)
        clientClass.start()
    }

    private fun createServerSocket() {
        isHost = true
        serverClass = ServerClass {
            setMessage(it)
        }
    }

    private fun createClientSocket(hostAddress: InetAddress) {
        isHost = false
        clientClass = ClientClass(hostAddress) {
            setMessage(it)
        }
    }
}