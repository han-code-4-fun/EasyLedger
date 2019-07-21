package hanzhou.easyledger.ui.settings;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import hanzhou.easyledger.viewmodel.sharedpreference_viewmodel.SettingsViewModel;

public class SettingAddNEditFragment extends Fragment {

    private AdapterNActionBarViewModel adapterNActionBarViewModel;
    private SettingsViewModel mSettingsViewModel;
    private AppCompatActivity mAppCompatActivity;

    private Toolbar toolbar;
    private FloatingActionButton mFloatingActionButton;

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private SettingAdapter mSettingAdapter;

    private ArrayList<String> mCategoryList;

    private AlertDialog mAlertDialog;
    private  AlertDialog.Builder mDialogBuilder;

    private GsonHelper mGsonHelper;

    private String mSettingType;

    public static final int REQUEST_DIALOG_CODE = 12345;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mAppCompatActivity = (AppCompatActivity) context;
        setHasOptionsMenu(true);

        mGsonHelper = GsonHelper.getInstance(mAppCompatActivity);


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_setting_add_n_edit, container, false);
        mRecyclerView = root.findViewById(R.id.setting_edit_recyclerview);

        mFloatingActionButton = root.findViewById(R.id.btn_setting_add_entry_btn);
        mFloatingActionButton.setOnClickListener(onClickFAB);

//        mCategoryList = FakeTestingData.testgetString();
//        Log.d("test_setting", "onCreateView: mCategoryList  get 0 -> " + mCategoryList.get(0));
//        Log.d("test_setting", "onCreateView: mCategoryList size -> " + mCategoryList.size());

        mLinearLayoutManager = new LinearLayoutManager(mAppCompatActivity);

        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        mSettingAdapter = new SettingAdapter(mCategoryList, mAppCompatActivity);

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

                if(mSettingAdapter.isCurrentLedgerOVERALL(positionFrom) ||
                        mSettingAdapter.isCurrentLedgerOVERALL(positionTo)){

                    launchWarningMSGNoSWAP(positionFrom,positionTo);

                }else{

                    mSettingAdapter.swapPosition(positionFrom, positionTo);
                }



                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();

                if (mSettingAdapter.isCurrentCategoryOthers(position)
                        || mSettingAdapter.isCurrentLedgerOVERALL(position)) {
                    /*user should not remove "Others" in category list or "OVERALL" in ledger list*/
                    launchWarningMSGNoDelete(position);
                } else {

                    mSettingAdapter.remove(position);
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
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.d("test_setting", "getting reponse back");
        if (requestCode == REQUEST_DIALOG_CODE) {
            if (resultCode == Activity.RESULT_OK) {

                String newCategory = data.getStringExtra(Constant.CATEGORY_ADD);
                mCategoryList.add(newCategory);
                mSettingAdapter.setData(mCategoryList);

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
        adapterNActionBarViewModel.setmIsInEditLedgerFragment(false);
    }

    private void setupViewModel() {
        adapterNActionBarViewModel = ViewModelProviders.of(mAppCompatActivity).get(AdapterNActionBarViewModel.class);
        adapterNActionBarViewModel.setmIsInEditLedgerFragment(true);

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
        mCategoryList = mGsonHelper.getDataFromSharedPreference(s);

        mSettingAdapter.setData(mCategoryList);
    }

    private void saveNewCategory() {
        mGsonHelper.saveDataToSharedPreference(mSettingAdapter.getData(), mSettingType);
    }

    private void launchWarningMSGNoDelete(final int position) {

        new AlertDialog.Builder(mAppCompatActivity)
                .setTitle(getString(R.string.setting_warning_cannot_delete_others_in_category_title))
                .setMessage(getString(R.string.setting_warning_cannot_delete_others_in_category_msg_body))
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
        if(mAlertDialog!=null && mAlertDialog.isShowing()){
        /*do nothing since it's already on screen*/
        }else{
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
            dialog.setTargetFragment(SettingAddNEditFragment.this, REQUEST_DIALOG_CODE);
            dialog.show(mAppCompatActivity.getSupportFragmentManager(), null);
        }
    };


}
