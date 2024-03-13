package com.example.fcppushnotificationshttpv1.receivers

import android.net.NetworkInfo
import android.net.wifi.p2p.WifiP2pDevice
import android.net.wifi.p2p.WifiP2pDeviceList
import android.net.wifi.p2p.WifiP2pGroup
import android.net.wifi.p2p.WifiP2pInfo

interface WifiDirectListener {
    
    fun stateChangedAction(isWifiP2PEnabled: Boolean)
    fun peersChangedAction(deviceList: WifiP2pDeviceList?)
    fun connectionChangedAction(
        p2pInfo: WifiP2pInfo?,
        networkInfo: NetworkInfo?,
        p2pGroup: WifiP2pGroup?
    )
    fun thisDeviceChangedAction(p2pDevice: WifiP2pDevice?)
}