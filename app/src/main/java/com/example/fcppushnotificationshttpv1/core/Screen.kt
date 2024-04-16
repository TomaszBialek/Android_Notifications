package com.example.fcppushnotificationshttpv1.core

typealias Route = String

sealed class Screen(
    val description: String,
    val route: Route,
    val isEnabled: Boolean = false
) {
    data object MainScreen: Screen("main", "MainScreen", true)
    data object NotificationChatScreen: Screen("notification", "NotificationChatScreen", true)
    data object WifiP2pConnectionScreen: Screen("wifi", "WifiP2pConnectionScreen", true)
    data object BluetoothChatScreen: Screen("bluetooth", "BluetoothChatScreen", true)
    data object PrivateNotesScreen: Screen("private notes", "PrivateNotesScreen", true)
    data object AddEditPrivateNoteScreen: Screen("add/edit note","AddEditNoteScreen", true)
    data object CompressingPhotoScreen: Screen("Compress a photo", "CompressPhotoScreen", true)
}