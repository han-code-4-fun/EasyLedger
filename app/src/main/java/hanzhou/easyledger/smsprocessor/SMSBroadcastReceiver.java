package hanzhou.easyledger.smsprocessor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import hanzhou.easyledger.R;

/*
 *
 * Due to battery efficiency concern
 * and the amount of processing resources need for this app (a little bit),
 *
 *
 * BroadcastReceiver will run only when app is not killed by OS
 *
 * All the sms received before app launching will be processed when app launching
 *
 */

public class SMSBroadcastReceiver extends BroadcastReceiver {


    private static final String TAG = SMSBroadcastReceiver.class.getSimpleName();

    private static SharedPreferences mSharedPreferences;

    @Override
    public void onReceive(Context context, Intent intent) {

        if(isAtLeastOneSmsExtractorFromSettingIsOn(context)){

            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Object[] pdusObj = (Object[]) bundle.get("pdus");
                if (pdusObj != null) {

                    String[] msgContent = getMSGContent(pdusObj, intent);

                    SMSBankSelector.smsFromWhichBank(context, msgContent[0],msgContent[1]);

                } else {
                    Log.e(TAG, "onReceive: error getting 'pdus'");
                }
            }
        }
    }

    private String[] getMSGContent(Object[] pdusObj, Intent intent) {

        String[] output = new String[2];

        SmsMessage currentMessage;
        for (int i = 0; i < pdusObj.length; i++) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                String formatFromPDU = intent.getStringExtra("format");
                currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i], formatFromPDU);
            } else {
                currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
            }

            output[0] = currentMessage.getOriginatingAddress();

            output[1] = currentMessage.getDisplayMessageBody();

        }


        return output;
    }

    private boolean isAtLeastOneSmsExtractorFromSettingIsOn(Context context){



        boolean isRBCOn = mSharedPreferences.getBoolean(
                context.getResources().getString(R.string.setting_others_msg_tracker_rbc_default_key),true);

        if(isRBCOn){
            return true;
        }

        return false;
    }

    public void setmSharedPreferences(SharedPreferences input){
        mSharedPreferences = input;
    }

}
