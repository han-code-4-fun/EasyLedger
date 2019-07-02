package hanzhou.easyledger.utility;

import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

import hanzhou.easyledger.R;


public class LedgerNameManager extends AppCompatActivity {
    private static int numberOfLedger =0;
    private static SharedPreferences sp;

    public LedgerNameManager(){

        sp= getSharedPreferences(Constant.prefName, MODE_PRIVATE);

    }

    public static void getLedgerNumber(){
        sp.getInt(Constant.ledgerNumber, 0);
    }

    public static void addToLedgers(String ledgerame){
        SharedPreferences.Editor editor = sp.edit();
//        editor.putString()
    }



}
