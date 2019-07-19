package hanzhou.easyledger.ui.settings;

import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.preference.PreferenceFragmentCompat;

import hanzhou.easyledger.R;
import hanzhou.easyledger.viewmodel.AdapterNActionBarViewModel;

public class SettingEditLedger extends Fragment {
    private AdapterNActionBarViewModel adapterNActionBarViewModel;

    private AppCompatActivity appCompatActivity;
    private Toolbar toolbar;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        appCompatActivity = (AppCompatActivity) context;
        setHasOptionsMenu(true);
    }
//
//    @Override
//    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
//        addPreferencesFromResource(R.xml.setting_edit_ledger);
//
//    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        toolbar = appCompatActivity.findViewById(R.id.toolbar_layout);
        toolbar.setTitle(getString(R.string.title_settings_fragment));
        toolbar.getMenu().clear();
        inflater.inflate(R.menu.toolbar_edit_ledger, menu);
        appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
                appCompatActivity.getSupportFragmentManager().popBackStack();
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
        adapterNActionBarViewModel = ViewModelProviders.of(appCompatActivity).get(AdapterNActionBarViewModel.class);
        adapterNActionBarViewModel.setmIsInEditLedgerFragment(true);
    }
}
