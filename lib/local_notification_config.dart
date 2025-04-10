class LocalNotificationConfig{
  String type; //区分通知类型，方便打点
  String title;
  String body;
  //间隔分钟数,大于15分钟，
  //小于15分钟使用OneTimeWorkRequest，且固定5秒后执行
  //仅显示一次通知，随便传
  int intervalMinute;
  String logoName;  //logo名字，不带后缀，固定为.png
  String logoFolder; //logo文件夹

  LocalNotificationConfig({
    required this.type,
    required this.title,
    required this.body,
    required this.intervalMinute,
    this.logoName="ic_launcher",
    this.logoFolder="mipmap",
  });

  Map<String, dynamic> toJson() {
    return {
      'type': type,
      'title': title,
      'body': body,
      'intervalMinute':intervalMinute,
      'logoName':logoName,
      'logoFolder':logoFolder,
    };
  }
}