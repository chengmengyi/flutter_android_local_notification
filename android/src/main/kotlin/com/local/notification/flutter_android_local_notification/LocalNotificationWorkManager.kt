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
import java.util.Random

class LocalNotificationWorkManager(context: Context, workerParams: WorkerParameters,) : Worker(context, workerParams) {
    override fun doWork(): Result {
        createNotification()
        return Result.success()
    }

    private fun createNotification(){
        val type = inputData.getString("type")?:""
        val title = inputData.getString("title")?:""
        val body = inputData.getString("body")?:""
        val channelId = createNotificationChannel(
            "my_channel",
            "my_channel",
            NotificationManager.IMPORTANCE_MAX
        )

        val intent = Intent(mApplicationContext,NotificationActivity::class.java).apply {
            action="click_notification"
            putExtra("type",type)
        }

        val id = Random().nextInt(999)
        val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getActivity(mApplicationContext, id, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        }else{
            PendingIntent.getActivity(mApplicationContext, id, intent, PendingIntent.FLAG_ONE_SHOT)
        }

        val notification = NotificationCompat.Builder(mApplicationContext, channelId?:"channel_$id")
            .setSmallIcon(getLogoIconId())
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setContentTitle(title)
            .setContentText(body)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
        val notificationManager = NotificationManagerCompat.from(mApplicationContext)
        notificationManager.notify(id, notification.build())
    }

    private fun createNotificationChannel(channelID: String, channelNAME: String, level: Int): String? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val manager = mApplicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?
            val channel = NotificationChannel(channelID, channelNAME, level)
            manager!!.createNotificationChannel(channel)
            channelID
        } else {
            null
        }
    }

    private fun getLogoIconId() = mApplicationContext.resources.getIdentifier(
        inputData.getString("logoName")?:"ic_launcher",
        inputData.getString("logoFolder")?:"mipmap",
        mApplicationContext.packageName
    )
}