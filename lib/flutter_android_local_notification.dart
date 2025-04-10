import 'package:flutter_android_local_notification/local_notification_callback.dart';
import 'package:flutter_android_local_notification/local_notification_config.dart';

import 'flutter_android_local_notification_platform_interface.dart';

class FlutterAndroidLocalNotification {
  static final FlutterAndroidLocalNotification _instance = FlutterAndroidLocalNotification();

  static FlutterAndroidLocalNotification get instance => _instance;

  initWorkManager({
    required List<LocalNotificationConfig> list,
    required LocalNotificationCallback callback,
  }){
    FlutterAndroidLocalNotificationPlatform.instance.initWorkManager(list,callback);
  }

  Future<String> getLaunchNotificationType()async{
    var result = await FlutterAndroidLocalNotificationPlatform.instance.getLaunchNotificationType();
    return result??"";
  }

  showNotification({required LocalNotificationConfig config,}){
    FlutterAndroidLocalNotificationPlatform.instance.showNotification(config);
  }
}
