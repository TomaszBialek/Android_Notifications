package com.example.fcppushnotificationshttpv1.wifip2p.chat.domain

import android.net.wifi.p2p.WifiP2pDevice

data class ComposeWifiP2pDevice(
    val deviceAddress: String,
    val deviceName: String?,
    val primaryDeviceType: String?,
    val secondaryDeviceType: String?,
    val deviceStatus: STATUS
) {
    enum class STATUS {
        AVAILABLE, INVITED, CONNECTED, FAILED, UNAVAILABLE, UNKNOWN
    }
}

fun WifiP2pDevice.getDeviceStatus(): ComposeWifiP2pDevice.STATUS {
    return when (status){
        WifiP2pDevice.AVAILABLE -> ComposeWifiP2pDevice.STATUS.AVAILABLE
        WifiP2pDevice.INVITED -> ComposeWifiP2pDevice.STATUS.INVITED
        WifiP2pDevice.CONNECTED -> ComposeWifiP2pDevice.STATUS.CONNECTED
        WifiP2pDevice.FAILED -> ComposeWifiP2pDevice.STATUS.FAILED
        WifiP2pDevice.UNAVAILABLE -> ComposeWifiP2pDevice.STATUS.UNAVAILABLE
        else -> ComposeWifiP2pDevice.STATUS.UNKNOWN
    }
}

fun WifiP2pDevice.toComposeWifiP2pDevice(): ComposeWifiP2pDevice {
    return ComposeWifiP2pDevice(deviceAddress, deviceName, primaryDeviceType, secondaryDeviceType, getDeviceStatus())
}