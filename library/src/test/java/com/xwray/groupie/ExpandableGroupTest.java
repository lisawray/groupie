package com.xwray.groupie;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(MockitoJUnitRunner.class)
public class ExpandableGroupTest {

    @Mock
    GroupAdapter groupAdapter;

    static class DummyExpandableItem extends DummyItem implements ExpandableItem {

        @Override
        public void setExpandableGroup(@NonNull ExpandableGroup onToggleListener) {

        }
    }

    private DummyExpandableItem parent = new DummyExpandableItem();

    @Test
    public void collapsedByDefault() {
        ExpandableGroup expandableGroup = new ExpandableGroup(parent);
        assertFalse(expandableGroup.isExpanded());
    }

    @Test
    public void expandedByDefault() {
        ExpandableGroup expandableGroup = new ExpandableGroup(parent, true);
        assertTrue(expandableGroup.isExpanded());
    }

    @Test
    public void noAddNotificationWhenCollapsed() {
        ExpandableGroup expandableGroup = new ExpandableGroup(parent);
        expandableGroup.registerGroupDataObserver(groupAdapter);
        expandableGroup.add(new DummyItem());
        verify(groupAdapter, Mockito.never()).onItemRangeInserted(expandableGroup, 1, 1);
    }

    @Test
    public void noChildAddNotificationWhenCollapsed() {
        ExpandableGroup expandableGroup = new ExpandableGroup(parent);
        expandableGroup.registerGroupDataObserver(groupAdapter);
        Section section = new Section();
        DummyItem item = new DummyItem();
        expandableGroup.add(section);
        section.add(item);
        verify(groupAdapter, Mockito.never()).onItemRangeInserted(expandableGroup, 1, 1);
    }

    @Test
    public void addNotificationWhenExpanded() {
        ExpandableGroup expandableGroup = new ExpandableGroup(parent);
        expandableGroup.onToggleExpanded();
        expandableGroup.registerGroupDataObserver(groupAdapter);
        expandableGroup.add(new DummyItem());
        verify(groupAdapter).onItemRangeInserted(expandableGroup, 1, 1);
    }

    @Test
    public void childAddNotificationWhenExpanded() {
        ExpandableGroup expandableGroup = new ExpandableGroup(parent);
        expandableGroup.onToggleExpanded();
        expandableGroup.registerGroupDataObserver(groupAdapter);
        Section section = new Section();
        DummyItem item = new DummyItem();
        expandableGroup.add(section);
        section.add(item);
        verify(groupAdapter).onItemRangeInserted(expandableGroup, 1, 1);
    }

    @Test
    public void testGetGroup() {
        ExpandableGroup expandableGroup = new ExpandableGroup(parent);
        Section section = new Section();
        int sectionSize = 5;
        for (int i = 0; i < sectionSize; i++) {
            section.add(new DummyItem());
        }
        expandableGroup.add(section);
        Item lastItem = new DummyItem();
        expandableGroup.add(lastItem);
        expandableGroup.onToggleExpanded();

        assertEquals(lastItem, expandableGroup.getGroup(2));
    }

    @Test
    public void testGetHeaderPosition() {
        ExpandableGroup expandableGroup = new ExpandableGroup(parent);
        assertEquals(0, expandableGroup.getPosition(parent));
    }

    @Test
    public void testGetPosition() {
        ExpandableGroup expandableGroup = new ExpandableGroup(parent);
        Section section = new Section();
        expandableGroup.add(section);
        Section notAddedSection = new Section();

        assertEquals(1, expandableGroup.getPosition(section));
        assertEquals(-1, expandableGroup.getPosition(notAddedSection));
    }

    @Test
    public void testUnexpandedGroupCount() {
        ExpandableGroup expandableGroup = new ExpandableGroup(parent);
        Section section = new Section();
        int sectionSize = 5;
        for (int i = 0; i < sectionSize; i++) {
            section.add(new DummyItem());
        }
        expandableGroup.add(section);
        Item lastItem = new DummyItem();
        expandableGroup.add(lastItem);

        assertEquals(1, expandableGroup.getGroupCount());
    }

