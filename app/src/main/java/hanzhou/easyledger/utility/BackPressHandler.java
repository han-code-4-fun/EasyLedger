package hanzhou.easyledger.utility;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

public class BackPressHandler {
    /*
     *   When in the main activity,
     *   If user clicks back for the first time,
     *   there will be a (Toast) msg warning,
     *   if user clicks the 'back' 2nd time within
     *   2 seconds, app will exit
     *
     * */

    private static int numOfTimesBackPressed =0;

    private static  Handler handlerBackPress;
    private static Runnable minusOneNumBackPressed;

    public static boolean isUserPressedTwice(Context context){

        numOfTimesBackPressed += 1;


        if(numOfTimesBackPressed <= 1)
        {
            Toast.makeText(
                    context,
                    "Tap one more time to exit app.",
                    Toast.LENGTH_SHORT).show();

            handlerBackPress = new Handler();
            minusOneNumBackPressed = new Runnable() {
                @Override
                public void run() {
                    numOfTimesBackPressed -= 1;
                }

            };
            //int numOfTimesBackPressed-- for every 2seconds
            handlerBackPress.postDelayed(minusOneNumBackPressed, 2000);
            return false;
        }else{
            handlerBackPress.removeCallbacks(minusOneNumBackPressed);
            numOfTimesBackPressed = 0;
            return true;
        }

    }
}
