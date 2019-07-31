package hanzhou.easyledger.smsprocessor;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import java.util.ArrayList;

public class HistorySMSReader {

    public static void readHistorySMS(Context context, String[] projection, String selection, String[] selectionArg,String order) {
        try{

            Cursor cursor = context.getContentResolver().query(
                    Uri.parse("content://sms/inbox"),
                    projection,
                    selection,
                    selectionArg,
                    order);

            ArrayList<ArrayList<String>> results= new ArrayList<>();

            if (cursor != null) {

                if (cursor.moveToFirst()) { // must check the result to prevent exception
                    do {
                        ArrayList<String> currentMSGData = new ArrayList<>();
                        for (int idx = 0; idx < cursor.getColumnCount(); idx++) {
                            /*add address, date, and body info of a MSG*/
                            currentMSGData.add(cursor.getString(idx));
                        }

                        results.add(currentMSGData);
                    } while (cursor.moveToNext());

                } else {
                    // empty box, no SMS
                }

                /*immediately close cursor if not in use*/
                cursor.close();
                String address="";
                String msgBody = "";

                for (int i = 0; i < results.size(); i++) {
                    //todo, extract each sms in background
                    address = results.get(i).get(0);
                    msgBody = results.get(i).get(1);
                    SMSProcessor.processExtraction(context, address,msgBody);

                }

            }
        }catch (SecurityException e){
            e.printStackTrace();
        }


    }
}
