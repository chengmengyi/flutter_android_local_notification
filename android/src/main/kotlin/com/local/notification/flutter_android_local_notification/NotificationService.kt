package com.local.notification.flutter_android_local_notification

import android.app.*
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import java.util.*

class NotificationService:Service() {
    private val serviceId=33
    private val channelId = "foregroundService_channelId"
    private val channelName = "foregroundService_channelName"
    private val channelDesc = "foregroundService_channelDesc"

    override fun onBind(p0: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        initForegroundService(intent)
        return START_STICKY
    }

    private fun initForegroundService(intent: Intent?) {
        createNotificationChannel()
        val notification = initServiceNotification(intent)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(
                serviceId,
                notification,
                ServiceInfo.FOREGROUND_SERVICE_TYPE_MANIFEST
            )
        } else {
            startForeground(serviceId, notification)
        }
    }

    private fun initServiceNotification(intent: Intent?): Notification {
        val type = intent?.getStringExtra("type")?:""
        val title = intent?.getStringExtra("title")?:""
        val body = intent?.getStringExtra("body")?:""
        val logoName = intent?.getStringExtra("logoName")?:""
        val logoFolder = intent?.getStringExtra("logoFolder")?:""

        val intent=NotificationHep.getLaunchIntent()?.apply {
            putExtra("type",type)
            putExtra("action","click_notification")
        }
        val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getActivity(mApplicationContext, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        }else{
            PendingIntent.getActivity(mApplicationContext, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        val builder = createBuilder(pendingIntent,logoName, logoFolder, title, body)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            builder.setForegroundServiceBehavior(Notification.FOREGROUND_SERVICE_IMMEDIATE)
        }
        return builder.build()
    }

    private fun createBuilder(pendingIntent: PendingIntent,logoName:String,logoFolder:String,title:String,body:String): Notification.Builder {
        val builder = Notification.Builder(this, channelId)
        builder.setOngoing(true)
        builder.setShowWhen(false)
        builder.setSmallIcon(NotificationHep.getLogoIconId(logoName, logoFolder))
        builder.setContentIntent(pendingIntent)
        builder.setVisibility(Notification.VISIBILITY_PUBLIC)
        builder.setContentTitle(title)
        builder.setContentText(body)
        return builder
    }

    private fun createNotificationChannel() {
        val nm = getSystemService(NotificationManager::class.java)
        if (nm.getNotificationChannel(channelId) == null) {
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_LOW).apply {
                if (channelDesc != null) {
                    description = channelDesc
                }
                enableVibration(false)
            }
            nm.createNotificationChannel(channel)
        }
    }
}