package hanzhou.easyledger.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import hanzhou.easyledger.data.RepositoryDB;
import hanzhou.easyledger.utility.Constant;

public class GeneralViewModel extends AndroidViewModel {

    private MutableLiveData<String> mCurrentScreen = new MutableLiveData<>();

    private MutableLiveData<String> mCurrentLedger = new MutableLiveData<>();


    private MutableLiveData<Boolean> mIsSwitchViewPager = new MutableLiveData<>();

    private MutableLiveData<Boolean> mIsInBaseFragment = new MutableLiveData<>();


    public GeneralViewModel(@NonNull Application application) {
        super(application);
        mCurrentLedger.setValue(Constant.LEDGER_OVERALL);
    }

    public void setmCurrentScreen(String name) {
        mCurrentScreen.setValue(name);
        if (name.equals(Constant.FRAG_NAME_CHART) ||
                name.equals(Constant.FRAG_NAME_OVERVIEW) ||
                name.equals(Constant.FRAG_NAME_LEDGER)) {
            mIsInBaseFragment.setValue(true);
        }else{
            mIsInBaseFragment.setValue(false);
        }
    }

    public LiveData<String> getCurrentScreen() {
        return mCurrentScreen;
    }

    public LiveData<Boolean> getmIsSwitchViewPager() {
        return mIsSwitchViewPager;
    }

    public void setmIsSwitchViewPager(boolean input) {
        this.mIsSwitchViewPager.setValue(input);
    }

    public LiveData<Boolean> getmIsInBaseFragment() {
        return mIsInBaseFragment;
    }

    public LiveData<String> getmCurrentLedger() {
        return mCurrentLedger;
    }

    public void setmCurrentLedger(String input) {
        this.mCurrentLedger.setValue(input);
    }
}