    @Test
    public void testUnexpandedChildCount() {
        ExpandableGroup expandableGroup = new ExpandableGroup(parent);
        Section section = new Section();
        int sectionSize = 5;
        for (int i = 0; i < sectionSize; i++) {
            section.add(new DummyItem());
        }
        expandableGroup.add(section);
        Item lastItem = new DummyItem();
        expandableGroup.add(lastItem);

        assertEquals(2, expandableGroup.getChildCount());
    }

    @Test
    public void expandNotifies() {
        ExpandableGroup expandableGroup = new ExpandableGroup(parent);
        Section section = new Section();
        int sectionSize = 5;
        for (int i = 0; i < sectionSize; i++) {
            section.add(new DummyItem());
        }
        expandableGroup.add(section);
        Item lastItem = new DummyItem();
        expandableGroup.add(lastItem);
        expandableGroup.registerGroupDataObserver(groupAdapter);
        expandableGroup.onToggleExpanded();

        verify(groupAdapter).onItemRangeInserted(expandableGroup, 1, 6);
    }

    @Test
    public void collapseNotifies() {
        ExpandableGroup expandableGroup = new ExpandableGroup(parent);
        Section section = new Section();
        int sectionSize = 5;
        for (int i = 0; i < sectionSize; i++) {
            section.add(new DummyItem());
        }
        expandableGroup.add(section);
        Item lastItem = new DummyItem();
        expandableGroup.add(lastItem);
        expandableGroup.onToggleExpanded();
        expandableGroup.registerGroupDataObserver(groupAdapter);
        expandableGroup.onToggleExpanded();

        verify(groupAdapter).onItemRangeRemoved(expandableGroup, 1, 6);
    }
    
    @Test
    public void setExpandedShouldntNotifiesExpandWhenTheValueNotChanged() {
        ExpandableGroup expandableGroup = new ExpandableGroup(parent, true);
        Section section = new Section();
        int sectionSize = 5;
        for (int i = 0; i < sectionSize; i++) {
            section.add(new DummyItem());
        }
        expandableGroup.add(section);
        Item lastItem = new DummyItem();
        expandableGroup.add(lastItem);
        expandableGroup.registerGroupDataObserver(groupAdapter);
        expandableGroup.setExpanded(true);

        verifyNoMoreInteractions(groupAdapter);
        assertTrue(expandableGroup.isExpanded());
    }

    @Test
    public void setExpandedShouldntNotifiesCollapseWhenTheValueNotChanged() {
        ExpandableGroup expandableGroup = new ExpandableGroup(parent, false);
        Section section = new Section();
        int sectionSize = 5;
        for (int i = 0; i < sectionSize; i++) {
            section.add(new DummyItem());
        }
        expandableGroup.add(section);
        Item lastItem = new DummyItem();
        expandableGroup.add(lastItem);
        expandableGroup.registerGroupDataObserver(groupAdapter);
        expandableGroup.setExpanded(false);

        verifyNoMoreInteractions(groupAdapter);
        assertFalse(expandableGroup.isExpanded());
    }

    @Test
    public void setExpandedShouldNotifiesExpandWhenTheValueChanged() {
        ExpandableGroup expandableGroup = new ExpandableGroup(parent, false);
        Section section = new Section();
        int sectionSize = 5;
        for (int i = 0; i < sectionSize; i++) {
            section.add(new DummyItem());
        }
        expandableGroup.add(section);
        Item lastItem = new DummyItem();
        expandableGroup.add(lastItem);
        expandableGroup.registerGroupDataObserver(groupAdapter);
        expandableGroup.setExpanded(true);

        verify(groupAdapter).onItemRangeInserted(expandableGroup, 1, 6);
        assertTrue(expandableGroup.isExpanded());
    }

