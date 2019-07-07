package hanzhou.easyledger.data;


import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/* A repository class that
*   1. can be further developer for taking network data
*   2. add an abstraction layout
* */

public class AppExecutors {

    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static AppExecutors sInstance;
    private final Executor diskIO;


    private AppExecutors(Executor diskIO) {
        this.diskIO = diskIO;
    }

    public static AppExecutors getInstance() {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new AppExecutors(
                        Executors.newSingleThreadExecutor()
                        );
            }
        }
        return sInstance;
    }

    public Executor diskIO() {
        return diskIO;
    }



}
