package hanzhou.easyledger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

public class SmsBroadcastReceiver extends BroadcastReceiver {


    private static final String TAG = SmsBroadcastReceiver.class.getSimpleName();

    private static final String RECEIVED_MSG = "android.provider.Telephony.SMS_RECEIVED";


    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent.getAction().equals(RECEIVED_MSG)){

            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Object[] pdusObj = (Object[]) bundle.get("pdus");
                Log.d(TAG, "onReceive: inside intent.getAction() is: "+ intent.getAction());
                Log.d(TAG, "onReceive: 1111111111111111111");
                for (int i = 0; i < pdusObj.length; i++) {
                    Log.d(TAG, "onReceive: 222222222222222222");
                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String message = currentMessage.getDisplayMessageBody();
                    StringBuilder sb = new StringBuilder();
                    sb.append("Sender: ");
                    sb.append(currentMessage.getOriginatingAddress());
                    sb.append(" MSG body: ");
                    sb.append(message);
                    Log.d(TAG, "onReceive: 3333333333333");
                    Log.d(TAG, sb.toString());

                }
            }
        }


    }

}
