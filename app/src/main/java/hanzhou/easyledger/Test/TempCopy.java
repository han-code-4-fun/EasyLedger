package hanzhou.easyledger.Test;

import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import hanzhou.easyledger.R;
import hanzhou.easyledger.SmsBroadcastReceiver;
import hanzhou.easyledger.data.TransactionDB;
import hanzhou.easyledger.ui.OverviewFragment;
import hanzhou.easyledger.viewmodel.AdapterNActionBarViewModel;
import hanzhou.easyledger.viewmodel.OverviewFragmentViewModel;
import hanzhou.easyledger.viewmodel.TransactionDBViewModel;

public class TempCopy {
//
//    package hanzhou.easyledger.ui;
//
//
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.content.pm.PackageManager;
//import android.os.Bundle;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.widget.Toolbar;
//import androidx.fragment.app.Fragment;
//import androidx.lifecycle.LifecycleOwner;
//import androidx.lifecycle.Observer;
//import androidx.lifecycle.ViewModelProviders;
//
//import android.view.LayoutInflater;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.google.android.material.bottomnavigation.BottomNavigationView;
//import com.google.android.material.floatingactionbutton.FloatingActionButton;
//import com.google.android.material.snackbar.Snackbar;
//
//import java.util.List;
//
//import hanzhou.easyledger.R;
//import hanzhou.easyledger.SmsBroadcastReceiver;
//import hanzhou.easyledger.data.AppExecutors;
//import hanzhou.easyledger.data.TransactionDB;
//import hanzhou.easyledger.data.TransactionEntry;
//import hanzhou.easyledger.utility.Constant;
//import hanzhou.easyledger.viewmodel.AdapterNActionBarViewModel;
//import hanzhou.easyledger.viewmodel.OverviewFragmentVMFactory;
//import hanzhou.easyledger.viewmodel.OverviewFragmentViewModel;
//import hanzhou.easyledger.viewmodel.TransactionDBViewModel;
//
//    /**
//     * A simple {@link Fragment} subclass.
//     */
//    public class BaseFragment extends Fragment {
//        private static final String TAG = BaseFragment.class.getSimpleName();
//
//
//        private IntentFilter mSmsIntentFilter;
//        private SmsBroadcastReceiver mSmsReceiver;
//
//        /* Database instance */
//        private TransactionDB mDb;
//        private TransactionDBViewModel mTransactionViewModel;
//        private OverviewFragmentViewModel mOverviewViewModel;
//        private AdapterNActionBarViewModel mAdapterActionViewModel;
//
//        private Fragment selectedFragment;
//
//        private FloatingActionButton btnFA;
//
//        private Toolbar toolBar;
//        private TextView textViewOnToolBar;
//
//
//        /*
//            Ignore btn on toolbar that appears when toolbar is in action mode,
//            this will 'tag' currently untagged transactions into 'others' category
//         */
//        private MenuItem ignore;
//        private MenuItem delete;
//        private MenuItem edit;
//        private MenuItem selectAll;
//
//        private boolean isInActionModel;
//        private boolean isAllSelected;
//        private int mNumberOfSelection;
//        AppCompatActivity appCompatActivity;
//
//
//        public BaseFragment() {
//            // Required empty public constructor
//        }
//
//        @Override
//        public void onAttach(Context context) {
//            super.onAttach(context);
//            appCompatActivity = (AppCompatActivity)context;
//        }
//
//        @Override
//        public void onCreate(@Nullable Bundle savedInstanceState) {
//            super.onCreate(savedInstanceState);
//            setHasOptionsMenu(true);
//
//
//        }
//
//        @Override
//        public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                                 Bundle savedInstanceState) {
//            View rootView = inflater.inflate(R.layout.fragment_base, container, false);
//
//
//            return rootView;
//        }
//
//        @Override
//        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//            super.onActivityCreated(savedInstanceState);
//
//            activityStartRunFragment();
//        }
//
//
//
//
//        private void activityStartRunFragment() {
//            selectedFragment = new OverviewFragment();
//            switchFragmentWithinActivity(selectedFragment);
//        }
//
//        private void switchFragmentWithinActivity(Fragment input) {
//
//            //todo, trigger mainactivity's aciton bar into origin mode
//
//            appCompatActivity.getSupportFragmentManager()
//                    .beginTransaction()
//                    .replace(R.id.main_fragment_container, input)
//                    .commit();
//        }
//
//
//
//
//
//
//
//
//    }

}
