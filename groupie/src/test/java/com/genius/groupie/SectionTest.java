package com.genius.groupie;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class SectionTest {

    @Mock GroupAdapter groupAdapter;
    final int footerSize = 5;
    Group footer = new Group() {
        @Override public int getItemCount() {
            return footerSize;
        }

        @Override public Item getItem(int position) {
            return null;
        }

        @Override public int getPosition(Item item) {
            return 0;
        }

        @Override public void setGroupDataObserver(GroupDataObserver groupDataObserver) {

        }
    };

    final int headerSize = 2;
    Group header = new Group() {
        @Override public int getItemCount() {
            return headerSize;
        }

        @Override public Item getItem(int position) {
            return null;
        }

        @Override public int getPosition(Item item) {
            return 0;
        }

        @Override public void setGroupDataObserver(GroupDataObserver groupDataObserver) {

        }
    };

    @Test public void settingFooterNotifiesFooterAdded() {
        Section section = new Section();
        section.setHeader(header);
        section.add(new DummyItem());
        section.setGroupDataObserver(groupAdapter);
        section.setFooter(footer);

        verify(groupAdapter).onItemRangeInserted(section, headerSize + 1, footerSize);
    }

    @Test public void removingFooterNotifiesPreviousFooterRemoved() {
        Section section = new Section();
        section.setHeader(header);
        section.add(new DummyItem());
        section.setFooter(footer);
        section.setGroupDataObserver(groupAdapter);
        section.removeFooter();

        verify(groupAdapter).onItemRangeRemoved(section, headerSize + 1, footerSize);
    }

    @Test(expected=NullPointerException.class)
    public void settingNullFooterThrowsNullPointerException(){
        Section section = new Section();
        section.setFooter(null);
    }

    @Test public void footerCountIs0WhenThereIsNoFooter() {
        Section section = new Section();
        section.removeFooter();

        assertEquals(0, section.getItemCount());
    }

    @Test public void footerCountIsSizeOfFooter() {
        Section section = new Section();

        section.setFooter(footer);
        assertEquals(footerSize, section.getItemCount());
    }

    @Test public void settingHeaderNotifiesHeaderAdded() {
        Section section = new Section();
        section.setGroupDataObserver(groupAdapter);
        section.setHeader(header);

        verify(groupAdapter).onItemRangeInserted(section, 0, headerSize);
    }

    @Test public void RemovingHeaderNotifiesPreviousHeaderRemoved() {
        Section section = new Section();
        section.setGroupDataObserver(groupAdapter);
        section.setHeader(header);
        section.removeHeader();

        verify(groupAdapter).onItemRangeRemoved(section, 0, headerSize);
    }

    @Test(expected=NullPointerException.class)
    public void settingNullHeaderThrowsNullPointerException(){
        Section section = new Section();
        section.setFooter(null);
    }

    @Test public void headerCountIs0WhenThereIsNoHeader() {
        Section section = new Section();
        section.removeHeader();

        assertEquals(0, section.getItemCount());
    }

    @Test public void headerCountIsSizeOfHeader() {
        Section section = new Section();

        section.setHeader(header);
        assertEquals(headerSize, section.getItemCount());
    }

    @Test public void getGroup() {
        Section section = new Section();
        Item item = new DummyItem();
        section.add(item);
        assertEquals(0, section.getPosition(item));
    }

    @Test public void getPositionReturnsNegativeIfItemNotPresent() {
        Section section = new Section();
        Item item = new DummyItem();
        assertEquals(-1, section.getPosition(item));
    }

    @Test public void constructorSetsListenerOnChildren() {
        List<Group> children = new ArrayList<>();
        Item item = Mockito.mock(Item.class);
        children.add(item);
        Section section = new Section(null, children);

        verify(item).setGroupDataObserver(section);
    }
}
