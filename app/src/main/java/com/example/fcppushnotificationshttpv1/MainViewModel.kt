package com.example.fcppushnotificationshttpv1

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    val destinations = listOf(
        Destination("notification", true),
        Destination("wifi"),
        Destination("bluetooth"),
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

data class Destination(
    val destination: String,
    val isEnabled: Boolean = false
)