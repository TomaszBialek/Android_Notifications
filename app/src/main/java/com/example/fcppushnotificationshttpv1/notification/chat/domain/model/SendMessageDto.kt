package com.example.fcppushnotificationshttpv1.notification.chat.domain.model

data class SendMessageDto(
    val to: String?,
    val notification: NotificationBody
)

data class NotificationBody(
    val title: String,
    val body: String
)
