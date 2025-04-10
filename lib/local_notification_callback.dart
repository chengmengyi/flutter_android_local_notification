class LocalNotificationCallback{
  const LocalNotificationCallback({required this.clickNotificationCallback});

  final void Function(String notificationType) clickNotificationCallback;
}