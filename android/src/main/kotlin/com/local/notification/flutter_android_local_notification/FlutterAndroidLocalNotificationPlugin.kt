package com.local.notification.flutter_android_local_notification

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.work.*
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.PluginRegistry
import java.util.concurrent.TimeUnit

lateinit var mApplicationContext: Context

class FlutterAndroidLocalNotificationPlugin: FlutterPlugin, MethodCallHandler,PluginRegistry.NewIntentListener, ActivityAware{
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private lateinit var channel : MethodChannel
  private var mActivity:Activity?=null

  override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    mApplicationContext=flutterPluginBinding.applicationContext
    channel = MethodChannel(flutterPluginBinding.binaryMessenger, "flutter_android_local_notification")
    channel.setMethodCallHandler(this)
  }

  override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
    when(call.method){
      "initWorkManager"->{
        initWorkManager(call)
      }
      "getLaunchNotificationType"->{
        if(null!=mActivity){
          val intent = mActivity?.intent
          val action = intent?.extras?.getString("action")
          if(action=="click_notification"){
            result.success(intent.extras?.getString("type")?:"")
          }
        }
      }
      "showNotification"->showNotification(call)
      "startNotificationService"->startNotificationService(call)
    }
  }

  private fun startNotificationService(call: MethodCall){
    call.arguments?.let{
      runCatching {
        val map = it as Map<String, Any>
        val logoName = (map["logoName"] as? String) ?: ""
        val type = (map["type"] as? String) ?: ""
        val title = (map["title"] as? String) ?: ""
        val body = (map["body"] as? String) ?: ""
        val logoFolder = (map["logoFolder"] as? String) ?: ""
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.UPSIDE_DOWN_CAKE){
          val intent = Intent(mApplicationContext, NotificationService::class.java)
          intent.putExtra("logoName",logoName)
          intent.putExtra("type",type)
          intent.putExtra("title",title)
          intent.putExtra("body",body)
          intent.putExtra("logoFolder",logoFolder)
          ContextCompat.startForegroundService(mApplicationContext,intent)
        }else{
          NotificationHep.createNotification(type, title, body, logoName, logoFolder)
        }
      }
    }
  }

  private fun showNotification(call: MethodCall){
    call.arguments?.let{
      runCatching {
        val map = it as Map<String, Any>
        val logoName = (map["logoName"] as? String) ?: ""
        val type = (map["type"] as? String) ?: ""
        val title = (map["title"] as? String) ?: ""
        val body = (map["body"] as? String) ?: ""
        val logoFolder = (map["logoFolder"] as? String) ?: ""
        NotificationHep.createNotification(type, title, body, logoName, logoFolder)
      }
    }
  }

  private fun initWorkManager(call: MethodCall){
    call.arguments?.let{
      runCatching {
        val list = it as List<Map<String, Any>>
        for (map in list) {
          val builder = Data.Builder()
            .putString("logoName",(map["logoName"] as? String)?:"")
            .putString("type",(map["type"] as? String)?:"")
            .putString("title",(map["title"] as? String)?:"")
            .putString("body",(map["body"] as? String)?:"")
            .putString("logoFolder",(map["logoFolder"] as? String)?:"")
            .build()
          val intervalMinute = (map["intervalMinute"] as? Int)?:0
          val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
            .build()
          if(intervalMinute<15){
            val workRequest= OneTimeWorkRequest
              .Builder(LocalNotificationWorkManager::class.java)
              .setConstraints(constraints)
              .setInitialDelay(5, TimeUnit.SECONDS)
              .setInputData(builder)
              .build()
            WorkManager.getInstance(mApplicationContext).enqueue(workRequest)
          }else{
            val periodicWorkRequest = PeriodicWorkRequest
              .Builder(LocalNotificationWorkManager::class.java, intervalMinute.toLong(), TimeUnit.MINUTES)
              .setConstraints(constraints)
              .setInputData(builder)
              .build()
            WorkManager.getInstance(mApplicationContext).enqueueUniquePeriodicWork("startWork",ExistingPeriodicWorkPolicy.KEEP,periodicWorkRequest)
          }
        }
      }
    }
  }

  override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
    channel.setMethodCallHandler(null)
  }

  override fun onNewIntent(p0: Intent): Boolean {
    val action = p0.extras?.getString("action")
    if(action=="click_notification"){
      val map= hashMapOf<String,String>()
      map["notificationType"]= p0.extras?.getString("type")?:""
      channel.invokeMethod("clickNotificationCallBack",map)
      return true
    }
    return true
  }

  override fun onAttachedToActivity(binding: ActivityPluginBinding) {
    binding.addOnNewIntentListener(this)
    mActivity=binding.activity
  }

  override fun onDetachedFromActivityForConfigChanges() {

  }

  override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
    binding.addOnNewIntentListener(this)
    mActivity=binding.activity
  }

  override fun onDetachedFromActivity() {
    mActivity=null
  }
}
