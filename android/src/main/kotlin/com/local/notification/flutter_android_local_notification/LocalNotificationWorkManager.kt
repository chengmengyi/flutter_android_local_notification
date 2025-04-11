package com.local.notification.flutter_android_local_notification

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.local.notification.flutter_android_local_notification.NotificationHep.createNotification

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