package com.example.fcppushnotificationshttpv1.wifip2p.chat.domain

data class WifiP2pMessage(
    val message: String,
    val senderName: String,
    val isFromLocalUser: Boolean
)
