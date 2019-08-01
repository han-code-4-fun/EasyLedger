package hanzhou.easyledger.utility;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class PermissionUtil {

    public static boolean isAllPermissionsGranted(Context context, String... permissions) {

        if (context != null && permissions != null) {

            for (String permission : permissions) {

                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {

                    return false;
                }
            }
        }

        return true;
    }

    public static void askPermission(AppCompatActivity mAppCompatActivity, String[] PERMISSIONS, int REQUEST_PERMISSION_APP_START){
        ActivityCompat.requestPermissions(mAppCompatActivity,
                PERMISSIONS,
                REQUEST_PERMISSION_APP_START);
    }
}
