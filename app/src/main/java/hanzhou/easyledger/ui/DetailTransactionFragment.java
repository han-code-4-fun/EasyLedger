package hanzhou.easyledger.ui;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import hanzhou.easyledger.R;
import hanzhou.easyledger.viewmodel.DetailTransactionViewModel;

public class DetailTransactionFragment extends Fragment {

    private DetailTransactionViewModel mViewModel;

    public static DetailTransactionFragment newInstance() {
        return new DetailTransactionFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.detail_transaction_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(DetailTransactionViewModel.class);
        // TODO: Use the ViewModel
    }

}
