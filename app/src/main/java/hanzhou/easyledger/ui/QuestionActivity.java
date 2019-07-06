package hanzhou.easyledger.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import hanzhou.easyledger.R;
import hanzhou.easyledger.utility.UnitUtil;


//activity that explain why the app is safe
public class QuestionActivity extends AppCompatActivity {

    private static final String TAG = QuestionActivity.class.getSimpleName();

    private Button btnFriend;
    private Button btnJoke;
    private Button btnLessFriendly;
    private Button btnGithub;
    private Toolbar toolBar;
    private TextView explanationTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);



        String dateString = UnitUtil.getMonthDayToday();

        uiInitialization();

        //make a joke on April 1st
        if (dateString.equals("04-01")) {btnJoke.setVisibility(View.VISIBLE);}

        setOnClickListenerForAllButton();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        int id = item.getItemId();
        if (id == android.R.id.home) {
            //navigate back to previous activity
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void uiInitialization() {
        toolBar = findViewById(R.id.toolbar_layout);
        setSupportActionBar(toolBar);
        getSupportActionBar().setTitle(getResources().getString(R.string.title_question_activity));
        toolBar.setTitle(getResources().getString(R.string.title_question_activity));
        toolBar.inflateMenu(R.menu.question_activity_toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnFriend = findViewById(R.id.button_question_friendly_answer);

        btnJoke = findViewById(R.id.button_question_bad_joke);

        btnLessFriendly = findViewById(R.id.button_question_less_friendly_answer);

        explanationTxt = findViewById(R.id.textview_question_explanation);

        btnGithub = findViewById(R.id.button_github);
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
}
