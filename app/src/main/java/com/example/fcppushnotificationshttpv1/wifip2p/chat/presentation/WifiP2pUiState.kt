package com.example.fcppushnotificationshttpv1.wifip2p.chat.presentation

import com.example.fcppushnotificationshttpv1.wifip2p.chat.domain.ComposeWifiP2pDevice
import com.example.fcppushnotificationshttpv1.wifip2p.chat.domain.WifiP2pMessage

data class WifiP2pUiState(
    val deviceArray: List<ComposeWifiP2pDevice> = emptyList(),
    val isDiscoveringStarted: Boolean = false,
    val messages: List<WifiP2pMessage> = emptyList(),
    val text: String = "",
    val errorMessage: String? = null
)