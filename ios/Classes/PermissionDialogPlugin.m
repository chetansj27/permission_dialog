#import "PermissionDialogPlugin.h"
#if __has_include(<permission_dialog/permission_dialog-Swift.h>)
#import <permission_dialog/permission_dialog-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "permission_dialog-Swift.h"
#endif

@implementation PermissionDialogPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftPermissionDialogPlugin registerWithRegistrar:registrar];
}
@end
