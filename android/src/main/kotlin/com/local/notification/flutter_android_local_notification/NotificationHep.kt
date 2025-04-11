package com.local.notification.flutter_android_local_notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import io.flutter.plugin.common.MethodChannel
import java.util.*

object NotificationHep {
    fun createNotification(type:String,title:String,body:String,logoName:String,logoFolder:String){
        val channelId = createNotificationChannel(
            "my_channel",
            "my_channel",
            NotificationManager.IMPORTANCE_MAX
        )

        val intent = getLaunchIntent()?.apply {
            putExtra("type",type)
            putExtra("action","click_notification")
        }

        val id = Random().nextInt(999)
        val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getActivity(mApplicationContext, id, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        }else{
            PendingIntent.getActivity(mApplicationContext, id, intent, PendingIntent.FLAG_ONE_SHOT)
        }

        val notification = NotificationCompat.Builder(mApplicationContext, channelId?:"channel_$id")
            .setSmallIcon(getLogoIconId(logoName,logoFolder))
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

    fun getLogoIconId(logoName:String,logoFolder:String) = mApplicationContext.resources.getIdentifier(
        logoName,
        logoFolder,
        mApplicationContext.packageName
    )

    fun getLaunchIntent(): Intent? {
        val packageName = mApplicationContext.packageName
        val packageManager = mApplicationContext.packageManager
        val intent = packageManager.getLaunchIntentForPackage(packageName)
        intent?.action = Intent.ACTION_MAIN
        intent?.addCategory(Intent.CATEGORY_LAUNCHER)
        intent?.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        return intent
    }
}