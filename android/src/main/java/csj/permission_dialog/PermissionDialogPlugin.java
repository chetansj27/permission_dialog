package csj.permission_dialog;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/**
 * PermissionDialogPlugin
 */
public class PermissionDialogPlugin implements FlutterPlugin, MethodCallHandler, ActivityAware, ActivityCompat.OnRequestPermissionsResultCallback {
    private String[] Permissions = {
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE

    };
    private Activity activity;
    private String permissionWithoutMessage;
    private int STORAGE_PERMISSION_CODE = 1;
    String title,message;

    private MethodChannel channel;


    public static void registerWith(Registrar registrar) {
        final MethodChannel channel = new MethodChannel(registrar.messenger(), "permission_dialog");
        channel.setMethodCallHandler(new PermissionDialogPlugin());
    }

    public static boolean hasPermissions(Context context, String... permissions) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
        channel = new MethodChannel(flutterPluginBinding.getFlutterEngine().getDartExecutor(), "permission_dialog");
        channel.setMethodCallHandler(this);
    }

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
        switch (call.method) {
            case "permissionWithMessage":
                title=call.argument("title").toString();
                message=call.argument("message").toString();
                if (ContextCompat.checkSelfPermission(activity,
                        Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(activity, "You have already granted this permission!",
                            Toast.LENGTH_SHORT).show();
                } else {
                    requestStoragePermission();
                }
                break;
            case "permissionDialog":
                int id = Integer.parseInt(call.argument("id").toString());
                permissionCode(id);
                if (ContextCompat.checkSelfPermission(activity,
                        permissionWithoutMessage) == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(activity, "You have already granted this permission!",
                            Toast.LENGTH_SHORT).show();
                } else {
                    requestPermissionWithout();
                }
                break;
            case "multiplePermissionDialog":
                if (!hasPermissions(activity, Permissions)) {
                    int permission_All = 1;
                    ActivityCompat.requestPermissions(activity, Permissions, permission_All);
                } else {
                    Toast.makeText(activity, "Granted", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                result.notImplemented();
                break;
        }
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        channel.setMethodCallHandler(null);
    }

    @Override
    public void onAttachedToActivity(@NonNull ActivityPluginBinding binding) {
        activity = binding.getActivity();
    }

    @Override
    public void onDetachedFromActivityForConfigChanges() {

    }

    @Override
    public void onReattachedToActivityForConfigChanges(@NonNull ActivityPluginBinding binding) {
        activity = binding.getActivity();
    }

    @Override
    public void onDetachedFromActivity() {

    }

    private void requestPermissionWithout() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                permissionWithoutMessage)) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{permissionWithoutMessage}, STORAGE_PERMISSION_CODE);


        } else {
            ActivityCompat.requestPermissions(activity,
                    new String[]{permissionWithoutMessage}, STORAGE_PERMISSION_CODE);

        }
    }

    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {
            new AlertDialog.Builder(activity)
                    .setTitle(title)
                    .setMessage(message)
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(activity,
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();
                        }
                    })
                    .create().show();
        } else {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);

        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(activity, "Permission GRANTED", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(activity, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void permissionCode(int id) {

        switch (id) {
            case 0:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
                    permissionWithoutMessage = Manifest.permission.ACCEPT_HANDOVER;

                break;
            case 1:
                permissionWithoutMessage = Manifest.permission.ACCESS_BACKGROUND_LOCATION;
                break;
            case 2:
                permissionWithoutMessage = Manifest.permission.ACCESS_CHECKIN_PROPERTIES;
                break;
            case 3:
                permissionWithoutMessage = Manifest.permission.ACCESS_COARSE_LOCATION;
                break;
            case 4:
                permissionWithoutMessage = Manifest.permission.ACCESS_FINE_LOCATION;
                break;

            case 5:
                permissionWithoutMessage = Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS;
                break;
            case 6:
                permissionWithoutMessage = Manifest.permission.ACCESS_MEDIA_LOCATION;
                break;

            case 7:
                permissionWithoutMessage = Manifest.permission.ACCESS_NETWORK_STATE;
                break;
            case 8:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    permissionWithoutMessage = Manifest.permission.ACCESS_NOTIFICATION_POLICY;
                }
                break;

            case 9:
                permissionWithoutMessage = Manifest.permission.ACCOUNT_MANAGER;
                break;
            case 10:
                permissionWithoutMessage = Manifest.permission.ACTIVITY_RECOGNITION;
                break;


        }
    }
}
