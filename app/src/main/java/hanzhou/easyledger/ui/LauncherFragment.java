package hanzhou.easyledger.ui;


import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import hanzhou.easyledger.R;
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

        View root = inflater.inflate(R.layout.fragment_launcher, container, false);
        mLogo = root.findViewById(R.id.app_start_logo);



        /*set a time for displaying splash screen*/
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (PermissionUtil.isAllPermissionsGranted(mAppCompatActivity, PERMISSIONS)) {

                    loadInitialFragment();
                } else {
                    PermissionUtil.askPermission(mAppCompatActivity, PERMISSIONS, REQUEST_PERMISSION_APP_START);
                }
            }
        }, splashScreenTime);


        return root;
    }


    private void setupViewModel() {
        GeneralViewModel mGeneralViewModel = ViewModelProviders.of(mAppCompatActivity).get(GeneralViewModel.class);
        mGeneralViewModel.setmCurrentScreen(Constant.FRAG_NAME_LAUNCHER);
    }


    private void loadInitialFragment() {
        OverviewFragment overviewFragment = new OverviewFragment();


        mAppCompatActivity.getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.enter_from_bottom, R.anim.exit_to_top)
                .replace(R.id.fragment_base, overviewFragment, Constant.FRAG_NAME_OVERVIEW)
                .commit();

    }
}
