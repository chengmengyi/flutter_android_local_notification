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
  var type="";

  @override
  void initState() {
    super.initState();
    _getLaunchNotificationType();
  }

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
                FlutterAndroidLocalNotification.instance.initWorkManager(
                    list: [LocalNotificationConfig(type: "type", title: "哈哈哈", body: "吞吞吐吐", intervalMinute: 15,)],
                    callback: LocalNotificationCallback(
                      clickNotificationCallback: (type){
                        setState(() {
                          this.type=type;
                        });
                      },
                    )
                );
              }, child: Text("点击===${type}"),),
              TextButton(onPressed: (){
                FlutterAndroidLocalNotification.instance.showNotification(config: LocalNotificationConfig(type: "type", title: "单次显示", body: "吞吞吐吐", intervalMinute: 15,));
              }, child: Text("单次显示"),)
            ],
          ),
        ),
      ),
    );
  }

  _getLaunchNotificationType()async{
    type=await FlutterAndroidLocalNotification.instance.getLaunchNotificationType();
    setState(() {

    });
  }
}
