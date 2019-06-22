package hanzhou.easyledger.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

import hanzhou.easyledger.R;
import hanzhou.easyledger.SmsBroadcastReceiver;

import static android.provider.Telephony.Sms.Intents.SMS_RECEIVED_ACTION;

public class OverviewActivity extends AppCompatActivity {

    private static final String TAG = OverviewActivity.class.getSimpleName();

    IntentFilter smsIntentFilter;
    SmsBroadcastReceiver smsReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        smsIntentFilter = new IntentFilter();
        smsIntentFilter.addAction(SMS_RECEIVED_ACTION);

        smsReceiver = new SmsBroadcastReceiver();

    }
    @Override
    protected void onResume() {
        super.onResume();
        this.registerReceiver(smsReceiver, smsIntentFilter);
        Log.d(TAG, "onResume: smsReceiver registered");
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(smsReceiver);
        Log.d(TAG, "onPause: smsReceiver unregistered");
    }

}
