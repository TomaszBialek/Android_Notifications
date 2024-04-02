package com.example.fcppushnotificationshttpv1.bluetooth.domain.chat

sealed interface ConnectionResult {
    data object ConnectionEstablished : ConnectionResult
    data class Error(val message: String) : ConnectionResult
}