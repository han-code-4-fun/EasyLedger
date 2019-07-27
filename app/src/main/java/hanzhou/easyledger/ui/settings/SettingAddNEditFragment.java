package hanzhou.easyledger.ui.settings;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import hanzhou.easyledger.R;
import hanzhou.easyledger.utility.Constant;
import hanzhou.easyledger.utility.GsonHelper;
import hanzhou.easyledger.viewadapter.SettingAdapter;
import hanzhou.easyledger.viewmodel.AdapterNActionBarViewModel;
import hanzhou.easyledger.viewmodel.GeneralViewModel;
import hanzhou.easyledger.viewmodel.sharedpreference_viewmodel.SettingsViewModel;

public class SettingAddNEditFragment extends Fragment {


    private GeneralViewModel mGeneralViewModel;
    private AdapterNActionBarViewModel adapterNActionBarViewModel;
    private SettingsViewModel mSettingsViewModel;
    private AppCompatActivity mAppCompatActivity;

    private Toolbar toolbar;
    private FloatingActionButton mFloatingActionButton;

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private SettingAdapter mSettingAdapter;

    private ArrayList<String> mListData;

    private AlertDialog mAlertDialog;
    private AlertDialog.Builder mDialogBuilder;

    private GsonHelper mGsonHelper;

    private String mSettingType;

    private ArrayList<String> mListDeletedData;

    private SharedPreferences mSharedPreference;

    private boolean isRBCMsgExtractionOn;

