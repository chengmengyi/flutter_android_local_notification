import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:flutter_android_local_notification/flutter_android_local_notification.dart';
import 'package:flutter_android_local_notification/local_notification_callback.dart';
import 'package:flutter_android_local_notification/local_notification_config.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({super.key});

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              TextButton(onPressed: (){
                FlutterAndroidLocalNotification.instance.initAllNotification(
                  fcmTopic: "",
                  serviceNotification: LocalNotificationConfig(type: "service", title: "常驻", body: "常驻", loopNum: 0, singleAddMinute: 0,),
                  lockScreenNotification: LocalNotificationConfig(type: "lock", title: "锁屏", body: "锁屏", loopNum: 0, singleAddMinute: 0,),
                  workList: [LocalNotificationConfig(type: "work", title: "哈哈哈", body: "吞吞吐吐", loopNum: 48, singleAddMinute: 60,)],
                  callback: LocalNotificationCallback(
                      clickNotificationCallback: (type){

                      },
                      lockScreenNotificationShow: (){

                      }
                  ),
                );
              }, child: Text("初始化所有通知"),),
              TextButton(onPressed: (){
                FlutterAndroidLocalNotification.instance.showNotification(config: LocalNotificationConfig(type: "单次显示", title: "单次显示", body: "吞吞吐吐", loopNum: 0, singleAddMinute: 0,));
              }, child: Text("单次显示"),),
              TextButton(onPressed: (){
                FlutterAndroidLocalNotification.instance.startNotificationService(config: LocalNotificationConfig(type: "常驻通知", title: "常驻通知", body: "吞吞吐吐", loopNum: 0, singleAddMinute: 0,));
              }, child: Text("常驻通知"),),
              TextButton(onPressed: (){
                FlutterAndroidLocalNotification.instance.initWorkManager(
                  list: [LocalNotificationConfig(type: "work", title: "哈哈哈", body: "吞吞吐吐", loopNum: 48, singleAddMinute: 60,)],
                  callback: LocalNotificationCallback(
                      clickNotificationCallback: (type){

                      },
                      lockScreenNotificationShow: (){

                      }
                  ),
                );
              }, child: Text("开启任务"),),
            ],
          ),
        ),
      ),
    );
  }
}
