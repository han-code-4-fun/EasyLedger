package hanzhou.easyledger.ui;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import hanzhou.easyledger.R;
import hanzhou.easyledger.viewmodel.AdapterNActionBarViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddNEditTransactionFragment extends Fragment {

    private AdapterNActionBarViewModel mAdapterActionViewModel;
    private AppCompatActivity appCompatActivity;
    private Toolbar toolBar;

    public AddNEditTransactionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        appCompatActivity = (AppCompatActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =inflater.inflate(R.layout.fragment_add_n_edit_transaction, container, false);
        toolBar = appCompatActivity.findViewById(R.id.toolbar_layout);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAdapterActionViewModel = ViewModelProviders.of(appCompatActivity).get(AdapterNActionBarViewModel.class);

        mAdapterActionViewModel.getmClickedEntryID().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if(integer != null){
                    /*  edit an existing transaction*/
                    toolBar.setTitle(R.string.title_edit_transaction);
                }else{
                    /* add a new transaction*/
                    toolBar.setTitle(R.string.title_add_transaction);

                }

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mAdapterActionViewModel.setmIsInAddNEditFragment(false);
    }
}
