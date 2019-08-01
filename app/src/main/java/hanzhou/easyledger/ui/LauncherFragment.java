package hanzhou.easyledger.ui;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.os.Handler;
import android.transition.Fade;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import hanzhou.easyledger.R;
import hanzhou.easyledger.ui.animation.DetailsTransition;
import hanzhou.easyledger.utility.Constant;
import hanzhou.easyledger.utility.PermissionUtil;
import hanzhou.easyledger.viewmodel.GeneralViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class LauncherFragment extends Fragment {

    public final static int REQUEST_PERMISSION_APP_START = 1001;

    private AppCompatActivity mAppCompatActivity;
    private ImageView mLogo;

    private static final long splashScreenTime = 3000L;

    public static String[] PERMISSIONS = {
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
        mLogo = root.findViewById(R.id.app_start_logo);



        /*set a time to display splash screen*/
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (PermissionUtil.isAllPermissionsGranted(mAppCompatActivity, PERMISSIONS)) {

                    loadInitialFragment();
                } else {
                    PermissionUtil.askPermission(mAppCompatActivity,PERMISSIONS,REQUEST_PERMISSION_APP_START);
//                    ActivityCompat.requestPermissions(mAppCompatActivity,
//                            PERMISSIONS,
//                            REQUEST_PERMISSION_APP_START);
                }
            }
        }, splashScreenTime);


//        LottieAnimationView lottieAnimationView = root.findViewById(R.id.app_start_animation);
//
//        lottieAnimationView.addAnimatorListener(new AnimatorListenerAdapter() {
//            @Override
//            public void onAnimationEnd(Animator animation) {
//
//                if(isAllPermissionsGranted(mAppCompatActivity, PERMISSIONS)){
//
//                    loadInitialFragment();
//                }else{
//                    ActivityCompat.requestPermissions(mAppCompatActivity,
//                            PERMISSIONS,
//                            REQUEST_PERMISSION_APP_START);
//                }
//            }
//        });

        return root;
    }


    private void setupViewModel() {
        GeneralViewModel mGeneralViewModel = ViewModelProviders.of(mAppCompatActivity).get(GeneralViewModel.class);
        mGeneralViewModel.setmCurrentScreen(Constant.FRAG_NAME_LAUNCHER);
    }

//    @Override
//    public boolean shouldShowRequestPermissionRationale(@NonNull String permission) {
//
//    }






//    private boolean isAllPermissionsGranted(Context context, String... permissions) {
//
//        if (context != null && permissions != null) {
//
//            for (String permission : permissions) {
//
//                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
//
//                    return false;
//                }
//            }
//        }
//
//        return true;
//    }


    private void loadInitialFragment() {
        OverviewFragment overviewFragment = new OverviewFragment();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            overviewFragment.setSharedElementEnterTransition(new DetailsTransition());
            overviewFragment.setEnterTransition(new Fade());
            setExitTransition(new Fade());
            overviewFragment.setSharedElementReturnTransition(new DetailsTransition());
        }


        mAppCompatActivity.getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.enter_from_bottom, R.anim.exit_to_top)
                .replace(R.id.fragment_base, overviewFragment, Constant.FRAG_NAME_OVERVIEW)
                .commit();

    }
}
