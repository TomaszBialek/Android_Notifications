package com.example.fcppushnotificationshttpv1.notification_chat.model

data class ChatState(
    val isEnteringToken: Boolean = true,
    val remoteToken: String = "",
    val messageText: String = ""
)
