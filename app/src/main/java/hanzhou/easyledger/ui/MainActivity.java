package hanzhou.easyledger.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import hanzhou.easyledger.R;
import hanzhou.easyledger.SmsBroadcastReceiver;
import hanzhou.easyledger.data.AppExecutors;
import hanzhou.easyledger.data.TransactionDB;
import hanzhou.easyledger.utility.BackPressHandler;
import hanzhou.easyledger.utility.Constant;
import hanzhou.easyledger.utility.FakeTestingData;
import hanzhou.easyledger.viewmodel.TransactionDBVMFactory;
import hanzhou.easyledger.viewmodel.TransactionDBViewModel;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import static android.provider.Telephony.Sms.Intents.SMS_RECEIVED_ACTION;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    public static String parentFragment;

    private FloatingActionButton btnFA;

    private IntentFilter smsIntentFilter;
    private SmsBroadcastReceiver smsReceiver;
    private BottomNavigationView bottomNavigation;

    private Toolbar toolBar;
    private TextView textViewOnToolBar;

    private Fragment selectedFragment;

    private Handler handlerBackPress;

    private int numOfTimesBackPressed;


    private TransactionDBViewModel viewModel;

    private boolean isInActionModel;
    private boolean isAllSelected;
    private int listSize;
    private int selectedNum;

    /* Database instance */
    private TransactionDB mDb;


    /*
        Ignore btn on toolbar that appears when toolbar is in action mode,
        this will 'tag' currently untagged transactions into 'others' category
     */
    private MenuItem ignore;
    private MenuItem delete;
    private MenuItem edit;
    private MenuItem selectAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        viewmodelInitialization();


        insert20FakeData();

        broadcastReceiverInitialization();

        uiInitialization();


        setViewModelOberver();

        //start initial fragment
        selectedFragment = new OverviewFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment_container, selectedFragment)
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
        getMenuInflater().inflate(R.menu.overview_toolbar_normal_mode, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_setting_mainactivity:
                startActivity(new Intent(MainActivity.this, SettingActivity.class));
                break;
            case R.id.menu_setting_question:
                startActivity(new Intent(this, QuestionActivity.class));
                break;
            case R.id.menu_setting_feedback:
                sendEmailToDeveloper();
                break;

            case android.R.id.home:

                if (isInActionModel) {
                    setToolBarToOriginMode();
                }
                break;

            case R.id.toolbar_edit:
                setToolBarToOriginMode();
//                startActivity(new Intent(MainActivity.this, TestTempActivity.class));
                break;

            case R.id.toolbar_select_all:
                /*  select/de-select all the transctions in the view
                    use viewmodel to send signal to Adapter's (adapter is observing these triggers),
                    adapter will preform operations after receive the signal,
                    after finish operation, adapter will set trigger to false
                 */
                Log.d(Constant.TESTFLOW+TAG, "onOptionsItemSelected: User clicked toolbar_select_all");
                if (isAllSelected) {
                    viewModel.setDeselectAllTrigger(true);
                } else {
                    viewModel.setSelectAllTrigger(true);

                }

                break;
            case R.id.toolbar_delete:
                if (selectedNum == 0) {
                    Toast.makeText(this,
                            getResources().getString(R.string.msg_deleting_need_to_have_one),
                            Toast.LENGTH_LONG).show();
                } else {
//                    //only transfer list of entities after conditions meet, to save system resource
//                    viewModel.setmTransferSelectedListTrigger(true);
                    AppExecutors.getInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {

                            mDb.transactionDAO().deleteListOfTransactions(
                                    viewModel.getSelectedTransactions()
                            );
                        }
                    });
                    Toast.makeText(this,
                            getResources().getString(R.string.msg_deleting_complete),
                            Toast.LENGTH_LONG).show();
                    setToolBarToOriginMode();
                }


                break;
            case R.id.toolbar_ignore:
                //todo put them all into others category

                break;

            default:
                return super.onOptionsItemSelected(item);

        }
        return true;

    }

    @Override
    public void onBackPressed() {
        if (isInActionModel) {
            setToolBarToOriginMode();

        } else {

            if (BackPressHandler.isUserPressedTwice(this)) {
                super.onBackPressed();
            }
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener bottomNavigationListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {


                    switch (menuItem.getItemId()) {
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

                    switchFragmentWithinActivity(selectedFragment);

                    return true;
                }
            };

    private FloatingActionButton.OnClickListener fabOnClickListener
            = new FloatingActionButton.OnClickListener() {
        @Override
        public void onClick(View view) {
            Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();

        }
    };

    private void viewmodelInitialization() {
        mDb = TransactionDB.getInstance(this);
//        TransactionDBVMFactory factory = new TransactionDBVMFactory(mDb, Constant.UNTAGGED);
//        viewModel = ViewModelProviders.of(this, factory).get(TransactionDBViewModel.class);
        viewModel = ViewModelProviders.of(this).get(TransactionDBViewModel.class);

        Log.d("test_hash", "MainActivity:  create viewModel "+ viewModel.hashCode());

    }

    private void broadcastReceiverInitialization() {
        smsIntentFilter = new IntentFilter();
        smsIntentFilter.addAction(SMS_RECEIVED_ACTION);
        smsReceiver = new SmsBroadcastReceiver();
    }

    private void uiInitialization() {
        toolBar = findViewById(R.id.toolbar_layout);
        setSupportActionBar(toolBar);
        toolBar.setNavigationIcon(R.drawable.ic_toolbar_nagivation);
        textViewOnToolBar = findViewById(R.id.toolbar_textview);


        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(bottomNavigationListener);

        btnFA = findViewById(R.id.btn_floating_aciton);
        btnFA.setOnClickListener(fabOnClickListener);
    }

    private void setViewModelOberver() {

        viewModel.getActionModeState().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                isInActionModel = aBoolean;
                Log.d("test_flow", "main activity got action state "+aBoolean);
                /*
                    only handle toolbar action mode start (comes from user long click),
                    end of (toolbar) action mode will be one of following :
                    1. press android.R.id.home
                    2. press back button
                    3. press edit (goes to another activity for editing)
                    4. press delete (after user select some data)
                    5. press ignore (automatically mark all transaciton in category others)
                 */
                if (isInActionModel) {
                    setToolBarToActionMode();
                }
            }

        });

