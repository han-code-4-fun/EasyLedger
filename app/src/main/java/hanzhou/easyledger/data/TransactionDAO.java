package hanzhou.easyledger.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TransactionDAO {
    @Query("SELECT * FROM transactions ORDER BY time DESC, id DESC")
    LiveData<List<TransactionEntry>> loadAllTransactions();

    @Query("SELECT * FROM transactions WHERE id = :id")
    LiveData<TransactionEntry> loadTransactionByID(int id);

    @Query("SELECT * FROM transactions WHERE ledger = :leger ORDER BY time DESC, id DESC")
    LiveData<List<TransactionEntry>> loadTransactionByLedger(String leger);

    @Query("SELECT * FROM transactions WHERE category = :untag ORDER BY time DESC, id DESC")
    LiveData<List<TransactionEntry>> loadUntaggedTransactions(String untag);

    @Query("SELECT * FROM transactions WHERE time >= :time7DaysBackwards ORDER BY time DESC, id DESC")
    LiveData<List<TransactionEntry>> loadTransactionByTime7days(int time7DaysBackwards);


    @Query("SELECT * FROM transactions WHERE time >= :startingDate and amount>=0 ORDER BY time DESC, id DESC")
    LiveData<List<TransactionEntry>> loadTransactionRevenuePeriod(int startingDate);
    @Query("SELECT * FROM transactions WHERE time >= :startingDate and amount<0 ORDER BY time DESC, id DESC")
    LiveData<List<TransactionEntry>> loadTransactionExpensePeriod(int startingDate);

    @Query("SELECT * FROM transactions WHERE time >= :timeDaysBackwards ORDER BY time DESC, id DESC")
    LiveData<List<TransactionEntry>> loadTransactionByTimeUserDefined(int timeDaysBackwards);

    @Query("SELECT * FROM transactions WHERE time >= :startDate and time<= :endDate ORDER BY time ASC, id ASC")
    LiveData<List<TransactionEntry>> loadTransactionInPeriodForChart(int startDate, int endDate);


    @Insert
    void insertTransaction(TransactionEntry transactionEntry);

    @Insert
    void insertListOfTransactions(List<TransactionEntry> lisOfTransactions);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateTransaction(TransactionEntry transactionEntry);

    @Delete
    void deleteTransaction(TransactionEntry transactionEntry);

    @Delete
    void deleteListOfTransactions(List<TransactionEntry> lisOfTransactions);

    @Query("DELETE FROM transactions")
    void deleteAll();


    /*
        select account_id, total,  standard_qty from orders

        order by account_id desc, total desc limit 100

     */
}
