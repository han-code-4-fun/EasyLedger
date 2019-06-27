package hanzhou.easyledger.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import hanzhou.easyledger.R;
import hanzhou.easyledger.SmsBroadcastReceiver;

import androidx.appcompat.widget.Toolbar;

import static android.provider.Telephony.Sms.Intents.SMS_RECEIVED_ACTION;

public class MainActivity extends AppCompatActivity{

    private static final String TAG = MainActivity.class.getSimpleName();

    private FloatingActionButton btnFA;

    private IntentFilter smsIntentFilter;
    private SmsBroadcastReceiver smsReceiver;
    private BottomNavigationView bottomNavigation;

    private Toolbar toolBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        toolBar = findViewById(R.id.toolbar_layout);
        setSupportActionBar(toolBar);

        toolBar.setNavigationIcon(R.drawable.ic_toolbar_nagivation);


        toolBar.setTitle(R.string.actionbar_title);



        smsIntentFilter = new IntentFilter();
        smsIntentFilter.addAction(SMS_RECEIVED_ACTION);

        smsReceiver = new SmsBroadcastReceiver();

        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(bottomNavigationListener);

        btnFA= findViewById(R.id.btn_floating_aciton);
        btnFA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });



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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_mainactivity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id){
            case R.id.menu_setting_mainactivity:
                //todo
                break;
            case R.id.menu_setting_question:
                startActivity(new Intent(this, QuestionActivity.class));
                break;
            case R.id.menu_setting_feedback:
                //todo
                break;
        }


        return super.onOptionsItemSelected(item);
    }



    private BottomNavigationView.OnNavigationItemSelectedListener bottomNavigationListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedFragment = null;

                    switch(menuItem.getItemId()){
                        case R.id.navigation_overview:
                            selectedFragment = new OverviewFragment();
                            btnFA.show();
                            break;
                        case R.id.navigation_transaction:
                            selectedFragment = new LedgerFragment();
                            btnFA.show();
                            break;
                        case R.id.navigation_charts:
                            selectedFragment = new ChartFragment();
                            btnFA.hide();
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
