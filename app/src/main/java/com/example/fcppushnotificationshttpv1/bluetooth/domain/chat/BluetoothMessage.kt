package com.example.fcppushnotificationshttpv1.bluetooth.domain.chat

data class BluetoothMessage(
    val message: String,
    val senderName: String,
    val isFromLocalUser: Boolean
)
