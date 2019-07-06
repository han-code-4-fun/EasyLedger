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

    @Query("SELECT * FROM transactions WHERE time >= :currentTime ORDER BY time DESC, id DESC")
    LiveData<List<TransactionEntry>> loadTransactionByTime7days(int currentTime);

//    LiveData<List<TransactionEntry>> loadTransactionByTime14days();
//
//    LiveData<List<TransactionEntry>> loadTransactionByTimeCurrentMonth();
//
//    LiveData<List<TransactionEntry>> loadTransactionByTimeUserDefined(int time);


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


    /*
        select account_id, total,  standard_qty from orders

        order by account_id desc, total desc limit 100

     */
}
