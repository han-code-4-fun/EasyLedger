package hanzhou.easyledger.ui;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.Set;

import hanzhou.easyledger.R;
import hanzhou.easyledger.data.AppExecutors;
import hanzhou.easyledger.data.TransactionDB;
import hanzhou.easyledger.data.TransactionEntry;
import hanzhou.easyledger.smsprocessor.HistoryRemark;
import hanzhou.easyledger.utility.Constant;
import hanzhou.easyledger.utility.GsonHelper;
import hanzhou.easyledger.utility.UnitUtil;
import hanzhou.easyledger.viewadapter.CategoryAdapter;
import hanzhou.easyledger.viewmodel.AdapterNActionBarViewModel;
import hanzhou.easyledger.viewmodel.AddTransactionVMFactory;
import hanzhou.easyledger.viewmodel.AddTransactionViewModel;
import hanzhou.easyledger.viewmodel.GeneralViewModel;
import hanzhou.easyledger.viewmodel.sharedpreference_viewmodel.SPViewModel;
import hanzhou.easyledger.viewmodel.sharedpreference_viewmodel.SettingsViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddNEditTransactionFragment extends Fragment
        implements DatePickerDialog.OnDateSetListener/*, View.OnFocusChangeListener*/ {

    // Constant for default task id to be used when not in update mode
    private static final int DEFAULT_TASK_ID = -1;

    // Extra for the task ID to be received after rotation
    public static final String INSTANCE_TASK_ID = "instanceTaskId";

    private TransactionDB mDB;

    private GeneralViewModel mGeneralViewModel;
    private AdapterNActionBarViewModel mAdapterActionViewModel;
    private SPViewModel mSPViewModel;
//    private AddTransactionViewModel mAddTransactionViewModel;

    private AppCompatActivity mAppCompatActivity;

    private Toolbar toolbar;

    private TextView mTvMoneyIn;
    private TextView mTvMoneyOut;
    private TextView mAmountLabel;
    private TextView mCategoryLabel;

    private ImageView mMoneyIn;
    private ImageView mMoneyOut;

    private EditText mEditTextAmount;
    private EditText mEditTextRemark;

    private TextView mTVCategory;
    private TextView mTVDate;
    private TextView mTVDateLabel;

    private Spinner mSpinner;

    private Button mMoneyFlowBtn;

    private FloatingActionButton mSaveBtn;

    private RecyclerView mCategoryRecyclerView;
    private CategoryAdapter mCategoryAdapter;
    private ArrayAdapter<String> mSpinnerLedgerAdapter;

    private SharedPreferences mSharedPreference;
    private GsonHelper mGsonHelper;


    private int mTransactionId = DEFAULT_TASK_ID;


    private boolean mIsAutotaggerOn;


    private double mMoneyNum;
    private int mCategoryNum;
    private int mLedgerNum;
    private int mDateNum;
    private String mRemark;

    private String mCurrentTransactionCategory;
    private int mPositionInSpinner;

    private HistoryRemark mHistoryRemark;
    private boolean mIsMoneyInChecked;


    public AddNEditTransactionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mAppCompatActivity = (AppCompatActivity) context;
        setHasOptionsMenu(true);
        mDB = TransactionDB.getInstance(getContext());
        mSharedPreference = PreferenceManager.getDefaultSharedPreferences(mAppCompatActivity);
        mGsonHelper = GsonHelper.getInstance();
        mGsonHelper.setmSharedPreferences(mSharedPreference);
        mIsMoneyInChecked = true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mHistoryRemark = HistoryRemark.getInstance();
        mHistoryRemark.loadFromFile(mGsonHelper);

        toolbar = mAppCompatActivity.findViewById(R.id.toolbar_layout);

        View rootView = inflater.inflate(R.layout.fragment_add_n_edit_transaction, container, false);

        setupUI(rootView);

        setupUIListenerForClosingSoftKeyboard(rootView);

        return rootView;

    }


    private void setupUI(View rootView) {

        mMoneyFlowBtn = rootView.findViewById(R.id.money_flow_change_btn);
        mMoneyFlowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mIsMoneyInChecked) {
                    setMoneyOutActive();
                } else {
                    setMoneyInActive();
                }
            }
        });

        mEditTextAmount = rootView.findViewById(R.id.add_edit_transaction_amount);

        mEditTextRemark = rootView.findViewById(R.id.add_edit_transaction_remark);

        mTVDate = rootView.findViewById(R.id.add_edit_transaction_date_view);
        mTVDate.setOnClickListener(txtViewDateClickListener);

        mTVDateLabel = rootView.findViewById(R.id.add_edit_transaction_date_label);

        mAmountLabel = rootView.findViewById(R.id.add_edit_transaction_amount_label);

        mCategoryLabel = rootView.findViewById(R.id.add_edit_transaction_category_label);

        mTVCategory = rootView.findViewById(R.id.add_edit_transaction_category_display_current);

        mMoneyIn = rootView.findViewById(R.id.add_edit_transaction_btn_money_in);

        mMoneyOut = rootView.findViewById(R.id.add_edit_transaction_btn_money_out);

        mCategoryRecyclerView = rootView.findViewById(R.id.add_edit_transaction_category_recycler_view);

        mSaveBtn = rootView.findViewById(R.id.add_edit_transaction_save_btn);
        mSaveBtn.setOnClickListener(saveBtnOnlickListener);

        mAdapterActionViewModel = ViewModelProviders.of(mAppCompatActivity).get(AdapterNActionBarViewModel.class);


        RecyclerView.LayoutManager layoutManager =
                new GridLayoutManager(getContext(), 3, RecyclerView.VERTICAL, false);
        mCategoryRecyclerView.setLayoutManager(layoutManager);
        mCategoryRecyclerView.setHasFixedSize(true);

        mCategoryAdapter = new CategoryAdapter(getContext(), mAdapterActionViewModel);

        mCategoryRecyclerView.setAdapter(mCategoryAdapter);


        mSpinnerLedgerAdapter = new ArrayAdapter<String>(
                mAppCompatActivity,
                android.R.layout.simple_spinner_item,
                notDisplayLedgerOVERALL(mGsonHelper.getLedgers(Constant.LEDGERS))
        );
        mSpinnerLedgerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mSpinner = rootView.findViewById(R.id.add_edit_transaction_spinner_choose_ledger);

        mSpinner.setAdapter(mSpinnerLedgerAdapter);
    }

    private void setupUIListenerForClosingSoftKeyboard(View rootView) {

       /*Set up touch listener for non-text box views to hide keyboard.*/
        if (!(rootView instanceof EditText)) {
            rootView.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard();
                    return false;
                }
            });
        }

        /*If a layout container, iterate over children and seed recursion.*/
        if (rootView instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) rootView).getChildCount(); i++) {
                View innerView = ((ViewGroup) rootView).getChildAt(i);
                setupUIListenerForClosingSoftKeyboard(innerView);
            }
        }


    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mSPViewModel = ViewModelProviders.of(mAppCompatActivity).get(SPViewModel.class);
        mSPViewModel.getmIsAutoTaggerOn().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                mIsAutotaggerOn = aBoolean;
            }
        });

        mGeneralViewModel = ViewModelProviders.of(mAppCompatActivity).get(GeneralViewModel.class);
        mGeneralViewModel.setmCurrentScreen(Constant.FRAG_NAME_ADD_EDIT_TRANSACTION);

        if (savedInstanceState != null && savedInstanceState.containsKey(INSTANCE_TASK_ID)) {
            mTransactionId = savedInstanceState.getInt(INSTANCE_TASK_ID, DEFAULT_TASK_ID);
        }

        mAdapterActionViewModel.getmClickedEntryID().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                mAdapterActionViewModel.getmClickedEntryID().removeObservers(getViewLifecycleOwner());
                if (integer != null) {
                    /*  edit an existing transaction*/
                    toolbar.setTitle(R.string.title_edit_transaction);
                    mTransactionId = integer;
                    AddTransactionVMFactory factory = new AddTransactionVMFactory(mDB, mTransactionId);

                    /* this viewmodel can only stay within the fragment */
                    final AddTransactionViewModel mAddTransactionViewModel = ViewModelProviders.of(AddNEditTransactionFragment.this, factory).get(AddTransactionViewModel.class);
                    mAddTransactionViewModel.getTransactionEntry().observe(getViewLifecycleOwner(), new Observer<TransactionEntry>() {
                        @Override
                        public void onChanged(TransactionEntry transactionEntry) {
                            mAddTransactionViewModel.getTransactionEntry().removeObservers(mAppCompatActivity);
                            populateUIWithExistingData(transactionEntry);
                        }
                    });

                } else {
                    /* add a new transaction*/
                    toolbar.setTitle(R.string.title_add_transaction);
                    setMoneyInActive();
                    mDateNum = -1;

                }

            }
        });

        mAdapterActionViewModel.getmSelectedCategory().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                mTVCategory.setText(s);
            }
        });

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        toolbar.getMenu().clear();
        inflater.inflate(R.menu.toolbar_empty, menu);
        mAppCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(INSTANCE_TASK_ID, mTransactionId);
        super.onSaveInstanceState(outState);
    }

    private void setMoneyInActive() {


        mIsMoneyInChecked = true;
        mMoneyIn.setVisibility(View.VISIBLE);
        mMoneyOut.setVisibility(View.GONE);
        mCategoryAdapter.setData(mGsonHelper.getDataFromSharedPreference(Constant.CATEGORY_TYPE_REVENUE));
        mCategoryAdapter.highlightExistingCategoryIfMatch(mCurrentTransactionCategory);
        mAdapterActionViewModel.setmSelectedCategory("");
        Toast.makeText(
                mAppCompatActivity,
                getString(R.string.add_edit_transaction_money_flow_change_msg_money_in),
                Toast.LENGTH_LONG).show();
    }


    private void setMoneyOutActive() {
        mIsMoneyInChecked = false;
        mCategoryAdapter.setData(mGsonHelper.getDataFromSharedPreference(Constant.CATEGORY_TYPE_EXPENSE));

        mCategoryAdapter.highlightExistingCategoryIfMatch(mCurrentTransactionCategory);

        mAdapterActionViewModel.setmSelectedCategory("");
        mMoneyOut.setVisibility(View.VISIBLE);
        mMoneyIn.setVisibility(View.GONE);
        Toast.makeText(
                mAppCompatActivity,
                getString(R.string.add_edit_transaction_money_flow_change_msg_money_out),
                Toast.LENGTH_LONG).show();


    }


    private TextView.OnClickListener txtViewDateClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            /*open date picker diaglo box*/
            int year = LocalDate.now().getYear();
            int month = LocalDate.now().getMonthOfYear() - 1;
            int day = LocalDate.now().getDayOfMonth();


            if (mTransactionId == DEFAULT_TASK_ID) {
                DatePickerDialog dialog = new DatePickerDialog(
                        mAppCompatActivity,
                        AddNEditTransactionFragment.this,
                        year,
                        month,
                        day);
                dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                dialog.show();

            } else {
                String tempDate = Integer.toString(mDateNum);
                year = Integer.parseInt(tempDate.substring(0, 2)) + 2000;
                month = Integer.parseInt(tempDate.substring(2, 4)) - 1;
                day = Integer.parseInt(tempDate.substring(4, 6));

                DatePickerDialog dialog = new DatePickerDialog(
                        mAppCompatActivity,
                        AddNEditTransactionFragment.this,
                        year,
                        month,
                        day
                );

                dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                dialog.show();
            }
        }
    };


    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

        mDateNum = UnitUtil.setDatePickerDateIntoAppIntDate(i, i1, i2);
        mTVDate.setText(UnitUtil.getTimeIntInMoreReadableFormat(mDateNum));

    }

    private FloatingActionButton.OnClickListener saveBtnOnlickListener
            = new FloatingActionButton.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (checkIfUserEnteredNecessaryDate()) {
                //String ledger, int time, Float amount, String category, String remark
                float tempAmount = Float.parseFloat(mEditTextAmount.getText().toString());
                if (!mIsMoneyInChecked) {

                    tempAmount = 0 - tempAmount;
                }
                String remark = mEditTextRemark.getText().toString();
                final TransactionEntry entry = new TransactionEntry(
                        mSpinner.getSelectedItem().toString(),
                        mDateNum,
                        tempAmount,
                        mCategoryAdapter.getClickedCategory(),
                        remark

                );


                if (mIsAutotaggerOn) {
                    /*
                        update HistoryRemark for auto-tagging
                        and apply Updates To Existing UntaggedTransaction

                    */
                    HistoryRemark.getInstance().synchronizeUserTaggingBehaviour(
                            remark,
                            mCategoryAdapter.getClickedCategory(),
                            mGsonHelper
                    );

                }

                /*save to DB*/
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (mTransactionId == DEFAULT_TASK_ID) {

                            mDB.transactionDAO().insertTransaction(entry);
                        } else {

                            /*update existing records*/
                            entry.setId(mTransactionId);
                            mDB.transactionDAO().updateTransaction(entry);
                        }

                        mGeneralViewModel.setmBackButtonPressTrigger(true);
                    }
                });

            }
        }
    };


    private void populateUIWithExistingData(TransactionEntry transactionEntry) {
        if (transactionEntry == null) {
            return;
        }
        if (transactionEntry.getAmount() >= 0) {
            setMoneyInActive();
        } else {
            setMoneyOutActive();
        }


        mMoneyNum = transactionEntry.getAmount();

        mEditTextAmount.setText(UnitUtil.displayPositiveMoney(mMoneyNum));

        mEditTextRemark.setText(transactionEntry.getRemark());

        //todo, check here
        if (mMoneyNum >= 0) {
        } else {
            mCategoryAdapter.setData(mGsonHelper.getDataFromSharedPreference(Constant.CATEGORY_TYPE_EXPENSE));
        }

        mCurrentTransactionCategory = transactionEntry.getCategory();
        mTVCategory.setText(mCurrentTransactionCategory);
        mCategoryAdapter.highlightExistingCategoryIfMatch(mCurrentTransactionCategory);

       /*select spinner for ledger*/
        mPositionInSpinner = mSpinnerLedgerAdapter.getPosition(transactionEntry.getLedger());
        if (mPositionInSpinner != -1) {
            mSpinner.setSelection(mPositionInSpinner);
        }

        mDateNum = transactionEntry.getTime();
        String date = UnitUtil.getTimeIntInMoreReadableFormat(transactionEntry.getTime());
        mTVDate.setText(date);

    }

    private boolean checkIfUserEnteredNecessaryDate() {
        boolean process = true;
        /*save new records*/
        if (mDateNum == -1) {
            mTVDateLabel.setBackground(getResources().getDrawable(R.drawable.datepicker_tv_background));
            mTVDateLabel.setText(getString(R.string.add_edit_transaction_warning_enter_date));
            process = false;
        } else {
            mTVDateLabel.setBackground(null);
            mTVDateLabel.setText(getString(R.string.add_edit_transaction_date));
        }
        if (mCategoryAdapter.getmActivePosition() == -1) {
            process = false;
            mCategoryLabel.setBackground(getResources().getDrawable(R.drawable.datepicker_tv_background));
            mCategoryLabel.setText(getString(R.string.add_edit_transaction_warning_enter_categoty));
        } else {
            mCategoryLabel.setBackground(null);
            mCategoryLabel.setText(getString(R.string.add_edit_transaction_category));
        }

        if (mEditTextAmount.getText().toString().length() == 0 || Float.parseFloat(mEditTextAmount.getText().toString()) == 0) {
            mAmountLabel.setBackground(getResources().getDrawable(R.drawable.datepicker_tv_background));
            mAmountLabel.setText(getString(R.string.add_edit_transaction_warning_enter_amount));
            process = false;

        } else {
            mAmountLabel.setBackground(null);
            mAmountLabel.setText(getString(R.string.add_edit_transaction_amount));
        }

        return process;
    }


    /*the "OVERALL" ledger is a 'virtual ledger' for displaying all the other ledgers
     * when adding/editing transaction, there is no need to that*/
    private ArrayList<String> notDisplayLedgerOVERALL(ArrayList<String> input) {

        input.remove(Constant.LEDGER_OVERALL);
        return input;
    }

    public void hideSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) mAppCompatActivity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            View view = mAppCompatActivity.getCurrentFocus();

            if (view != null) {
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }


}
