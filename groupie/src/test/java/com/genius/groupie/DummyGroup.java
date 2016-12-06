package com.genius.groupie;

public abstract class DummyGroup implements Group {

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
