import 'package:firebase_messaging/firebase_messaging.dart';
import 'package:flutter_android_local_notification/local_notification_callback.dart';
import 'package:flutter_android_local_notification/local_notification_config.dart';
import 'package:flutter_broadcasts/flutter_broadcasts.dart';
import 'package:permission_handler/permission_handler.dart';

import 'flutter_android_local_notification_platform_interface.dart';

class FlutterAndroidLocalNotification {
  static final FlutterAndroidLocalNotification _instance = FlutterAndroidLocalNotification();

  static FlutterAndroidLocalNotification get instance => _instance;

  initAllNotification({
    required String fcmTopic, //不接入传""
    required List<LocalNotificationConfig> workList,
    required LocalNotificationConfig? lockScreenNotification, //锁屏通知，不接入传null
    required LocalNotificationConfig? serviceNotification, //常驻通知栏，不接入传null
    required LocalNotificationCallback callback,
  })async{
    var result = await Permission.notification.request();
    if (result.isGranted) {
      _registerBroadcast(lockScreenNotification,callback);
      initWorkManager(list: workList, callback: callback);
      if(null!=serviceNotification){
        startNotificationService(config: serviceNotification);
      }
      if(fcmTopic.isNotEmpty){
        FirebaseMessaging.instance.subscribeToTopic(fcmTopic);
      }
    }
    var type = await getLaunchNotificationType();
    if(type.isNotEmpty){
      callback.clickNotificationCallback.call(type);
    }
  }

  _registerBroadcast(LocalNotificationConfig? lockScreenNotification, LocalNotificationCallback callback){
    if(null==lockScreenNotification){
      return;
    }
    var receiver = BroadcastReceiver(names: ["android.intent.action.BOOT_COMPLETED","android.intent.action.USER_PRESENT"]);
    receiver.messages.listen((event) async{
      callback.lockScreenNotificationShow.call();
      showNotification(config: lockScreenNotification);
    });
    receiver.start();
  }

  initWorkManager({
    required List<LocalNotificationConfig> list,
    required LocalNotificationCallback callback,
  }){
    FlutterAndroidLocalNotificationPlatform.instance.initWorkManager(list,callback);
  }

  //initAllNotification方法会自动调用此方法
  Future<String> getLaunchNotificationType()async{
    var result = await FlutterAndroidLocalNotificationPlatform.instance.getLaunchNotificationType();
    return result??"";
  }

  showNotification({required LocalNotificationConfig config,}){
    FlutterAndroidLocalNotificationPlatform.instance.showNotification(config);
  }

  startNotificationService({required LocalNotificationConfig config,}){
    FlutterAndroidLocalNotificationPlatform.instance.startNotificationService(config);
  }
}
