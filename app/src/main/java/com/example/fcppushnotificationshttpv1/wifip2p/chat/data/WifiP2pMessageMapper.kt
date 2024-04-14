package com.example.fcppushnotificationshttpv1.wifip2p.chat.data

import com.example.fcppushnotificationshttpv1.wifip2p.chat.domain.WifiP2pMessage

fun String.toWifiP2pMessage(isFromLocalUser: Boolean): WifiP2pMessage {
    val senderName = substringBeforeLast("#")
    val message = substringAfter("#")

    return WifiP2pMessage(message, senderName, isFromLocalUser)
}

fun WifiP2pMessage.toByteArray(): ByteArray {
    return "$senderName#$message".encodeToByteArray()
}