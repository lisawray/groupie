package com.xwray.groupie;

import java.util.Objects;

class ContentUpdatingItem extends DummyItem {

    private String content;

    ContentUpdatingItem(int id, String content) {
        super(id);
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ContentUpdatingItem that = (ContentUpdatingItem) o;

        return Objects.equals(content, that.content);
    }

    @Override
    public int hashCode() {
        return content != null ? content.hashCode() : 0;
    }
}
