package hanzhou.easyledger_demo.smsprocessor;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;

import hanzhou.easyledger_demo.R;

public class HistorySMSReader {

    public static void readHistorySMS(Context context, String[] projection, String selection, String[] selectionArg, String order) {
        try {

            Cursor cursor = context.getContentResolver().query(
                    Uri.parse(context.getString(R.string.sms_base_uri)),
                    projection,
                    selection,
                    selectionArg,
                    order);

            ArrayList<ArrayList<String>> results = new ArrayList<>();

            if (cursor != null) {

                if (cursor.moveToFirst()) {
                    /*must check the result to prevent exception*/
                    do {
                        ArrayList<String> currentMSGData = new ArrayList<>();
                        for (int idx = 0; idx < cursor.getColumnCount(); idx++) {
                            /*add address, date, and body info of a MSG*/
                            currentMSGData.add(cursor.getString(idx));
                        }

                        results.add(currentMSGData);
                    } while (cursor.moveToNext());

                } else {
                    /*empty box, no SMS*/
                }

                /*immediately close cursor if not in use*/
                cursor.close();
                String address = "";
                String msgBody = "";

                for (int i = 0; i < results.size(); i++) {

                    address = results.get(i).get(0);
                    msgBody = results.get(i).get(1);
                    SMSProcessor.processExtraction(context, address, msgBody);

                }

            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }


    }
}
