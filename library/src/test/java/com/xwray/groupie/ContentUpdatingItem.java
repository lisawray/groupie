package com.xwray.groupie;

class ContentUpdatingItem extends DummyItem {

    String content;

    public ContentUpdatingItem(int id, String content) {
        super(id);
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ContentUpdatingItem that = (ContentUpdatingItem) o;

        return content != null ? content.equals(that.content) : that.content == null;
    }

    @Override
    public int hashCode() {
        return content != null ? content.hashCode() : 0;
    }
}
