package hanzhou.easyledger.utility;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class PermissionUtil {

    public static boolean isAllPermissionsGranted(Context context, String... permissions) {
        Log.d("test_start", " request result 2___1 , rstart to check if all permissions granted");

        if (context != null && permissions != null) {
            Log.d("test_start", " request result 2___2 , is context & permissino not null");

            for (String permission : permissions) {
                Log.d("test_start", " request result 2___3 , check each permission");

                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    Log.d("test_start", " request result 2___3___1 , a permission NOT GRANTED");




                    return false;
                }
            }
        }
        Log.d("test_start", " request result 2___4 , alll good!!!!");

        return true;
    }

    public static void askPermission(AppCompatActivity mAppCompatActivity, String[] PERMISSIONS, int REQUEST_PERMISSION_APP_START){
        ActivityCompat.requestPermissions(mAppCompatActivity,
                PERMISSIONS,
                REQUEST_PERMISSION_APP_START);
    }
}
