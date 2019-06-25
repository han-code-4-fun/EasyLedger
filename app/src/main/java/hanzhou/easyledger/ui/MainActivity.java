package hanzhou.easyledger.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import hanzhou.easyledger.R;
import hanzhou.easyledger.SmsBroadcastReceiver;

import static android.provider.Telephony.Sms.Intents.SMS_RECEIVED_ACTION;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    IntentFilter smsIntentFilter;
    SmsBroadcastReceiver smsReceiver;
    BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        smsIntentFilter = new IntentFilter();
        smsIntentFilter.addAction(SMS_RECEIVED_ACTION);

        smsReceiver = new SmsBroadcastReceiver();

        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(bottomNavigationListener);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment_container, new OverviewFragment())
                .commit();

    }
    @Override
    protected void onResume() {
        super.onResume();
        this.registerReceiver(smsReceiver, smsIntentFilter);
        Log.d(TAG, "onResume: smsReceiver registered");
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(smsReceiver);
        Log.d(TAG, "onPause: smsReceiver unregistered");
    }

    private BottomNavigationView.OnNavigationItemSelectedListener bottomNavigationListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedFragment = null;

                    switch(menuItem.getItemId()){
                        case R.id.navigation_overview:
                            selectedFragment = new OverviewFragment();
                            break;
                        case R.id.navigation_transaction:
                            selectedFragment = new LedgerFragment();
                            break;
                        case R.id.navigation_charts:
                            selectedFragment = new ChartFragment();
                            break;

                    }

                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.main_fragment_container, selectedFragment)
                            .commit();

                    return true;
                }
            };

}
