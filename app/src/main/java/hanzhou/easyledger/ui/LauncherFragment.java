package hanzhou.easyledger.ui;


import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.airbnb.lottie.LottieAnimationView;

import hanzhou.easyledger.R;
import hanzhou.easyledger.utility.Constant;
import hanzhou.easyledger.viewmodel.GeneralViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class LauncherFragment extends Fragment {

    public final static int REQUEST_PERMISSION_APP_START = 1001;

    private AppCompatActivity mAppCompatActivity;

    private static final long splashScreenTime = 3000L;

    private String[] PERMISSIONS = {
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.READ_SMS,
            Manifest.permission.INTERNET,
    };

    public LauncherFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mAppCompatActivity = (AppCompatActivity) context;
        setupViewModel();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View root = inflater.inflate(R.layout.fragment_launcher, container, false);




        LottieAnimationView lottieAnimationView = root.findViewById(R.id.app_start_animation);

        lottieAnimationView.addAnimatorListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {

                if(isAllPermissionsGranted(mAppCompatActivity, PERMISSIONS)){

                    loadInitialFragment();
                }else{
                    ActivityCompat.requestPermissions(mAppCompatActivity,
                            PERMISSIONS,
                            REQUEST_PERMISSION_APP_START);
                }
            }
        });

        return root;
    }


    private void setupViewModel() {
        GeneralViewModel mGeneralViewModel = ViewModelProviders.of(mAppCompatActivity).get(GeneralViewModel.class);
        mGeneralViewModel.setmCurrentScreen(Constant.FRAG_NAME_LAUNCHER);
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
//                loadInitialFragment();
//            }
            if(isAllPermissionsGranted(mAppCompatActivity,permissions)){
                loadInitialFragment();
            }
        }
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


    private void loadInitialFragment(){
        mAppCompatActivity.getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.enter_from_bottom, R.anim.exit_to_top, R.anim.enter_from_top, R.anim.exit_to_bottom)
                .replace(R.id.fragment_base, new OverviewFragment(),Constant.FRAG_NAME_OVERVIEW)
                .commit();

    }
}
