package com.example.fcppushnotificationshttpv1.notification_chat.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.fcppushnotificationshttpv1.notification_chat.viewmodel.ChatViewModel

@Composable
fun NotificationChatScreen(
    modifier: Modifier = Modifier,
    chatViewModel: ChatViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = modifier.fillMaxSize()
    ) {
        val state = chatViewModel.state
        if (state.isEnteringToken) {
            EnterTokenDialog(
                token = state.remoteToken,
                onTokenChange = chatViewModel::onRemoteTokenChange,
                onSubmit = chatViewModel::onSubmitRemoteToken
            )
        } else {
            ChatScreen(
                messageText = state.messageText,
                onMessageSend = {
                    chatViewModel.sendMessage(isBroadcast = false)
                },
                onMessageBroadcast = {
                    chatViewModel.sendMessage(isBroadcast = true)
                },
                onMessageChange = chatViewModel::onMessageChange
            )
        }
    }
}

@Preview
@Composable
fun NotificationChatScreenPreview1() {
    NotificationChatScreen(Modifier, ChatViewModel())
}

@Preview
@Composable
fun NotificationChatScreenPreview2() {
    NotificationChatScreen(Modifier, ChatViewModel().apply { onSubmitRemoteToken() })
}