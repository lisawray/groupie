package com.xwray.groupie.example.databinding.item;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import com.xwray.groupie.databinding.BindableItem;
import com.xwray.groupie.example.databinding.R;
import com.xwray.groupie.example.databinding.databinding.ItemLiveDataBinding;

import java.util.Random;


public class LiveDataItem extends BindableItem<ItemLiveDataBinding> {

    // Simple wrapper around liveData
    public class DataWrapper {
        private MutableLiveData<String> data = new MutableLiveData<>();


        public MutableLiveData<String> getData() {
            return data;
        }
    }

    private DataWrapper internalDataWrapper = new DataWrapper();

    public LiveDataItem() {
        super();
        // simple thread which postValue in the liveData every 500ms
        new Thread(new Runnable() {
            @Override
            public void run() {
                Random random = new Random();
                while(true) {
                    internalDataWrapper.data.postValue(String.valueOf(random.nextLong()));
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @Override public int getLayout() {
        return R.layout.item_live_data;
    }

    @Override public void bind(@NonNull ItemLiveDataBinding viewBinding, int position) {
        viewBinding.setWrapper(internalDataWrapper);
        viewBinding.executePendingBindings();
    }
}
