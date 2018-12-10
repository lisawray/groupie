package com.xwray.groupie.example.databinding.item;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import com.xwray.groupie.databinding.BindableItem;
import com.xwray.groupie.example.databinding.R;
import com.xwray.groupie.example.databinding.databinding.ItemLiveDataBinding;

public class LiveDataItem extends BindableItem<ItemLiveDataBinding> {

    // Simple wrapper around liveData
    public class DataWrapper {
        private MutableLiveData<String> data = new MutableLiveData<>();

        public MutableLiveData<String> getData() {
            return data;
        }
    }

    public DataWrapper internalDataWrapper = new DataWrapper();

    public LiveDataItem() {
        super();
    }

    @Override public int getLayout() {
        return R.layout.item_live_data;
    }

    @Override public void bind(@NonNull ItemLiveDataBinding viewBinding, int position) {
        viewBinding.setWrapper(internalDataWrapper);
    }
}
