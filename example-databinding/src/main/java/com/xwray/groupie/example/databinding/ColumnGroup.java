package com.xwray.groupie.example.databinding;

import com.xwray.groupie.core.Group;
import com.xwray.groupie.databinding.Item;
import com.xwray.groupie.core.GroupDataObserver;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple, non-editable, non-nested group of Items which displays a list as vertical columns.
 */
public class ColumnGroup implements Group<Item> {

    private List<Item> items = new ArrayList<>();

    public ColumnGroup(List<? extends Item> items) {
        for (int i = 0; i < items.size(); i++) {
            // Rearrange items so that the adapter appears to arrange them in vertical columns
            int index;
            if (i % 2 == 0) {
                index = i / 2;
            } else {
                index = (i - 1) / 2 + (int) (items.size() / 2f);
                // If columns are uneven, we'll put an extra one at the end of the first column,
                // meaning the second column's indices will all be increased by 1
                if (items.size() % 2 == 1) index++;
            }
            Item trackItem = items.get(index);
            this.items.add(trackItem);
        }
    }

    public void setGroupDataObserver(GroupDataObserver groupDataObserver) {
        // no real need to do anything here
    }

    public Item getItem(int position) {
        return items.get(position);
    }

    @Override
    public int getPosition(Item item) {
        return items.indexOf(item);
    }

    @Override public int getItemCount() {
        return items.size();
    }
}
