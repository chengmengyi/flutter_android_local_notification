class LocalNotificationConfig{
  String type; //区分通知类型，方便打点
  String title;
  String body;
  //循环次数，单个通知传0
  int loopNum;
  //单次增加分钟数，必须大于15
  //单个通知传0
  //小于15分钟使用OneTimeWorkRequest，且固定5秒后执行
  int singleAddMinute;
  String logoName;  //logo名字，不带后缀，固定为.png
  String logoFolder; //logo文件夹

  LocalNotificationConfig({
    required this.type,
    required this.title,
    required this.body,
    required this.loopNum,
    required this.singleAddMinute,
    this.logoName="ic_launcher",
    this.logoFolder="mipmap",
  });

  Map<String, dynamic> toJson() {
    return {
      'type': type,
      'title': title,
      'body': body,
      'loopNum':loopNum,
      'singleAddMinute':singleAddMinute,
      'logoName':logoName,
      'logoFolder':logoFolder,
    };
  }
}