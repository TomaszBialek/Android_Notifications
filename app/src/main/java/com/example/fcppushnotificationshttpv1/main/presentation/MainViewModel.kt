package com.example.fcppushnotificationshttpv1.main.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fcppushnotificationshttpv1.core.Screen
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    val screens = listOf(
        Screen.NotificationChatScreen,
        Screen.WifiP2pConnectionScreen,
        Screen.BluetoothChatScreen,
        Screen.PrivateNotesScreen,
        Screen.RunningScreen
    )

    private val _isReady = MutableStateFlow(false)
    val isReady = _isReady.asStateFlow()

    init {
        viewModelScope.launch {
            delay(2000L)
            _isReady.value = true
        }
    }
}