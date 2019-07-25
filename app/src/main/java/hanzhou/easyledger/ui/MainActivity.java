package hanzhou.easyledger.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import hanzhou.easyledger.R;
import hanzhou.easyledger.smsprocessor.HistoryRemark;
import hanzhou.easyledger.smsprocessor.SMSBroadcastReceiver;
import hanzhou.easyledger.ui.settings.SettingMain;
import hanzhou.easyledger.utility.GsonHelper;
import hanzhou.easyledger.utility.UnitUtil;
import hanzhou.easyledger.viewmodel.sharedpreference_viewmodel.SPViewModelFactory;
import hanzhou.easyledger.viewmodel.sharedpreference_viewmodel.SPViewModel;
import hanzhou.easyledger.chart_personalization.ChartDialogSetting;
import hanzhou.easyledger.data.AppExecutors;
import hanzhou.easyledger.data.TransactionDB;
import hanzhou.easyledger.data.TransactionEntry;
import hanzhou.easyledger.utility.BackPressHandler;
import hanzhou.easyledger.utility.Constant;
import hanzhou.easyledger.utility.TestingData;
import hanzhou.easyledger.viewmodel.AdapterNActionBarViewModel;
import hanzhou.easyledger.viewmodel.OverviewFragmentViewModel;
import hanzhou.easyledger.viewmodel.TransactionDBViewModel;
import hanzhou.easyledger.viewmodel.sharedpreference_viewmodel.SettingsViewModel;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import net.danlew.android.joda.JodaTimeAndroid;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.provider.Telephony.Sms.Intents.SMS_RECEIVED_ACTION;

public class MainActivity extends AppCompatActivity {

    public static final String SMS_RECEIVED_ACTION_OLD = "android.provider.Telephony.SMS_RECEIVED";

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final String CHART_FRAGMENT = "in_chart_fragment";
    private static final String NON_CHART_FRAGMENT = "not_in_chart_fragment";

    private IntentFilter mSmsIntentFilter;
    private SMSBroadcastReceiver mSmsReceiver;

    /* Database instance */
    private TransactionDB mDb;
    private TransactionDBViewModel mTransactionViewModel;
    private OverviewFragmentViewModel mOverviewViewModel;
    private AdapterNActionBarViewModel mAdapterActionViewModel;

    private HistoryRemark mHistoryRemark;


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

//    private boolean isInQuestionFragment;
//    private boolean isInSettingsFragment;
//    private boolean isInAddNEditFragment;
//    private boolean isInEditLedgerFragment;


    private boolean mIsInBaseFragment;

    private int mNumberOfSelection;

    private int mOverviewDateRange;
    private int mOverviewDateStartTime;
    private boolean isOverviewCustomRange;


    private SharedPreferences mSharedPreference;
    private GsonHelper mGsonHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*Initialize JodaTime library*/
        JodaTimeAndroid.init(this);

        initializeSharedPreference();

        broadcastReceiverInitialization();

        uiInitialization();

        setViewModel();