    @Test
    public void setExpandedShouldNotifiesCollapseWhenTheValueChanged() {
        ExpandableGroup expandableGroup = new ExpandableGroup(parent, true);
        Section section = new Section();
        int sectionSize = 5;
        for (int i = 0; i < sectionSize; i++) {
            section.add(new DummyItem());
        }
        expandableGroup.add(section);
        Item lastItem = new DummyItem();
        expandableGroup.add(lastItem);
        expandableGroup.registerGroupDataObserver(groupAdapter);
        expandableGroup.setExpanded(false);

        verify(groupAdapter).onItemRangeRemoved(expandableGroup, 1, 6);
        assertFalse(expandableGroup.isExpanded());
    }

    @Test
    public void testExpandedGroupCount() {
        ExpandableGroup expandableGroup = new ExpandableGroup(parent);
        Section section = new Section();
        int sectionSize = 5;
        for (int i = 0; i < sectionSize; i++) {
            section.add(new DummyItem());
        }
        expandableGroup.add(section);
        Item lastItem = new DummyItem();
        expandableGroup.add(lastItem);
        expandableGroup.registerGroupDataObserver(groupAdapter);
        expandableGroup.onToggleExpanded();

        assertEquals(3, expandableGroup.getGroupCount());
    }

    @Test
    public void testExpandedGroupCountForAddAll() {
        ExpandableGroup expandableGroup = new ExpandableGroup(parent);
        List<DummyItem> items = new ArrayList<>();
        int itemsCount = 5;
        for (int i = 0; i < itemsCount; i++) {
            items.add(new DummyItem());
        }
        expandableGroup.addAll(items);
        expandableGroup.registerGroupDataObserver(groupAdapter);
        expandableGroup.onToggleExpanded();

        assertEquals(6, expandableGroup.getGroupCount());
    }

    @Test
    public void testExpandedGroupCountForAdd() {
        ExpandableGroup expandableGroup = new ExpandableGroup(parent);
        DummyItem item = new DummyItem();
        expandableGroup.add(item);
        expandableGroup.registerGroupDataObserver(groupAdapter);
        expandableGroup.onToggleExpanded();
        assertEquals(2, expandableGroup.getGroupCount());
    }

    @Test
    public void testAddAtPositionWhenExpanded() {
        final ExpandableGroup expandableGroup = new ExpandableGroup(parent);
        expandableGroup.add(new DummyItem());
        expandableGroup.add(new DummyItem());
        expandableGroup.onToggleExpanded();
        expandableGroup.registerGroupDataObserver(groupAdapter);

        final DummyItem newItem = new DummyItem();
        expandableGroup.add(1, newItem);

        assertEquals(newItem, expandableGroup.getGroup(2));
        assertEquals(4, expandableGroup.getGroupCount());
        verify(groupAdapter).onItemRangeInserted(expandableGroup, 2, 1);
    }

    @Test
    public void testAddAllAtPositionWhenExpanded() {
        final ExpandableGroup expandableGroup = new ExpandableGroup(parent);
        expandableGroup.add(new DummyItem());
        expandableGroup.onToggleExpanded();
        expandableGroup.registerGroupDataObserver(groupAdapter);

        final List<DummyItem> newItems = new ArrayList<>();
        newItems.add(new DummyItem());
        newItems.add(new DummyItem());
        expandableGroup.addAll(0, newItems);

        assertEquals(newItems.get(0), expandableGroup.getGroup(1));
        assertEquals(newItems.get(1), expandableGroup.getGroup(2));
        assertEquals(4, expandableGroup.getGroupCount());
        verify(groupAdapter).onItemRangeInserted(expandableGroup, 1, 2);
    }

    @Test
    public void testAddAtPositionWhenCollapsed() {
        final ExpandableGroup expandableGroup = new ExpandableGroup(parent);
        expandableGroup.add(new DummyItem());
        expandableGroup.registerGroupDataObserver(groupAdapter);

        final DummyItem newItem = new DummyItem();
        expandableGroup.add(0, newItem);

        assertEquals(1, expandableGroup.getItemCount());
        verifyNoMoreInteractions(groupAdapter);
    }

