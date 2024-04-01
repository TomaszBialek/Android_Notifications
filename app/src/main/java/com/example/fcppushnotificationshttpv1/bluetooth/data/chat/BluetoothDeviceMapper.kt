package com.example.fcppushnotificationshttpv1.bluetooth.data.chat

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import com.example.fcppushnotificationshttpv1.bluetooth.domain.chat.BluetoothDeviceDomain

@SuppressLint("MissingPermission")
fun BluetoothDevice.toBluetoothDeviceDomain(): BluetoothDeviceDomain {
    return BluetoothDeviceDomain(
        name = name,
        address = address
    )
}