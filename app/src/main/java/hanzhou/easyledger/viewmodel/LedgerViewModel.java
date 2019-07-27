package hanzhou.easyledger.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class LedgerViewModel extends AndroidViewModel {

    private MutableLiveData<String> mLedgerName = new MutableLiveData<>();

    public LedgerViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<String> getmLedgerName() {
        return mLedgerName;
    }

    public void setmLedgerName(String ledgerName) {
        this.mLedgerName.setValue(ledgerName);
    }
}
