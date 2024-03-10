package com.example.fcppushnotificationshttpv1.notification_chat.api

import com.example.fcppushnotificationshttpv1.model.SendMessageDto
import retrofit2.http.Body
import retrofit2.http.POST

interface FcmApi {

    @POST("/send")
    suspend fun sendMessage(
        @Body body: SendMessageDto
    )

    @POST("/broadcast")
    suspend fun broadcast(
        @Body body: SendMessageDto
    )
}