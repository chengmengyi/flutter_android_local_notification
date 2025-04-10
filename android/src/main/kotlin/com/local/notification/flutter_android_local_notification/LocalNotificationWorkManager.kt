package com.local.notification.flutter_android_local_notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.local.notification.flutter_android_local_notification.NotificationHep.createNotification
import java.util.Random

class LocalNotificationWorkManager(context: Context, workerParams: WorkerParameters,) : Worker(context, workerParams) {
    override fun doWork(): Result {
        val type = inputData.getString("type")?:""
        val title = inputData.getString("title")?:""
        val body = inputData.getString("body")?:""
        val logoName = inputData.getString("logoName") ?: "ic_launcher"
        val logoFolder = inputData.getString("logoFolder") ?: "mipmap"
        createNotification(type, title, body, logoName, logoFolder)
        return Result.success()
    }


}