package hanzhou.easyledger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;


public class SmsBroadcastReceiver extends BroadcastReceiver {


    private static final String TAG = SmsBroadcastReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {

//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
//        if(intent.getAction().equals(RECEIVED_MSG)){
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            Object[] pdusObj = (Object[]) bundle.get("pdus");
            if(pdusObj != null){
                logMSG(pdusObj,intent);
            }else{
                Log.e(TAG, "onReceive: error getting 'pdus'" );
            }

        }

    }

    private void logMSG(Object[] pdusObj,Intent intent){
        Log.d(TAG, "onReceive: 1111111111111111111");
        StringBuilder outputFormat = new StringBuilder();

        StringBuilder messageSender = new StringBuilder();
        StringBuilder messageBody= new StringBuilder();

        SmsMessage currentMessage;
        for (int i = 0; i < pdusObj.length; i++) {

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                String formatFromPDU = intent.getStringExtra("format");
                currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i], formatFromPDU);
            }else{
                currentMessage= SmsMessage.createFromPdu((byte[]) pdusObj[i]);
            }

            if(!messageSender.toString().equals(currentMessage.getOriginatingAddress())){
                messageSender.append(currentMessage.getOriginatingAddress());
            }
            messageBody.append(currentMessage.getDisplayMessageBody());
        }
        //todo handle international number e.g. +1
        outputFormat.append("Sender: ");
        outputFormat.append(messageSender) ;
        outputFormat.append(" MSG body: ") ;
        outputFormat.append(messageBody);
        Log.d(TAG, "onReceive: 22222222222222222222");
        Log.d(TAG, outputFormat.toString());
    }

}
