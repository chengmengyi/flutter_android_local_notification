package com.local.notification.flutter_android_local_notification
import android.content.Intent
import io.flutter.plugin.common.MethodChannel

object NotificationHep {
    var notificationType=""
    private var channel: MethodChannel?=null

    fun initChannel(channel: MethodChannel){
        this.channel=channel
    }

    fun clickNotificationCallBack(intent: Intent?,fromLaunch:Boolean){
        if(intent?.action=="click_notification"){
            notificationType=intent.extras?.getString("type")?:""
            val map= hashMapOf<String,String>()
            map["notificationType"]=notificationType
            if(!fromLaunch){
                channel?.invokeMethod("clickNotificationCallBack",map)
                notificationType=""
            }
        }
    }
}