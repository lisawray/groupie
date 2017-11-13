package com.xwray.groupie;

class AlwaysUpdatingItem extends DummyItem {

    public AlwaysUpdatingItem(int id) {
        super(id);
    }

    @Override
    public boolean equals(Object obj) {
        return false;
    }

}
