package hanzhou.easyledger.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import hanzhou.easyledger.R;
import hanzhou.easyledger.SmsBroadcastReceiver;
import hanzhou.easyledger.data.AppExecutors;
import hanzhou.easyledger.data.TransactionDB;
import hanzhou.easyledger.data.TransactionEntry;
import hanzhou.easyledger.utility.BackPressHandler;
import hanzhou.easyledger.utility.Constant;
import hanzhou.easyledger.utility.FakeTestingData;
import hanzhou.easyledger.utility.UnitUtil;
import hanzhou.easyledger.viewmodel.AdapterNActionBarViewModel;
import hanzhou.easyledger.viewmodel.OverviewFragmentVMFactory;
import hanzhou.easyledger.viewmodel.OverviewFragmentViewModel;
import hanzhou.easyledger.viewmodel.TransactionDBViewModel;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import net.danlew.android.joda.JodaTimeAndroid;

import org.joda.time.LocalDate;

import java.util.Date;
import java.util.List;

import static android.provider.Telephony.Sms.Intents.SMS_RECEIVED_ACTION;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();


    private IntentFilter mSmsIntentFilter;
    private SmsBroadcastReceiver mSmsReceiver;

    /* Database instance */
    private TransactionDB mDb;
    private TransactionDBViewModel mTransactionViewModel;
    private OverviewFragmentViewModel mOverviewViewModel;
    private AdapterNActionBarViewModel mAdapterActionViewModel;

    private Fragment selectedFragment;

    private BottomNavigationView bottomNavigation;

    private FloatingActionButton btnFA;

    private Toolbar toolBar;
    private TextView textViewOnToolBar;


    /*
        Ignore btn on toolbar that appears when toolbar is in action mode,
        this will 'tag' currently untagged transactions into 'others' category
     */
    private MenuItem mIgnoreBtn;
    private MenuItem mDeleteBtn;
    private MenuItem mEditBtn;
    private MenuItem mSelectAllBtn;

    private MenuItem mSaveBtn;

    private boolean isInActionModel;
    private boolean isAllSelected;

    private boolean isInQuestionFragment;
    private boolean isInSettingsFragment;
    private boolean isInAddNEditFragment;

    private int mNumberOfSelection;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*Initialize JodaTime library*/
        JodaTimeAndroid.init(this);

        viewmodelInitialization();

        broadcastReceiverInitialization();

        uiInitialization();

        setViewModelOberver();

        activityStartRunFragment();

        testSharedPreference();

    }

    private void testSharedPreference() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        if(sp == null){
            Log.d("test_flow3", "testSharedPreference: is empty");
        }else{
            Log.d("test_flow3", "testSharedPreference: is NOT empty");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.registerReceiver(mSmsReceiver, mSmsIntentFilter);
        Log.d(TAG, "onResume: mSmsReceiver registered");
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mSmsReceiver);
        Log.d(TAG, "onPause: mSmsReceiver unregistered");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_normal_mode, menu);
        return true;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_setting_mainactivity:
                setToolBarToOriginMode();
                selectedFragment = new PreferenceFragment();
                addCurrentFragmentToBack(selectedFragment);

                break;
            case R.id.menu_user_has_question:
                setToolBarToOriginMode();
                selectedFragment = new QuestionFragment();
                addCurrentFragmentToBack(selectedFragment);
                break;
            case R.id.menu_feedback:
                sendEmailToDeveloper();
                break;
            case R.id.menu_insert_data:
                insert20FakeData();
                break;
            case R.id.menu_delete_all_data:
                deleteAll();
                break;
            case R.id.menu_insert_data_within7days:
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        mDb.transactionDAO().insertListOfTransactions(
                                FakeTestingData.create10DesignateTestingDataInCertaindays(7));
                    }
                });
                break;
            case R.id.menu_insert_data_within30days:
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        mDb.transactionDAO().insertListOfTransactions(
                                FakeTestingData.create10DesignateTestingDataInCertaindays(30));
                    }
                });
                break;
            case R.id.menu_insert_data_within180days:
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        mDb.transactionDAO().insertListOfTransactions(
                                FakeTestingData.create10DesignateTestingDataInCertaindays(180));
                    }
                });

                break;
            case R.id.menu_insert_data_within_this_week:
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        mDb.transactionDAO().insertListOfTransactions(
                                FakeTestingData.create10DesignateTestingDataInCurrentWeek());
                    }
                });

                break;
            case R.id.menu_insert_data_within_this_month:
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        mDb.transactionDAO().insertListOfTransactions(
                                FakeTestingData.create10DesignateTestingDataInCurrentMonth());
                    }
                });
                break;

            case R.id.menu_insert_untagged_data:
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        mDb.transactionDAO().insertListOfTransactions(
                                FakeTestingData.create5UntaggedTransactions());
                    }
                });
                break;

            case android.R.id.home:
                if (isInActionModel) {
                    setToolBarToOriginMode();
                }else if (isInQuestionFragment){


                    //todo, jump back to previous fragment
                    getSupportFragmentManager().popBackStack();
                    btnFA.show();
                }else if(isInSettingsFragment){
                    //todo, combine this and previous into one logic
                    getSupportFragmentManager().popBackStack();
                    btnFA.show();
                }else if(isInAddNEditFragment){
                    //todo, combine this and previous into one logic

                    getSupportFragmentManager().popBackStack();
                    btnFA.show();
                }
                break;

            case R.id.toolbar_edit:
                setToolBarToOriginMode();
                //todo, implement editing fragment/activity
