package hanzhou.easyledger.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import hanzhou.easyledger.R;
import hanzhou.easyledger.ui.MainActivity;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    private static final String NOTIFICATION_MSG_FROM_FIREBASE="message_from_firebase";

    private static final int NOTIFICATION_ID_REMINDER_UPDATE_APP  = 1001;


    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        /*leave it here for further developement*/
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
//        super.onMessageReceived(remoteMessage);


        sendUpdateNotification(remoteMessage.getNotification().getBody());
    }

    private void sendUpdateNotification(String body) {

        Context context = MyFirebaseMessagingService.this;
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent  = PendingIntent.getActivity(this, 0, intent,PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSourdUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    NOTIFICATION_MSG_FROM_FIREBASE,
                    context.getString(R.string.main_notification_channel_name),
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIFICATION_MSG_FROM_FIREBASE);
        builder.setSmallIcon(R.drawable.ic_nofitication);
        builder.setContentTitle(getString(R.string.notification_title_msg_from_firebase));
        builder.setContentText(body);
        builder.setAutoCancel(true);
        builder.setSound(defaultSourdUri);
        builder.setContentIntent(pendingIntent);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            builder.setPriority(NotificationCompat.PRIORITY_HIGH);
        }
        notificationManager.notify(NOTIFICATION_ID_REMINDER_UPDATE_APP, builder.build());

    }
}