    public static final int REQUEST_DIALOG_CODE = Constant.REQUEST_DIALOG_CODE_SETTING_ADD_EDIT_FRAGMENT;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mAppCompatActivity = (AppCompatActivity) context;
        setHasOptionsMenu(true);
        mSharedPreference = PreferenceManager.getDefaultSharedPreferences(mAppCompatActivity);
        isRBCMsgExtractionOn = mSharedPreference.getBoolean(getString(R.string.setting_others_msg_tracker_rbc_default_key),true);
        mGsonHelper = GsonHelper.getInstance();
        mGsonHelper.setmSharedPreferences(mSharedPreference);


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_setting_add_n_edit, container, false);
        mRecyclerView = root.findViewById(R.id.setting_edit_recyclerview);

        mFloatingActionButton = root.findViewById(R.id.btn_setting_add_entry_btn);
        mFloatingActionButton.setOnClickListener(onClickFAB);


        mLinearLayoutManager = new LinearLayoutManager(mAppCompatActivity);

        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        mSettingAdapter = new SettingAdapter(mListData, mAppCompatActivity);

        mRecyclerView.setAdapter(mSettingAdapter);

        RecyclerView.ItemDecoration divider =
                new DividerItemDecoration(mAppCompatActivity, DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(divider);


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP | ItemTouchHelper.DOWN,
                ItemTouchHelper.START | ItemTouchHelper.END) {


            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                int positionFrom = viewHolder.getAdapterPosition();
                int positionTo = target.getAdapterPosition();

                if (mSettingAdapter.isCurrentLedgerOVERALL(positionFrom) ||
                        mSettingAdapter.isCurrentLedgerOVERALL(positionTo)) {

                    launchWarningMSGNoSWAP(positionFrom, positionTo);

                } else {

                    mSettingAdapter.swapPosition(positionFrom, positionTo);
                }


                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                boolean isValidAction = true;
                int position = viewHolder.getAdapterPosition();

                if (mSettingAdapter.isCurrentCategoryOthers(position)) {
                    /*user should not remove "Others" in category list or "OVERALL" in ledger list*/
                    isValidAction=false;
                    launchWarningMSGNoDelete(position, getString(R.string.setting_warning_cannot_delete_others_in_category_msg_body));
                }

                if (mSettingAdapter.isCurrentLedgerOVERALL(position)) {
                    isValidAction=false;
                    launchWarningMSGNoDelete(position, getString(R.string.setting_warning_cannot_delete_overall_in_ledger_msg_body));

                }

                if (isRBCMsgExtractionOn) {
                    if (mSettingAdapter.isCurrentLedgerOnSMSExtraction(position, Constant.RBC_LEDGER_NAME)){
                        isValidAction=false;
                        launchWarningMSGNoDelete(position, getString(R.string.setting_warning_cannot_delete_sms_extraction_ledger_msg_body));
                    }
                }

                if(isValidAction){
                    String removedItem = mSettingAdapter.remove(position);
                    mListDeletedData.add(removedItem);
                }


            }
        }).attachToRecyclerView(mRecyclerView);

        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        toolbar = mAppCompatActivity.findViewById(R.id.toolbar_layout);
        toolbar.setTitle(getString(R.string.title_settings_fragment));
        toolbar.getMenu().clear();
        inflater.inflate(R.menu.toolbar_setting_edit, menu);
        mAppCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupViewModel();
        mListDeletedData = new ArrayList<>();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_DIALOG_CODE) {
            if (resultCode == Activity.RESULT_OK) {

                String newCategory = data.getStringExtra(Constant.CATEGORY_ADD);
                mListData.add(newCategory);
                mSettingAdapter.setData(mListData);

            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                mAppCompatActivity.getSupportFragmentManager().popBackStack();
                break;
            case R.id.toolbar_edit_ledger_save:
                saveNewCategory();
                markDeletedDataIntoTransactionEntryRemark();
                mAppCompatActivity.getSupportFragmentManager().popBackStack();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void setupViewModel() {

        mGeneralViewModel = ViewModelProviders.of(mAppCompatActivity).get(GeneralViewModel.class);
        mGeneralViewModel.setmCurrentScreen(Constant.FRAG_NAME_SETTING_ADD_EDIT);

        adapterNActionBarViewModel = ViewModelProviders.of(mAppCompatActivity).get(AdapterNActionBarViewModel.class);

        mSettingsViewModel = ViewModelProviders.of(mAppCompatActivity).get(SettingsViewModel.class);

        mSettingsViewModel.getmCategoryType().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                mSettingType = s;
                updateAdapterData(s);

            }
        });

    }

    private void updateAdapterData(String s) {
        mListData = mGsonHelper.getDataFromSharedPreference(s);

        mSettingAdapter.setData(mListData);
    }

    private void saveNewCategory() {
        mGsonHelper.saveDataToSharedPreference(mSettingAdapter.getData(), mSettingType);
    }

    /*if user delete any category/ledger, add a remark to these entry in DB*/
    private void markDeletedDataIntoTransactionEntryRemark() {

        if (mListDeletedData.size() >= 1) {
            if (mSettingType.equals(Constant.LEDGERS)) {
                /*add remark, (history Ledger -> $LedgerName)*/
                for (int i = 0; i < mListDeletedData.size(); i++) {
                    String ledgerName = mListDeletedData.get(i);
                    String addedRemark = "(history ledger -> " + ledgerName + ") ";
                    mSettingsViewModel.renameHistoryLedger(addedRemark, ledgerName);

                }
            } else {
                /*add remark, (hisotory category -> $categoryName)*/
                for (int i = 0; i < mListDeletedData.size(); i++) {
                    String categoryName = mListDeletedData.get(i);
                    String addedRemark = "(history category -> " + categoryName + ") ";
                    mSettingsViewModel.renameHistoryCategory(addedRemark, categoryName);

                }
            }
        }
    }

    private void launchWarningMSGNoDelete(final int position, String warningString) {

        new AlertDialog.Builder(mAppCompatActivity)
                .setTitle(getString(R.string.setting_warning_cannot_delete_others_in_category_title))
                .setMessage(warningString)
                .setNeutralButton(getString(R.string.setting_warning_neutral_btn_title), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mSettingAdapter.notifyItemChanged(position);
                    }
                })

                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void launchWarningMSGNoSWAP(final int position1, final int position2) {
        if (mAlertDialog != null && mAlertDialog.isShowing()) {
            /*do nothing since it's already on screen*/
        } else {
            mDialogBuilder = new AlertDialog.Builder(mAppCompatActivity)
                    .setTitle(getString(R.string.setting_warning_cannot_swap_overall_in_ledger_title))
                    .setMessage(getString(R.string.setting_warning_cannot_swap_overall_in_ledger_msg_body))
                    .setNeutralButton(getString(R.string.setting_warning_neutral_btn_title), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            mSettingAdapter.notifyItemChanged(position1);
                            mSettingAdapter.notifyItemChanged(position2);
                        }
                    })

                    .setIcon(android.R.drawable.ic_dialog_alert);

            mAlertDialog = mDialogBuilder.create();
            mAlertDialog.show();
        }


    }

    private FloatingActionButton.OnClickListener onClickFAB = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            AddEntryDialog dialog = new AddEntryDialog();
            Bundle args = new Bundle();
            args.putStringArrayList(Constant.SETTING_BUNDLE_LIST_OF_NAMES, mListData);
            dialog.setArguments(args);
            dialog.setTargetFragment(SettingAddNEditFragment.this, REQUEST_DIALOG_CODE);




            dialog.show(mAppCompatActivity.getSupportFragmentManager(), null);
        }
    };


}
