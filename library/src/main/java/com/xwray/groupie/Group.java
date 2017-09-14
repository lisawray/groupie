package com.xwray.groupie;

import android.support.annotation.NonNull;

/**
 * A group of items, to be used in an adapter.
 */
public interface Group {

    int getItemCount();

    @NonNull Item getItem(int position);

    /**
     * Gets the position of a
     * @param item
     * @return
     */
    int getPosition(@NonNull Item item);

    void registerGroupDataObserver(@NonNull GroupDataObserver groupDataObserver);

    void unregisterGroupDataObserver(@NonNull GroupDataObserver groupDataObserver);

}