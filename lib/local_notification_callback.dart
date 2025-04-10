class LocalNotificationCallback{
  const LocalNotificationCallback({
    required this.clickNotificationCallback,
    required this.lockScreenNotificationShow,
  });

  final void Function(String notificationType) clickNotificationCallback;
  final void Function() lockScreenNotificationShow;
}