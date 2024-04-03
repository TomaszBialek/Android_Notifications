package com.example.fcppushnotificationshttpv1.bluetooth.data.chat

import com.example.fcppushnotificationshttpv1.bluetooth.domain.chat.BluetoothMessage

fun String.toBluetoothMessage(isFromLocalUser: Boolean): BluetoothMessage {
    val name = substringBeforeLast("#")
    val message = substringAfter("#")

    return BluetoothMessage(name, message, isFromLocalUser)
}

fun BluetoothMessage.toByteArray(): ByteArray {
    return "$senderName#$message".encodeToByteArray()
}