//
//        viewModel.getmSizeOfTransactions().observe(this, new Observer<Integer>() {
//            @Override
//            public void onChanged(Integer integer) {
//                listSize = integer;
//            }
//        });


        viewModel.getTransactionSelectedNumberLiveData().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                selectedNum = integer;

                String display = integer + " " +
                        getResources().getString(R.string.string_toolbar_selection_word);
                textViewOnToolBar.setText(display);
                //display different type of icons on toolbar based on user selection
                if (isInActionModel) {
                    if (edit != null && ignore != null) {
                        if(viewModel.getCurrentLedger().equals(Constant.CALLFROMOVERVIEW)){
                            if (integer < 1) {
                                ignore.setVisible(false);
                                edit.setVisible(false);
                            } else if (integer == 1) {
                                ignore.setVisible(true);
                                edit.setVisible(true);
                            } else {
                                //more than 1 items is selected
                                ignore.setVisible(true);
                                edit.setVisible(false);

                            }
                        }else{
                            ignore.setVisible(false);
                            selectAll.setVisible(false);
                            delete.setVisible(true);
                            if (integer != 1) {
                                edit.setVisible(false);
                            }else{
                                edit.setVisible(true);

                            }
                        }

                    }

                }

            }
        });

        viewModel.getmIsAllSelected().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                isAllSelected = aBoolean;
            }
        });


    }

    private void switchFragmentWithinActivity(Fragment input) {

//        viewModel.prepareViewModelForChangingFragment();
        setToolBarToOriginMode();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment_container, input)
                .commit();
    }

    private void setToolBarToOriginMode() {

        toolBar.getMenu().clear();
        toolBar.setTitle(R.string.app_name);
        toolBar.inflateMenu(R.menu.overview_toolbar_normal_mode);
        toolBar.setNavigationIcon(R.drawable.ic_toolbar_nagivation);
        textViewOnToolBar.setVisibility(View.GONE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        viewModel.selectedBooleanArrayViewMode.clear();
        viewModel.setActionModeState(false);

    }

    private void setToolBarToActionMode() {

        toolBar.getMenu().clear();
        toolBar.setTitle(R.string.empty_string);
        toolBar.inflateMenu(R.menu.overview_toolbar_action_mode);
        textViewOnToolBar.setVisibility(View.VISIBLE);

        assignMenuItemToVariableForDifferentCombinationNSetInitialState();
        if(viewModel.getCurrentLedger().equals(Constant.CALLFROMLEDGER)){
            /*
                if current fragment is the ledger fragment
                (which is mostly likely already been tagged),
                not to display selectALl btn icon, because I don't want user
                accidently hit selectAll and hit delete.
             */
            selectAll.setVisible(false);
        }else{
            selectAll.setVisible(true);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void assignMenuItemToVariableForDifferentCombinationNSetInitialState() {

        /*  the ignore btn is for auto-set selected item to 'Others' category
         *  when entering toolbar action mode, no item has selected,
         *  there is not need to display edit and ignore
         * */
        ignore = toolBar.getMenu().findItem(R.id.toolbar_ignore);
        edit = toolBar.getMenu().findItem(R.id.toolbar_edit);
        selectAll = toolBar.getMenu().findItem(R.id.toolbar_select_all);
        delete = toolBar.getMenu().findItem(R.id.toolbar_delete);
        ignore.setVisible(false);
        edit.setVisible(false);
    }

    private void insert20FakeData() {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.transactionDAO().insertListOfTransactions(FakeTestingData.create20UntaggedTransactions());
            }
        });
    }


    private void sendEmailToDeveloper() {

        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        String[] emailList = {getResources().getString(R.string.developer_email_addr)};
        emailIntent.setType(getString(R.string.data_type_email));
        emailIntent.putExtra(Intent.EXTRA_EMAIL, emailList);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_feedback_subject));

        PackageManager packageManager = getPackageManager();
        boolean isIntentSafe = emailIntent.resolveActivity(packageManager) != null;
        if (isIntentSafe) {
            startActivity(emailIntent);
        } else {
            Toast.makeText(this, getString(R.string.no_email_app) + getString(R.string.developer_email_addr), Toast.LENGTH_LONG).show();
        }
    }

}