    @Test
    public void testExpandedGroupCountForRemove() {
        ExpandableGroup expandableGroup = new ExpandableGroup(parent);
        List<DummyItem> items = new ArrayList<>();
        int itemsCount = 5;
        for (int i = 0; i < itemsCount; i++) {
            items.add(new DummyItem());
        }
        expandableGroup.addAll(items);
        expandableGroup.registerGroupDataObserver(groupAdapter);

        expandableGroup.remove(items.get(0));

        expandableGroup.onToggleExpanded();
        assertEquals(5, expandableGroup.getGroupCount());
    }


    @Test
    public void testGroupCountForRemoveAll() {
        ExpandableGroup expandableGroup = new ExpandableGroup(parent);
        List<DummyItem> items = new ArrayList<>();
        int itemsCount = 5;
        for (int i = 0; i < itemsCount; i++) {
            items.add(new DummyItem());
        }
        expandableGroup.addAll(items);
        expandableGroup.registerGroupDataObserver(groupAdapter);

        expandableGroup.removeAll(items);

        expandableGroup.onToggleExpanded();
        assertEquals(1, expandableGroup.getGroupCount());
    }

    @Test
    public void testExpandedChildCount() {
        ExpandableGroup expandableGroup = new ExpandableGroup(parent);
        Section section = new Section();
        int sectionSize = 5;
        for (int i = 0; i < sectionSize; i++) {
            section.add(new DummyItem());
        }
        expandableGroup.add(section);
        Item lastItem = new DummyItem();
        expandableGroup.add(lastItem);
        expandableGroup.registerGroupDataObserver(groupAdapter);
        expandableGroup.onToggleExpanded();

        assertEquals(2, expandableGroup.getChildCount());
    }

    @Test
    public void testExpandedChildCountForAddAll() {
        ExpandableGroup expandableGroup = new ExpandableGroup(parent);
        List<DummyItem> items = new ArrayList<>();
        int itemsCount = 5;
        for (int i = 0; i < itemsCount; i++) {
            items.add(new DummyItem());
        }
        expandableGroup.addAll(items);
        expandableGroup.registerGroupDataObserver(groupAdapter);
        expandableGroup.onToggleExpanded();

        assertEquals(5, expandableGroup.getChildCount());
    }

    @Test
    public void testExpandedChildCountForAdd() {
        ExpandableGroup expandableGroup = new ExpandableGroup(parent);
        DummyItem item = new DummyItem();
        expandableGroup.add(item);
        expandableGroup.registerGroupDataObserver(groupAdapter);
        expandableGroup.onToggleExpanded();
        assertEquals(1, expandableGroup.getChildCount());
    }

    @Test
    public void testExpandedChildCountForRemove() {
        ExpandableGroup expandableGroup = new ExpandableGroup(parent);
        List<DummyItem> items = new ArrayList<>();
        int itemsCount = 5;
        for (int i = 0; i < itemsCount; i++) {
            items.add(new DummyItem());
        }
        expandableGroup.addAll(items);
        expandableGroup.registerGroupDataObserver(groupAdapter);

        expandableGroup.remove(items.get(0));

        expandableGroup.onToggleExpanded();
        assertEquals(4, expandableGroup.getChildCount());
    }


    @Test
    public void testChildCountForRemoveAll() {
        ExpandableGroup expandableGroup = new ExpandableGroup(parent);
        List<DummyItem> items = new ArrayList<>();
        int itemsCount = 5;
        for (int i = 0; i < itemsCount; i++) {
            items.add(new DummyItem());
        }
        expandableGroup.addAll(items);
        expandableGroup.registerGroupDataObserver(groupAdapter);

        expandableGroup.removeAll(items);

        expandableGroup.onToggleExpanded();
        assertEquals(0, expandableGroup.getChildCount());
    }
}
