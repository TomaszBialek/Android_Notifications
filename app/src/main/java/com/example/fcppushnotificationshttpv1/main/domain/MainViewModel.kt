package com.example.fcppushnotificationshttpv1.main.domain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    val destinations = listOf(
        Destination("notification", "NotificationChatScreen", true),
        Destination("wifi", "WifiP2pConnectionScreen", true),
        Destination("bluetooth", "BluetoothChatScreen"),
        Destination("private notes", "PrivateNotesScreen", true),
    )

    private val _isReady = MutableStateFlow(false)
    val isReady = _isReady.asStateFlow()

    init {
        viewModelScope.launch {
            delay(3000L)
            _isReady.value = true
        }
    }
}

typealias Route = String

data class Destination(
    val destination: String,
    val route: Route,
    val isEnabled: Boolean = false
)