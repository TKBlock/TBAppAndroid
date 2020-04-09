package io.taekwonblock.tkbblock.ui.dojo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DojoViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public DojoViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}