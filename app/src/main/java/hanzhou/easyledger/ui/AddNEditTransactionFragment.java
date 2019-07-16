package hanzhou.easyledger.ui;


import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sackcentury.shinebuttonlib.ShineButton;

import org.joda.time.LocalDate;

import hanzhou.easyledger.R;
import hanzhou.easyledger.data.AppExecutors;
import hanzhou.easyledger.data.TransactionDB;
import hanzhou.easyledger.data.TransactionEntry;
import hanzhou.easyledger.utility.FakeTestingData;
import hanzhou.easyledger.utility.UnitUtil;
import hanzhou.easyledger.viewadapter.CategoryAdapter;
import hanzhou.easyledger.viewmodel.AdapterNActionBarViewModel;
import hanzhou.easyledger.viewmodel.AddTransactionVMFactory;
import hanzhou.easyledger.viewmodel.AddTransactionViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddNEditTransactionFragment extends Fragment
        implements DatePickerDialog.OnDateSetListener {

    // Constant for default task id to be used when not in update mode
    private static final int DEFAULT_TASK_ID = -1;

    // Extra for the task ID to be received after rotation
    public static final String INSTANCE_TASK_ID = "instanceTaskId";

    private TransactionDB mDB;

    private AdapterNActionBarViewModel mAdapterActionViewModel;

//    private AddTransactionViewModel mAddTransactionViewModel;

    private AppCompatActivity mAppCompatActivity;

    private Toolbar toolbar;

    private TextView mTvMoneyIn;
    private TextView mTvMoneyOut;
    private TextView mAmountLabel;
    private TextView mCategoryLabel;

    private ShineButton mMoneyIn;
    private ShineButton mMoneyOut;

    private EditText mEditTextAmount;
    private EditText mEditTextRemark;

    private TextView mTVCategory;
    private TextView mTVDate;
    private TextView mTVDateLabel;

    private Spinner mSpinner;

    private FloatingActionButton mSaveBtn;

    private RecyclerView mCategoryRecyclerView;
    private CategoryAdapter mCategoryAdapter;
    private ArrayAdapter<String> mSpinnerLedgerAdapter;

//    private boolean mIsNewTransaction;

    private int mTransactionId = DEFAULT_TASK_ID;

    private double mMoneyNum;
    private int mCategoryNum;
    private int mLedgerNum;
    private int mDateNum;
    private String mRemark;

    private String mCurrentTransactionCategory;
    private int mPositionInSpinner;


    public AddNEditTransactionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mAppCompatActivity = (AppCompatActivity) context;
        setHasOptionsMenu(true);
        mDB = TransactionDB.getInstance(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("test_flow4", "onCreateView");

        toolbar = mAppCompatActivity.findViewById(R.id.toolbar_layout);

//        mIsNewTransaction = false;
        View rootView = inflater.inflate(R.layout.fragment_add_n_edit_transaction, container, false);


        mEditTextAmount = rootView.findViewById(R.id.add_edit_transaction_amount);

        mEditTextRemark = rootView.findViewById(R.id.add_edit_transaction_remark);

        mTVDate = rootView.findViewById(R.id.add_edit_transaction_date_view);
        mTVDate.setOnClickListener(txtViewDateClickListener);

        mTVDateLabel = rootView.findViewById(R.id.add_edit_transaction_date_label);

        mAmountLabel = rootView.findViewById(R.id.add_edit_transaction_amount_label);

        mCategoryLabel = rootView.findViewById(R.id.add_edit_transaction_category_label);

        mTVCategory = rootView.findViewById(R.id.add_edit_transaction_category_display_current);

        mMoneyIn = rootView.findViewById(R.id.add_edit_transaction_btn_money_in);
        mMoneyIn.init(mAppCompatActivity);

        mTvMoneyIn = rootView.findViewById(R.id.add_edit_transaction_tv_money_in);


        mMoneyOut = rootView.findViewById(R.id.add_edit_transaction_btn_money_out);
        mMoneyOut.init(mAppCompatActivity);

        mTvMoneyOut = rootView.findViewById(R.id.add_edit_transaction_tv_money_out);

        mCategoryRecyclerView = rootView.findViewById(R.id.add_edit_transaction_category_recycler_view);

        mSaveBtn = rootView.findViewById(R.id.add_edit_transaction_save_btn);
        mSaveBtn.setOnClickListener(saveBtnOnlickListener);

        Log.d("test_flow4", "before create view model");
        mAdapterActionViewModel = ViewModelProviders.of(getActivity()).get(AdapterNActionBarViewModel.class);

        //setup recyclerview
        RecyclerView.LayoutManager layoutManager =
                new GridLayoutManager(getContext(), 3, RecyclerView.VERTICAL, false);
        mCategoryRecyclerView.setLayoutManager(layoutManager);
        mCategoryRecyclerView.setHasFixedSize(true);

        mCategoryAdapter = new CategoryAdapter(getContext(), mAdapterActionViewModel);

        mCategoryRecyclerView.setAdapter(mCategoryAdapter);


        //setup Spinner
        mSpinnerLedgerAdapter = new ArrayAdapter<String>(
                mAppCompatActivity,
                android.R.layout.simple_spinner_item,
                FakeTestingData.getLedgers());
        mSpinnerLedgerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mSpinner = rootView.findViewById(R.id.add_edit_transaction_spinner_choose_ledger);

        mSpinner.setAdapter(mSpinnerLedgerAdapter);


        return rootView;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("test_flow4", "on activity created");


        mAdapterActionViewModel.setmIsInAddNEditFragment(true);

        if (savedInstanceState != null && savedInstanceState.containsKey(INSTANCE_TASK_ID)) {
            mTransactionId = savedInstanceState.getInt(INSTANCE_TASK_ID, DEFAULT_TASK_ID);
        }

        mAdapterActionViewModel.getmClickedEntryID().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                Log.d("test_flow11", "getmClickedEntryID observer onChanged: " + integer);
                mAdapterActionViewModel.getmClickedEntryID().removeObservers(getViewLifecycleOwner());
                if (integer != null) {
                    Log.d("test_flow8", "getmClickedEntryID the id is " + integer);
                    /*  edit an existing transaction*/
                    toolbar.setTitle(R.string.title_edit_transaction);
                    mTransactionId = integer;
                    AddTransactionVMFactory factory = new AddTransactionVMFactory(mDB, mTransactionId);

                    //todo, check error
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
//                    mIsNewTransaction = true;
                    /* add a new transaction*/
                    toolbar.setTitle(R.string.title_add_transaction);
                    setMoneyInActive();
                    setMoneyOutDeActive();
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


        mMoneyIn.setOnClickListener(moneyInBtnListener);

        mMoneyOut.setOnClickListener(moneyOutBtnListener);

        Log.d("flow_test4", "onActivityCreated:  new fragment");
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("test_flow4", "onDestroyView   add transaction fragment: ");
        mAdapterActionViewModel.setmIsInAddNEditFragment(false);
    }

    private void setMoneyInActive() {
        Log.d("test_flow7", " response money in is " + mMoneyIn.isChecked());

        mMoneyIn.setChecked(true);
        mTvMoneyIn.setTextColor(getResources().getColor(R.color.color_money_in));
        mCategoryAdapter.setData(FakeTestingData.getRevenueCategory());
        mCategoryAdapter.highlightExistingCategoryIfMatch(mCurrentTransactionCategory);
        mAdapterActionViewModel.setmSelectedCategory("");
    }

    private void setMoneyInDeActive() {
        Log.d("test_flow7", " response money in is " + mMoneyIn.isChecked());

        mMoneyIn.setChecked(false);
        mTvMoneyIn.setTextColor(getResources().getColor(R.color.color_deactive));

    }

    private void setMoneyOutActive() {
        Log.d("test_flow7", "response money out is " + mMoneyOut.isChecked());
        mCategoryAdapter.setData(FakeTestingData.getExpenseCategory());

        mMoneyOut.setChecked(true);
        mTvMoneyOut.setTextColor(getResources().getColor(R.color.color_money_out));
        mCategoryAdapter.highlightExistingCategoryIfMatch(mCurrentTransactionCategory);

        mAdapterActionViewModel.setmSelectedCategory("");
    }

    private void setMoneyOutDeActive() {
        Log.d("test_flow7", "response money out is " + mMoneyOut.isChecked());

        mMoneyOut.setChecked(false);
        mTvMoneyOut.setTextColor(getResources().getColor(R.color.color_deactive));
    }

    private Button.OnClickListener moneyInBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.d("test_flow7", "onclick, money in is " + mMoneyIn.isChecked());
            if (mMoneyIn.isChecked()) {
                setMoneyInActive();
                setMoneyOutDeActive();
            } else {
                setMoneyInDeActive();
                setMoneyOutActive();
            }
        }
    };

    private Button.OnClickListener moneyOutBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.d("test_flow7", "onclick, money out is " + mMoneyOut.isChecked());
            if (mMoneyOut.isChecked()) {
                setMoneyOutActive();
                setMoneyInDeActive();
            } else {
                setMoneyOutDeActive();
                setMoneyInActive();
            }
        }
    };

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
                dialog.show();

            } else {
                String tempDate = Integer.toString(mDateNum);
                year = Integer.parseInt(tempDate.substring(0, 2)) + 2000;
                month = Integer.parseInt(tempDate.substring(2, 4)) - 1;
                day = Integer.parseInt(tempDate.substring(4, 6));

                Log.d("test_flow12", "onClick: y+m+d" + year + " " + month + " " + day);
                DatePickerDialog dialog = new DatePickerDialog(
                        mAppCompatActivity,
                        AddNEditTransactionFragment.this,
                        year,
                        month,
                        day
                );
                dialog.show();
            }
        }
    };


    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

        mDateNum = UnitUtil.setDatePickerDateIntoAppIntDate(i, i1, i2);
        Log.d("test_flow12", "onDateSet: " + mDateNum);
        mTVDate.setText(UnitUtil.getTimeIntInMoreReadableFormat(mDateNum));

    }

    private FloatingActionButton.OnClickListener saveBtnOnlickListener
            = new FloatingActionButton.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (checkIfUserEnteredNecessaryDate()) {
                //String ledger, int time, Float amount, String category, String remark
                float tempAmount = Float.parseFloat(mEditTextAmount.getText().toString());
                if (mMoneyOut.isChecked()) {
                    tempAmount = 0 - tempAmount;
                }
                final TransactionEntry entry = new TransactionEntry(
                        mSpinner.getSelectedItem().toString(),
                        mDateNum,
                        tempAmount,
                        mCategoryAdapter.getClickedCategory(),
                        mEditTextRemark.getText().toString()

                );
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (mTransactionId == DEFAULT_TASK_ID) {

                            Log.d("test_flow13", "save new data ");
                            mDB.transactionDAO().insertTransaction(entry);
                        } else {
                            /*update existing records*/
                            Log.d("test_flow13", "update existing data ");
                            entry.setId(mTransactionId);
                            mDB.transactionDAO().updateTransaction(entry);

                        }

                        mAppCompatActivity.getSupportFragmentManager().popBackStack();
                    }
                });

            }

        }
    };


    private void populateUIWithExistingData(TransactionEntry transactionEntry) {
        if (transactionEntry == null) {
            Log.d("test_flow11", "populateUIWithExistingData: transactionEntry is null");
            return;
        }
        Log.d("test_flow8", "populateUIWithExistingData: ledger is " + transactionEntry.getLedger());
        if (transactionEntry.getAmount() >= 0) {
            //this transaction is revenue, need to active the money In btn
            setMoneyInActive();
            setMoneyOutDeActive();
        } else {
            //this transaction is revenue, need to active the money out btn
            setMoneyInDeActive();
            setMoneyOutActive();
        }

        mMoneyNum = transactionEntry.getAmount();

        //set amount
        mEditTextAmount.setText(UnitUtil.displayPositiveMoney(mMoneyNum));

        //set remark
        mEditTextRemark.setText(transactionEntry.getRemark());

        //set category
        if (mMoneyNum >= 0) {
        } else {
            mCategoryAdapter.setData(FakeTestingData.getExpenseCategory());
        }

        mCurrentTransactionCategory = transactionEntry.getCategory();
        mTVCategory.setText(mCurrentTransactionCategory);
        mCategoryAdapter.highlightExistingCategoryIfMatch(mCurrentTransactionCategory);

        //select spinner
        mPositionInSpinner = mSpinnerLedgerAdapter.getPosition(transactionEntry.getLedger());
        if (mPositionInSpinner != -1) {
            mSpinner.setSelection(mPositionInSpinner);
        }

        //IDK what is this now
//        //todo, change it to real category data source
//        FakeTestingData.getLedgers().indexOf(transactionEntry.getCategory());


        //set date
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

}
