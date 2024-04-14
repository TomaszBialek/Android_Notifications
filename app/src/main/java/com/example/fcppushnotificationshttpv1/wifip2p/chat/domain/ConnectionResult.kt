package com.example.fcppushnotificationshttpv1.wifip2p.chat.domain

sealed interface ConnectionResult {
    data object ConnectionEstablished : ConnectionResult
    data class TransferSucceeded(val message: WifiP2pMessage) : ConnectionResult
    data class Error(val message: String) : ConnectionResult
}