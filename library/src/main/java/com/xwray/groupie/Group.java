package com.xwray.groupie;

import androidx.annotation.NonNull;

/**
 * A group of items, to be used in an adapter.
 */
public interface Group {

    int getItemCount();

    @NonNull Item getItem(int position);

    /**
     * Gets the position of an item inside this Group
     * @param item item to return position of
     * @return The position of the item or -1 if not present
     */
    int getPosition(@NonNull Item item);

    void registerGroupDataObserver(@NonNull GroupDataObserver groupDataObserver);

    void unregisterGroupDataObserver(@NonNull GroupDataObserver groupDataObserver);

}