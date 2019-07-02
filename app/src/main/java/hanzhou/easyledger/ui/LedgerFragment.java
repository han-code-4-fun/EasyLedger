package hanzhou.easyledger.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import hanzhou.easyledger.R;
import hanzhou.easyledger.LedgersAdapter;
import hanzhou.easyledger.viewmodel.CrossFragmentCommunicationViewModel;

/*
*   Fragment that shows detailed transactions based on number of 'ledger'
*   that user created, by default there is one ledger
*
* */
public class LedgerFragment extends Fragment
       {

    //todo
    private static int NUMBER_OF_LEDGERS;
    private CrossFragmentCommunicationViewModel crossVM;
    private AppCompatActivity appCompatActivity;
    private LedgersAdapter ledgersAdapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        appCompatActivity = (AppCompatActivity) getActivity();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {




//        NUMBER_OF_LEDGERS = HelperUtilMoveToSharedPreferenceLater.getNumberOfTransactionTables();

        final View rootView =inflater.inflate(R.layout.fragment_ledger, container,false);

        final TabLayout tabLayout = rootView.findViewById(R.id.transaction_tablayout);

        ViewPager viewPager = rootView.findViewById(R.id.transaction_viewpager);

        crossVM = ViewModelProviders.of(appCompatActivity).get(CrossFragmentCommunicationViewModel.class);


        //todo, watch out, if add to back stack, will not populate this if
        ledgersAdapter = new LedgersAdapter(getChildFragmentManager(), /*NUMBER_OF_LEDGERS,*/ this);
        //todo,  track if there is change while user fragment is not in forground and user add/remove ledgers

        viewPager.setAdapter(ledgersAdapter);

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setMinimumWidth(250);

        return rootView;
    }





}
