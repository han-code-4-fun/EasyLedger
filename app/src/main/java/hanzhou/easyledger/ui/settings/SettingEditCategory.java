package hanzhou.easyledger.ui.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;

import hanzhou.easyledger.R;
import hanzhou.easyledger.utility.FakeTestingData;
import hanzhou.easyledger.utility.UnitUtil;
import hanzhou.easyledger.viewadapter.SettingAdapter;
import hanzhou.easyledger.viewmodel.AdapterNActionBarViewModel;

public class SettingEditCategory extends Fragment {

    private AdapterNActionBarViewModel adapterNActionBarViewModel;

    private AppCompatActivity mAppCompatActivity;
    private Toolbar toolbar;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private SettingAdapter mSettingAdapter;

    private ArrayList<String> mTestList;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mAppCompatActivity = (AppCompatActivity) context;
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_setting_add_n_edit, container, false);
        mRecyclerView = root.findViewById(R.id.setting_edit_recyclerview);


        mTestList = FakeTestingData.testgetString();
        Log.d("test_setting", "onCreateView: mTestList  get 0 -> "+mTestList.get(0));
        Log.d("test_setting", "onCreateView: mTestList size -> "+mTestList.size());

        mLinearLayoutManager = new LinearLayoutManager(mAppCompatActivity);

        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        mSettingAdapter = new SettingAdapter(mTestList, mAppCompatActivity);

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

//                Collections.swap(mTestList, positionFrom, positionTo);

                mSettingAdapter.swapPosition(positionFrom, positionTo);

                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                mSettingAdapter.remove(position);
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
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                mAppCompatActivity.getSupportFragmentManager().popBackStack();
                break;
            case R.id.toolbar_edit_ledger_save:
                //perform save action
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
    }

    private void loadListToSystem(){
        SharedPreferences sharedPreferences = mAppCompatActivity.getSharedPreferences();

        Type type = new TypeToken<ArrayList<String>>(){}.getType();

    }

    private void saveListToSystem(){

    }
}