//                startActivity(new Intent(MainActivity.this, TestTempActivity.class));
                break;

            case R.id.toolbar_select_all:
                selectAllOrDeselectAll();
                break;
            case R.id.toolbar_delete:
                deleteSelectedRecords();
                break;
            case R.id.toolbar_ignore:
                //todo set all selected into others category

                break;
            case R.id.add_edit_transaction_save_btn:
                Log.d("test_flow5", "save btn clicked!");
                break;



            default:
                return super.onOptionsItemSelected(item);
        }
        Log.d("test_flow2", "menu clickedddddddddddddddddddddddddddddddddddddddd");
        return true;
    }

    private void deleteAll() {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.transactionDAO().deleteAll();
            }
        });
    }


    @Override
    public void onBackPressed() {
        if (isInActionModel) {
            setToolBarToOriginMode();
        } else if(isInQuestionFragment){
            getSupportFragmentManager().popBackStack();
            btnFA.show();
        }else if(isInSettingsFragment){
            getSupportFragmentManager().popBackStack();
            btnFA.show();
        }else if(isInAddNEditFragment) {
            getSupportFragmentManager().popBackStack();
            btnFA.show();

        }
        else{
            if (BackPressHandler.isUserPressedTwice(this)) {
                super.onBackPressed();
            }
        }

    }


    private void viewmodelInitialization() {

        mDb = TransactionDB.getInstance(this);

        Date halfYear = LocalDate.now().minusDays(180).toDate();

        int time = UnitUtil.formatTime(halfYear);

        OverviewFragmentVMFactory factory = new OverviewFragmentVMFactory(time, mDb);
        mOverviewViewModel = ViewModelProviders.of(this, factory).
                get(OverviewFragmentViewModel.class);

        mTransactionViewModel = ViewModelProviders.of(this).get(TransactionDBViewModel.class);

        mAdapterActionViewModel = ViewModelProviders.of(this).get(AdapterNActionBarViewModel.class);



    }


    private void broadcastReceiverInitialization() {
        mSmsIntentFilter = new IntentFilter();
        mSmsIntentFilter.addAction(SMS_RECEIVED_ACTION);
        mSmsReceiver = new SmsBroadcastReceiver();
    }

    private void uiInitialization() {
        toolBar = findViewById(R.id.toolbar_layout);
        setSupportActionBar(toolBar);
        //todo, change to app Logo
        toolBar.setNavigationIcon(R.drawable.ic_toolbar_nagivation);
        textViewOnToolBar = findViewById(R.id.toolbar_textview);

        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(bottomNavigationListener);

        btnFA = findViewById(R.id.btn_floating_aciton);
        btnFA.setOnClickListener(fabOnClickListener);

    }

    private void setViewModelOberver() {

        mOverviewViewModel.getlistOfTransactionsInTimeRange().observe(this, new Observer<List<TransactionEntry>>() {
            @Override
            public void onChanged(List<TransactionEntry> transactionEntryList) {
                Log.d("test_flow3", "main_activity, should be called one time");
                calculateSpendingNRevenueSum(transactionEntryList);
            }
        });



        mAdapterActionViewModel.getActionModeState().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                /*
                    only handle toolbar action mode start (comes from user long click),
                    end of (toolbar) action mode will be one of following :
                    1. press android.R.id.home
                    2. press back button
                    3. press mEditBtn (goes to another activity for editing)
                    4. press mDeleteBtn (after user select some data)
                    5. press mIgnoreBtn (automatically mark all transaciton in category others)
                 */
                isInActionModel = aBoolean;
                if (isInActionModel) {
                    setToolBarToActionMode();
                }
            }

        });


        mAdapterActionViewModel.getTransactionSelectedNumberLiveData().observe( this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                /*
                    toolbar's respondse to user selction of records
                */

                mNumberOfSelection = integer;

                showNumberOfSelectedTransactionOnToolbar(integer);

                displayToolbarIconsBasedOnNumberOfSelections(integer);

            }
        });

        mAdapterActionViewModel.getmIsAllSelected().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                isAllSelected = aBoolean;
            }
        });

        mAdapterActionViewModel.getmIsInQuestionFragment().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                Log.d("test_flow5", "onChanged in Mainactivity the viewmodel is "+mAdapterActionViewModel.hashCode());
                isInQuestionFragment = aBoolean;
                Log.d("test_flow5", "same viewmodel value is "+aBoolean);

                if(aBoolean){
                    resetAdapterActionViewModelForEnteringAnotherFragment();
                    bottomNavigation.setVisibility(View.GONE);
                    btnFA.hide();
                }else{
                    setToolBarToOriginMode();
                    bottomNavigation.setVisibility(View.VISIBLE);
                }
            }
        });

        mAdapterActionViewModel.getmIsInSettingsFragment().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                isInSettingsFragment = aBoolean;
                if(aBoolean){
                    resetAdapterActionViewModelForEnteringAnotherFragment();
                    bottomNavigation.setVisibility(View.GONE);
                    btnFA.hide();
                }else{
                    setToolBarToOriginMode();
                    bottomNavigation.setVisibility(View.VISIBLE);
                }
            }
        });

        mAdapterActionViewModel.getmClickedEntryID().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                Log.d("test_flow11", "main activity getmClickedEntryID onChanged: "+integer);
                //start the fragment
                if(integer != null){
                   openAddNEditTransactionFragment();
                }
            }
        });

        mAdapterActionViewModel.getmIsInAddNEditFragment().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                isInAddNEditFragment = aBoolean;
                if(aBoolean){
                    Log.d("test_flow4", "this should triggered after enter new fragment");
                    resetAdapterActionViewModelForEnteringAnotherFragment();
                    bottomNavigation.setVisibility(View.GONE);
                    btnFA.hide();
                }else{
                    setToolBarToOriginMode();
                    bottomNavigation.setVisibility(View.VISIBLE);
                }
            }
        });


    }



    private void resetAdapterActionViewModelForEnteringAnotherFragment(){
        mAdapterActionViewModel.emptySelectedItems();
        mAdapterActionViewModel.setActionModeState(false);


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


    //todo, FAB should create a new fragment
    private FloatingActionButton.OnClickListener fabOnClickListener
            = new FloatingActionButton.OnClickListener() {
        @Override
        public void onClick(View view) {

            mAdapterActionViewModel.setmClickedEntryIDToNull();
            openAddNEditTransactionFragment();

        }
    };


    private void calculateSpendingNRevenueSum(List<TransactionEntry> transactionEntryList) {
        float revenue = 0;
        float spending = 0;
        for (TransactionEntry entry : transactionEntryList) {
            if (entry.getAmount() >= 0) {
                revenue = revenue + entry.getAmount();
            } else {
                spending = spending + entry.getAmount();
            }
        }

        mOverviewViewModel.setRevenue(revenue);
        mOverviewViewModel.setSpend(spending);

    }

    private void displayToolbarIconsBasedOnNumberOfSelections(int integer) {

        if (isInActionModel) {
            if (mEditBtn != null && mIgnoreBtn != null) {
                if (mAdapterActionViewModel.getCurrentLedger().equals(Constant.CALLFROMOVERVIEW)) {

                    toolbarActionsIfCalledFromOverViewFragment(integer);
                } else {
                    toolbarAcitonsIfCalledFromLedgerFragment(integer);
                }
            }
        }
    }

    private void toolbarActionsIfCalledFromOverViewFragment(int integer) {
        /*  when selected number ==0, show icon: mSelectAllBtn and mDeleteBtn
         *   when selected number ==1, show icon: mEditBtn, mIgnoreBtn, mSelectAllBtn and mDeleteBtn
         *   when selected number >1, show icon: mIgnoreBtn, mSelectAllBtn and mDeleteBtn
         * */
        if (integer < 1) {
            mIgnoreBtn.setVisible(false);
            mEditBtn.setVisible(false);
        } else if (integer == 1) {
            mIgnoreBtn.setVisible(true);
            mEditBtn.setVisible(true);
        } else {
            //more than 1 items is selected
            mIgnoreBtn.setVisible(true);
            mEditBtn.setVisible(false);

        }
    }

    private void toolbarAcitonsIfCalledFromLedgerFragment(int integer) {
        /*
                if current fragment is the ledger fragment
                (which is mostly likely already been tagged),
                not to display selectALl btn icon, because I don't want user
                accidently hit mSelectAllBtn and hit mDeleteBtn.


        *   when selected number ==1, show icon: mEditBtn, and mDeleteBtn
        *   when selected number ==0 or more than 1, show icon:mDeleteBtn
        */
        mIgnoreBtn.setVisible(false);
        mSelectAllBtn.setVisible(false);
        mDeleteBtn.setVisible(true);
        if (integer != 1) {
            mEditBtn.setVisible(false);
        } else {
            mEditBtn.setVisible(true);

        }
    }


    private void activityStartRunFragment() {
        selectedFragment = new OverviewFragment();
        switchFragmentWithinActivity(selectedFragment);
    }

    private void switchFragmentWithinActivity(Fragment input) {

        setToolBarToOriginMode();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.base_fragment, input)
                .commit();
    }

    //todo, input id from here
    private void openAddNEditTransactionFragment() {

        selectedFragment = new AddNEditTransactionFragment();
        addCurrentFragmentToBack(selectedFragment);


    }

    private void addCurrentFragmentToBack(Fragment input){


        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.base_fragment, input)
                .addToBackStack(null)
                .commit();
        Log.d("flow_test4", "end of entering new fragmnet");
    }

    private void setToolBarToOriginMode() {
        Log.d("test_flow4", "setToolBarToOriginMode: ");
        toolBar.getMenu().clear();
        toolBar.setTitle(R.string.app_name);
        toolBar.inflateMenu(R.menu.toolbar_normal_mode);
        toolBar.setNavigationIcon(R.drawable.ic_toolbar_nagivation);
        textViewOnToolBar.setVisibility(View.GONE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        mAdapterActionViewModel.emptySelectedItems();
        mAdapterActionViewModel.setActionModeState(false);

    }

    private void setToolBarToActionMode() {

        toolBar.getMenu().clear();
        toolBar.setTitle(R.string.empty_string);
        toolBar.inflateMenu(R.menu.toolbar_action_mode);
        textViewOnToolBar.setVisibility(View.VISIBLE);

        assignMenuItemToVariableForDifferentCombinationNSetInitialState();
        if (mAdapterActionViewModel.getCurrentLedger().equals(Constant.CALLFROMLEDGER)) {

            mSelectAllBtn.setVisible(false);
        } else {
            mSelectAllBtn.setVisible(true);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }



    private void assignMenuItemToVariableForDifferentCombinationNSetInitialState() {

        /*  the mIgnoreBtn btn is for auto-set selected item to 'Others' category
         *  when entering toolbar action mode, no item has selected,
         *  there is not need to display mEditBtn and mIgnoreBtn
         * */
        mIgnoreBtn = toolBar.getMenu().findItem(R.id.toolbar_ignore);
        mEditBtn = toolBar.getMenu().findItem(R.id.toolbar_edit);
        mSelectAllBtn = toolBar.getMenu().findItem(R.id.toolbar_select_all);
        mDeleteBtn = toolBar.getMenu().findItem(R.id.toolbar_delete);
        mIgnoreBtn.setVisible(false);
        mEditBtn.setVisible(false);
    }

    private void insert20FakeData() {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.transactionDAO().insertListOfTransactions(FakeTestingData.create20Transactions());
            }
        });
    }


    private void insert10000FakeData() {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.transactionDAO().insertListOfTransactions(FakeTestingData.create10kTransactions());
            }
        });
    }

    private void deleteSelectedRecords() {
        if (mNumberOfSelection == 0) {
            Toast.makeText(this,
                    getResources().getString(R.string.msg_deleting_need_to_have_one),
                    Toast.LENGTH_LONG).show();
        } else {

            final List<TransactionEntry> entries;
            if(mAdapterActionViewModel.getCurrentLedger().equals(Constant.CALLFROMOVERVIEW)){
                entries = mOverviewViewModel.getUntaggedTransactions().getValue();
            }else{
                entries = mTransactionViewModel.getAllTransactions().getValue();
            }

            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {

                    mDb.transactionDAO().deleteListOfTransactions(
                            mAdapterActionViewModel.getSelectedTransactions(entries)
                    );
                }
            });
            Toast.makeText(this,
                    getResources().getString(R.string.msg_deleting_complete),
                    Toast.LENGTH_LONG).show();
            setToolBarToOriginMode();
        }
    }

    private void selectAllOrDeselectAll() {
         /* select/de-select all the transctions in the view
            use viewmodel to send signal to Recyclerview (the fragment which run recyclerview
            is observing these triggers),
            the fragment will perform operations after receive the signal,
            after finish operation, the fragment will set trigger to false
         */
        if (isAllSelected) {
            mAdapterActionViewModel.setDeselectAllTrigger(true);
        } else {
            mAdapterActionViewModel.setSelectAllTrigger(true);

        }
    }

    private void showNumberOfSelectedTransactionOnToolbar(int integer) {
        String display = integer + " " +
                getResources().getString(R.string.string_toolbar_selection_word);
        textViewOnToolBar.setText(display);
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
