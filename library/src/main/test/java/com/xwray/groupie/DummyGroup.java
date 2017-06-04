package com.xwray.groupie;

import com.xwray.groupie.core.Group;
import com.xwray.groupie.core.GroupDataObserver;

public abstract class DummyGroup implements Group<Item> {

    @Override
    public Item getItem(int position) {
        return null;
    }

    @Override
    public int getPosition(Item item) {
        return 0;
    }

    @Override
    public void setGroupDataObserver(GroupDataObserver groupDataObserver) {

    }
}
