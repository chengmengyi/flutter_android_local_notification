import 'package:flutter_android_local_notification/local_notification_callback.dart';
import 'package:flutter_android_local_notification/local_notification_config.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'flutter_android_local_notification_method_channel.dart';

abstract class FlutterAndroidLocalNotificationPlatform extends PlatformInterface {
  /// Constructs a FlutterAndroidLocalNotificationPlatform.
  FlutterAndroidLocalNotificationPlatform() : super(token: _token);

  static final Object _token = Object();

  static FlutterAndroidLocalNotificationPlatform _instance = MethodChannelFlutterAndroidLocalNotification();

  /// The default instance of [FlutterAndroidLocalNotificationPlatform] to use.
  ///
  /// Defaults to [MethodChannelFlutterAndroidLocalNotification].
  static FlutterAndroidLocalNotificationPlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [FlutterAndroidLocalNotificationPlatform] when
  /// they register themselves.
  static set instance(FlutterAndroidLocalNotificationPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<void> initWorkManager(List<LocalNotificationConfig> list,LocalNotificationCallback callback) {
    throw UnimplementedError('platformVersion() has not been implemented.');
  }

  Future<String?> getLaunchNotificationType(){
    throw UnimplementedError('platformVersion() has not been implemented.');
  }
  Future<void> showNotification(LocalNotificationConfig config){
    throw UnimplementedError('platformVersion() has not been implemented.');
  }
}
