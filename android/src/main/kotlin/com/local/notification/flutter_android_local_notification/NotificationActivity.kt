package com.local.notification.flutter_android_local_notification

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle

class NotificationActivity:Activity() {
    var open=false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.statusBarColor=Color.TRANSPARENT
    }

    override fun onStart() {
        super.onStart()
        if(!open){
            NotificationHep.clickNotificationCallBack(intent, pages<=1)
            if(pages<=1){
                startActivity(packageManager.getLaunchIntentForPackage(mApplicationContext.packageName))
            }
            finish()
        }
        open=true
    }
}