package hanzhou.easyledger.data;

import androidx.lifecycle.LiveData;
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

    @Insert
    void insertTransaction(TransactionEntry transactionEntry);

    @Insert
    void insertListOfTransactions(List<TransactionEntry> lisOfTransactions);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateTransaction(TransactionEntry transactionEntry);

    @Delete
    void deleteTask(TransactionEntry transactionEntry);

    @Query("SELECT * FROM transactions WHERE id = :id")
    LiveData<TransactionEntry> loadTransactionByID(int id);

    @Query("SELECT * FROM transactions WHERE ledger = :leger ORDER BY time DESC, id DESC")
    LiveData<List<TransactionEntry>> loadTransactionByLedger(String leger);
    /*
        select account_id, total,  standard_qty from orders

        order by account_id desc, total desc limit 100

     */
}
