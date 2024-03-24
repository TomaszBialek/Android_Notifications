package com.example.fcppushnotificationshttpv1.wifip2p.chat.domain.viewmodel

import android.net.wifi.p2p.WifiP2pDevice
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

class WifiP2pViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val peers = savedStateHandle.getStateFlow("peers", listOf<WifiP2pDevice>())
    val deviceNameArray = savedStateHandle.getStateFlow("deviceNameArray", arrayOf<String>())
    val deviceArray = savedStateHandle.getStateFlow("deviceArray", arrayOf<WifiP2pDevice>())

    val connectionStatus = savedStateHandle.getStateFlow("connectionStatus", false)
    val message = savedStateHandle.getStateFlow("message", "Received Message")


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
}