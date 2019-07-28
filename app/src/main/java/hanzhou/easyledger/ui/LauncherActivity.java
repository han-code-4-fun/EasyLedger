package hanzhou.easyledger.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;

import com.airbnb.lottie.LottieAnimationView;

import hanzhou.easyledger.R;

public class LauncherActivity extends AppCompatActivity {

    public final static int REQUEST_PERMISSION_APP_START = 1001;

    private static final long splashScreenTime = 3000L;

    private String[] PERMISSIONS = {
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.READ_SMS,
            Manifest.permission.INTERNET,
    };


    private static final String TAG = LauncherActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        LottieAnimationView lottieAnimationView = findViewById(R.id.app_start_animation);

        lottieAnimationView.addAnimatorListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {

                if(isAllPermissionsGranted(LauncherActivity.this, PERMISSIONS)){


                    startMainActivity();
                }else{
                    ActivityCompat.requestPermissions(LauncherActivity.this,
                            PERMISSIONS,
                            REQUEST_PERMISSION_APP_START);
                }


            }
        });




//
//
//
//        if(isAllPermissionsGranted(this, PERMISSIONS)){
//            startMainActivity();
//        }else{
//            ActivityCompat.requestPermissions(this,
//                    PERMISSIONS,
//                    REQUEST_PERMISSION_APP_START);
//        }
        //app must read sms in order to work
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS)
//                != PackageManager.PERMISSION_GRANTED) {
//            // Permission is not granted
//            ActivityCompat.requestPermissions(this,
//                    new String[]{Manifest.permission.RECEIVE_SMS},
//                    REQUEST_PERMISSION_APP_START);
//        }else{
//            // Permission is granted
//            Log.d(TAG, "onCreate: Already granted");
//            startMainActivity();
//        }
    }


    private boolean isAllPermissionsGranted(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_PERMISSION_APP_START) {
//            if (grantResults.length > 0
//                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                Log.d(TAG, "onRequestPermissionsResult: Granted");
//                startMainActivity();
//            }
            if(isAllPermissionsGranted(this,permissions)){
                startMainActivity();
            }
        }
    }
    private void startMainActivity(){


        startActivity(new Intent(LauncherActivity.this, MainActivity.class));
        finish();


//        /*
//        *  let flash screen run for 3 seconds;
//        * */
//
//        Handler myHandler = new Handler();
//        myHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                startActivity(new Intent(LauncherActivity.this, MainActivity.class));
//                finish();
//            }
//        }, splashScreenTime);

    }
}
