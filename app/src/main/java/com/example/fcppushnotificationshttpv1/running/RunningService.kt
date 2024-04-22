package com.example.fcppushnotificationshttpv1.running

import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.fcppushnotificationshttpv1.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch
import java.time.LocalDateTime


class RunningService: Service() {

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when(intent?.action) {
            Actions.START.toString() -> start()
            Actions.STOP.toString() -> stopSelf()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun start() {
        runClock()
        val notification = createNotification()
        startForeground(SERVICE_ID, notification)
    }

    private fun runClock() {
        scope.launch {
            while (true) {
                ensureActive()
                delay(1000)
                updateNotification()
            }
        }
    }

    private fun updateNotification() {
        val notification = createNotification()

        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.notify(SERVICE_ID, notification)
    }

    private fun createNotification() = NotificationCompat.Builder(this, "running_channel")
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentTitle("Run is active")
        .setContentText(LocalDateTime.now().toString())
        .setSilent(true)
        .build()


    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    enum class Actions {
        START, STOP
    }

    companion object {
        private const val SERVICE_ID = 1
    }
}