        activityStartRunFragment();
    }

    private void initializeSharedPreference() {

        mSharedPreference = androidx.preference.PreferenceManager.getDefaultSharedPreferences(this);

        String resultRev = mSharedPreference.getString(Constant.CATEGORY_TYPE_REVENUE, null);
        String resultExp = mSharedPreference.getString(Constant.CATEGORY_TYPE_EXPENSE, null);
        String resultLedger = mSharedPreference.getString(Constant.LEDGERS, null);

        mGsonHelper = GsonHelper.getInstance();
        mGsonHelper.setmSharedPreferences(mSharedPreference);

        /*only populates the default category one when first-mOverviewDateStartTime run the app*/

        if (resultRev == null) {

            mGsonHelper.saveDataToSharedPreference(
                    new ArrayList<>(Arrays.asList(Constant.DEFAULT_CATEGORIES_REVENUE)),
                    Constant.CATEGORY_TYPE_REVENUE);
        }

        if (resultExp == null) {

            mGsonHelper.saveDataToSharedPreference(
                    new ArrayList<>(Arrays.asList(Constant.DEFAULT_CATEGORIES_EXPENSE)),
                    Constant.CATEGORY_TYPE_EXPENSE);
        }

        if (resultLedger == null) {
            mGsonHelper.saveDataToSharedPreference(new ArrayList<>(Arrays.asList(Constant.DEFAULT_LEDGER)), Constant.LEDGERS);
        }

        isOverviewCustomRange = false;

    }


    @Override
    protected void onResume() {
        super.onResume();
        this.registerReceiver(mSmsReceiver, mSmsIntentFilter);

    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(mSmsReceiver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_normal_mode, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_setting_mainactivity:
                selectedFragment = new SettingMain();
                addBaseFragmentToBack(selectedFragment);

                break;
            case R.id.menu_user_has_question:
                selectedFragment = new QuestionFragment();
                addBaseFragmentToBack(selectedFragment);
                break;
            case R.id.menu_feedback:
                sendEmailToDeveloper();
                break;
            case R.id.menu_insert_data:
                insert1000FakeData();
                break;
            case R.id.menu_delete_all_data:
                deleteAll();
                break;
            case R.id.menu_insert_data_within7days:
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        mDb.transactionDAO().insertListOfTransactions(
                                TestingData.create10DesignateTestingDataInCertaindays(7, mGsonHelper));
                    }
                });
                break;
            case R.id.menu_insert_data_within30days:
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        mDb.transactionDAO().insertListOfTransactions(
                                TestingData.create10DesignateTestingDataInCertaindays(30, mGsonHelper));
                    }
                });
                break;
            case R.id.menu_insert_data_within180days:
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        mDb.transactionDAO().insertListOfTransactions(
                                TestingData.create10DesignateTestingDataInCertaindays(180, mGsonHelper));
                    }
                });

                break;
            case R.id.menu_insert_data_within_this_week:
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        mDb.transactionDAO().insertListOfTransactions(
                                TestingData.create10DesignateTestingDataInCurrentWeek(mGsonHelper));
                    }
                });

                break;
            case R.id.menu_insert_data_within_this_month:
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        mDb.transactionDAO().insertListOfTransactions(
                                TestingData.create10DesignateTestingDataInCurrentMonth(mGsonHelper));
                    }
                });
                break;

            case R.id.menu_insert_untagged_data:
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        mDb.transactionDAO().insertListOfTransactions(
                                TestingData.create5UntaggedTransactions(mGsonHelper));
                    }
                });
                break;

            case android.R.id.home:
                if (isInActionModel) {
                    setToolBarToOriginMode();
                } else if (!mIsInBaseFragment) {
                    Log.d("test_test", "home : ");
                    getSupportFragmentManager().popBackStack();
                }

                break;

            case R.id.toolbar_edit:
                editSelectedTransaction();
                break;

            case R.id.toolbar_select_all:
                selectAllOrDeselectAll();
                break;
            case R.id.toolbar_delete:
                deleteSelectedRecords();
                setToolBarToOriginMode();
                break;
            case R.id.toolbar_ignore:
                mAdapterActionViewModel.setmCategorizeItemsToOthersTrigger(true);
