package hanzhou.easyledger.ui;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import hanzhou.easyledger.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class LauncherFragment extends Fragment {


    public LauncherFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_launcher, container, false);
    }

}
