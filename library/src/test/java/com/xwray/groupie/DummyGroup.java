package com.xwray.groupie;

import androidx.annotation.NonNull;

public abstract class DummyGroup implements Group {

    @NonNull
    @Override
    public Item getItem(int position) {
        return null;
    }

    @Override
    public int getPosition(@NonNull Item item) {
        return 0;
    }

    @Override
    public void registerGroupDataObserver(@NonNull GroupDataObserver groupDataObserver) {

    }

    @Override
    public void unregisterGroupDataObserver(@NonNull GroupDataObserver groupDataObserver) {

    }
}
