package hanzhou.easyledger.data;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {TransactionEntry.class}, version = 1, exportSchema = false)
public abstract class TransactionDB extends RoomDatabase {

    private static final String LOG_TAG = TransactionDB.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "ledger_db";
    private static TransactionDB sInstance;

    public static TransactionDB getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                Log.d(LOG_TAG, "Creating new database instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        TransactionDB.class, TransactionDB.DATABASE_NAME)
                        .fallbackToDestructiveMigration()//this means if we change DB schema, ROOM will delete original and make new
                        .build();
            }
        }
        Log.d(LOG_TAG, "Getting the database instance");
        return sInstance;
    }

    public abstract TransactionDAO transactionDAO();
}
