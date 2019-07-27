package hanzhou.easyledger.ui;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import hanzhou.easyledger.R;
import hanzhou.easyledger.utility.Constant;
import hanzhou.easyledger.utility.UnitUtil;
import hanzhou.easyledger.viewmodel.AdapterNActionBarViewModel;
import hanzhou.easyledger.viewmodel.GeneralViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class QuestionFragment extends Fragment {

    private static final String TAG = QuestionFragment.class.getSimpleName();

    private Button btnFriend;
    private Button btnJoke;
    private Button btnLessFriendly;
    private Button btnGithub;
    private TextView explanationTxt;

    private GeneralViewModel mGeneralViewModel;
    private AdapterNActionBarViewModel mAdapterActionViewModel;
    private AppCompatActivity mAppCompatActivity;
    private Toolbar toolbar;

    public QuestionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        setHasOptionsMenu(true);
        mAppCompatActivity = (AppCompatActivity)context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =inflater.inflate(R.layout.fragment_question, container, false);

        String dateString = UnitUtil.getMonthDayToday();


        uiInitialization(rootView);

        //make a joke on April 1st
        if (dateString.equals("04-01")) {btnJoke.setVisibility(View.VISIBLE);}

        setOnClickListenerForAllButton();

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupViewModel();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        toolbar =  mAppCompatActivity.findViewById(R.id.toolbar_layout);
        toolbar.setTitle(getString(R.string.title_question_fragment));
        toolbar.getMenu().clear();
        inflater.inflate(R.menu.toolbar_empty, menu);
        mAppCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onResume() {
        super.onResume();
//        mAdapterActionViewModel.setmIsInBaseFragment(false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //todo, change viewmodel

//        mAdapterActionViewModel.setmIsInQuestionFragment(false);
    }



    private void uiInitialization(View rootView) {
        btnFriend = rootView.findViewById(R.id.button_question_friendly_answer);

        btnJoke = rootView.findViewById(R.id.button_question_bad_joke);

        btnLessFriendly = rootView.findViewById(R.id.button_question_less_friendly_answer);

        explanationTxt = rootView.findViewById(R.id.textview_question_explanation);

        btnGithub = rootView.findViewById(R.id.button_github);
    }


    private void setOnClickListenerForAllButton() {
        btnFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                explanationTxt.setVisibility(View.VISIBLE);
                explanationTxt.setText(R.string.explanation_friendly_why_this_app_is_safe);
                btnGithub.setVisibility(View.VISIBLE);
            }
        });

        btnLessFriendly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                explanationTxt.setVisibility(View.VISIBLE);
                explanationTxt.setText(R.string.explanation_less_friendly_why_this_app_is_safe);
                btnGithub.setVisibility(View.VISIBLE);
            }
        });

        btnJoke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                explanationTxt.setVisibility(View.VISIBLE);
                explanationTxt.setText(R.string.explanation_bad_joke);
            }
        });

        btnGithub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent visitGithub = new Intent(Intent.ACTION_VIEW);
                visitGithub.setData(Uri.parse(getResources().getString(R.string.github_link)));
                startActivity(visitGithub);
            }
        });
    }


    private void setupViewModel() {
        mGeneralViewModel = ViewModelProviders.of(mAppCompatActivity).get(GeneralViewModel.class);
        mGeneralViewModel.setmCurrentScreen(Constant.FRAG_NAME_QUESTION);


    }
}