//                categorizeSelectedItemsToOthers();
                setToolBarToOriginMode();
                break;

            default:
                return super.onOptionsItemSelected(item);
        }
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
        } else if (!mIsInBaseFragment) {
            Log.d("test_test", "onBackPressed: ");
            getSupportFragmentManager().popBackStack();
        } else {
            if (BackPressHandler.isUserPressedTwice(this)) {
                super.onBackPressed();
            }
        }

    }


    private void broadcastReceiverInitialization() {

        mHistoryRemark = HistoryRemark.getInstance();
        mHistoryRemark.loadFromFile(mGsonHelper);

        mSmsIntentFilter = new IntentFilter();

        //todo , handle this
        //todo, may crush
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
        && Build.VERSION.SDK_INT< Build.VERSION_CODES.KITKAT){
            mSmsIntentFilter.addAction(SMS_RECEIVED_ACTION_OLD);
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){

            mSmsIntentFilter.addAction(SMS_RECEIVED_ACTION);
        }

        mSmsReceiver = new SMSBroadcastReceiver();
        mSmsReceiver.setmSharedPreferences(mSharedPreference);
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


    }

    private void setViewModel() {

        SPViewModelFactory spFactory = new SPViewModelFactory(mSharedPreference);
        SPViewModel mSharedPreferenceViewModel = ViewModelProviders.of(this, spFactory).get(SPViewModel.class
        );

        mSharedPreferenceViewModel.getmSettingOverviewCustomDateRange().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                mOverviewDateRange = integer;
                Log.d("test_overview", " date range " + mOverviewDateRange);

                if (isOverviewCustomRange) {
                    mOverviewDateStartTime = UnitUtil.getStartingDateCurrentCustom(mOverviewDateRange);

                }
                if (mOverviewViewModel == null) {

                    mOverviewViewModel = ViewModelProviders.of(MainActivity.this).
                            get(OverviewFragmentViewModel.class);
                }
                mOverviewViewModel.updateTransactionOverviewPeriod(mOverviewDateStartTime);
            }
        });


        mSharedPreferenceViewModel.getmSettingOverviewDateRangeType().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s.equals(Constant.SETTING_GENERAL_OVERVIEW_DATE_RANGE_BY_MONTH)) {
                    isOverviewCustomRange = false;
                    mOverviewDateStartTime = UnitUtil.getStartingDateCurrentMonth();

                } else if (s.equals(Constant.SETTING_GENERAL_OVERVIEW_DATE_RANGE_BY_WEEK)) {
                    isOverviewCustomRange = false;
                    mOverviewDateStartTime = UnitUtil.getStartingDateCurrentWeek();

                } else {
                    /*custom range*/
                    isOverviewCustomRange = true;
                    mOverviewDateStartTime = UnitUtil.getStartingDateCurrentCustom(mOverviewDateRange);
                }


                if (mOverviewViewModel == null) {

                    mOverviewViewModel = ViewModelProviders.of(MainActivity.this).
                            get(OverviewFragmentViewModel.class);
                }
                mOverviewViewModel.updateTransactionOverviewPeriod(mOverviewDateStartTime);

            }
        });
        mSharedPreferenceViewModel.getmIsAutoTaggerOn().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                Constant.setIsAutoTaggerOn(aBoolean);
            }
        });

        mDb = TransactionDB.getInstance(this);


        mTransactionViewModel = ViewModelProviders.of(this).get(TransactionDBViewModel.class);

        mAdapterActionViewModel = ViewModelProviders.of(this).get(AdapterNActionBarViewModel.class);


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


        mAdapterActionViewModel.getTransactionSelectedNumberLiveData().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                /*
                    toolbar's response to user selction of records
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

        mAdapterActionViewModel.getmIsInBaseFragment().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                mIsInBaseFragment = aBoolean;
                if (aBoolean) {
                    btnFA.show();
                    bottomNavigation.setVisibility(View.VISIBLE);
                    setToolBarToOriginMode();
                } else {
                    btnFA.hide();
                    bottomNavigation.setVisibility(View.GONE);
                    setToolBarToOriginMode();
                }
            }
        });


        mAdapterActionViewModel.getmClickedEntryID().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                Log.d("test_flow11", "main activity getmClickedEntryID onChanged: " + integer);
                //start the fragment
                if (integer != null) {
                    openAddNEditTransactionFragment();
                }
            }
        });

        final SettingsViewModel mSettingsViewModel = ViewModelProviders.of(this).get(SettingsViewModel.class);
        mSettingsViewModel.getmRefreshLedgerFragmentTrigger().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                    switchFragmentWithinActivity(new LedgerFragment());

                    mSettingsViewModel.setmRefreshLedgerFragmentTrigger(false);
                }
            }
        });


    }


    private BottomNavigationView.OnNavigationItemSelectedListener bottomNavigationListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {


                    switch (menuItem.getItemId()) {
                        case R.id.navigation_overview:
                            selectedFragment = new OverviewFragment();
                            setFloatingActionBtn(selectedFragment);

                            break;
                        case R.id.navigation_transaction:
                            selectedFragment = new LedgerFragment();
                            setFloatingActionBtn(selectedFragment);

                            break;
                        case R.id.navigation_charts:
                            selectedFragment = new ChartFragment();
                            setFloatingActionBtn(selectedFragment);
                            break;

                    }

                    switchFragmentWithinActivity(selectedFragment);

                    return true;
                }
            };


    private FloatingActionButton.OnClickListener fabOnClickListenerOpenFragment
            = new FloatingActionButton.OnClickListener() {
        @Override
        public void onClick(View view) {

            mAdapterActionViewModel.setmClickedEntryIDToNull();
            openAddNEditTransactionFragment();

        }
    };
    private FloatingActionButton.OnClickListener fabOnClickListenerOpenSettingDialog
            = new FloatingActionButton.OnClickListener() {
        @Override
        public void onClick(View view) {


            ChartDialogSetting dialogSetting = new ChartDialogSetting();

            dialogSetting.show(getSupportFragmentManager(), null);


        }
    };

    private FloatingActionButton.OnClickListener fabOnClickListenerAddEntry = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

        }
    };


    private void displayToolbarIconsBasedOnNumberOfSelections(int integer) {

        if (isInActionModel) {
            if (mEditBtn != null && mIgnoreBtn != null) {
                if (mAdapterActionViewModel.getParentFragment().equals(Constant.FRAG_CALL_FROM_OVERVIEW)) {

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
        setFloatingActionBtn(selectedFragment);

        switchFragmentWithinActivity(selectedFragment);
    }

    private void switchFragmentWithinActivity(Fragment input) {

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.base_fragment, input)
                .commit();
    }

    //todo, input id from here
    private void openAddNEditTransactionFragment() {

        selectedFragment = new AddNEditTransactionFragment();
        addBaseFragmentToBack(selectedFragment);
    }

    private void addBaseFragmentToBack(Fragment input) {

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.base_fragment, input)
                .addToBackStack(null)
                .commit();
    }

    private void setToolBarToOriginMode() {

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
        if (mAdapterActionViewModel.getParentFragment().equals(Constant.FRAG_CALL_FROM_LEDGER)) {

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

    private void insert1000FakeData() {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.transactionDAO().insertListOfTransactions(TestingData.create1000Transactions(mGsonHelper));
            }
        });
    }


    private void insert10000FakeData() {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.transactionDAO().insertListOfTransactions(TestingData.create10kTransactions());
            }
        });
    }


    private void editSelectedTransaction() {

        if (mNumberOfSelection == 1) {
            mAdapterActionViewModel.setmEditAnEntryTrigger(true);

        } else {
            Log.d(TAG, "editSelectedTransaction: unknow error causing mNumberOfSelect != 1 while edit btn shows");
        }
    }

    private void deleteSelectedRecords() {
        if (mNumberOfSelection == 0) {
            Toast.makeText(this,
                    getResources().getString(R.string.msg_deleting_need_to_have_one),
                    Toast.LENGTH_LONG).show();
        } else {

//            final List<TransactionEntry> entries;
////            if (mAdapterActionViewModel.getParentFragment().equals(Constant.FRAG_CALL_FROM_OVERVIEW)) {
////                entries = mOverviewViewModel.getUntaggedTransactions().getValue();
////                Log.d("test_delete", "deleteSelectedRecords:   from untagged transactions");
////            } else {
////                entries = mTransactionViewModel.getAllTransactions().getValue();
////                Log.d("test_delete", "deleteSelectedRecords:   from all transactions ");
////
////            }
//            entries = mTransactionViewModel.getTransactionsByLedger().getValue();
//
//            AppExecutors.getInstance().diskIO().execute(new Runnable() {
//                @Override
//                public void run() {
//
//                    mDb.transactionDAO().deleteListOfTransactions(
//                            mAdapterActionViewModel.getSelectedTransactions(entries)
//                    );
//                }
//            });

            mAdapterActionViewModel.setmDeleteItemTrigger(true);
            Toast.makeText(this,
                    getResources().getString(R.string.msg_deleting_complete),
                    Toast.LENGTH_LONG).show();

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

//    private void categorizeSelectedItemsToOthers() {
//        final List<TransactionEntry> entries = mOverviewViewModel.getUntaggedTransactions().getValue();
//
//        AppExecutors.getInstance().diskIO().execute(new Runnable() {
//            @Override
//            public void run() {
//                mDb.transactionDAO().updateListOfTransactions(
//                        mAdapterActionViewModel.categorizeSelectedItemsToOthers(entries)
//                );
//            }
//        });
//
//    }


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


    //todo, change viewmodel

    private void setFloatingActionBtn(Fragment inputFragment) {
        if (inputFragment instanceof ChartFragment) {
            btnFA.setImageResource(R.drawable.ic_chart_setting);
            btnFA.show();
            btnFA.setOnClickListener(null);
            btnFA.setOnClickListener(fabOnClickListenerOpenSettingDialog);

        } else if (inputFragment instanceof OverviewFragment || inputFragment instanceof LedgerFragment) {
            btnFA.setImageResource(R.drawable.icon_floating_action_btn_add);
            btnFA.show();
            btnFA.setOnClickListener(null);

            btnFA.setOnClickListener(fabOnClickListenerOpenFragment);
        }

    }


}
