import 'dart:convert';

import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';
import 'package:flutter_android_local_notification/local_notification_callback.dart';
import 'package:flutter_android_local_notification/local_notification_config.dart';

import 'flutter_android_local_notification_platform_interface.dart';

/// An implementation of [FlutterAndroidLocalNotificationPlatform] that uses method channels.
class MethodChannelFlutterAndroidLocalNotification extends FlutterAndroidLocalNotificationPlatform {
  LocalNotificationCallback? _callback;

  @visibleForTesting
  final methodChannel = const MethodChannel('flutter_android_local_notification');

  MethodChannelFlutterAndroidLocalNotification(){
    methodChannel.setMethodCallHandler((result)async{
      switch(result.method){
        case "clickNotificationCallBack":
          _callback?.clickNotificationCallback.call(result.arguments["notificationType"]);
          break;
      }
    });
  }

  @override
  Future<String?> initWorkManager(List<LocalNotificationConfig> list,LocalNotificationCallback callback) async {
    _callback=callback;
    await methodChannel.invokeMethod<String>('initWorkManager',list.map((config) => config.toJson()).toList());
  }

  @override
  Future<String?> getLaunchNotificationType() async{
    return await methodChannel.invokeMethod<String>("getLaunchNotificationType");
  }
}
