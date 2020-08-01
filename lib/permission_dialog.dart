import 'dart:async';

import 'package:flutter/services.dart';

class PermissionDialog {
  static const MethodChannel _channel =
      const MethodChannel('permission_dialog');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  static Future<Null> permissionDialogWithMessage(
      int id, String title, String message) async {
    Map<String, dynamic> args = <String, dynamic>{};

    await _channel.invokeMethod("permissionWithMessage");
    return null;
  }

  static Future<Null> permissionDialog(int id) async {
    Map<String, dynamic> args = <String, dynamic>{};
    args.putIfAbsent("id", () => id);
    await _channel.invokeMethod("permissionDialog", args);
    return null;
  }

  static Future<Null> multiplePermissionDialog() async {
    await _channel.invokeMethod("multiplePermissionDialog");
    return null;
  }
}
