package com.xwray.groupie;

import android.support.annotation.NonNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ExpandableGroupTest {

    @Mock
    GroupAdapter groupAdapter;

    class DummyExpandableItem extends DummyItem implements ExpandableItem {

        @Override
        public void setExpandableGroup(@NonNull ExpandableGroup onToggleListener) {

        }
    }

    DummyExpandableItem parent = new DummyExpandableItem();

    @Test
    public void collapsedByDefault() throws Exception {
        ExpandableGroup expandableGroup = new ExpandableGroup(parent);
        assertFalse(expandableGroup.isExpanded());
    }

    @Test
    public void expandedByDefault() throws Exception {
        ExpandableGroup expandableGroup = new ExpandableGroup(parent, true);
        assertTrue(expandableGroup.isExpanded());
    }

    @Test
    public void noAddNotificationWhenCollapsed() throws Exception {
        ExpandableGroup expandableGroup = new ExpandableGroup(parent);
        expandableGroup.registerGroupDataObserver(groupAdapter);
        expandableGroup.add(new DummyItem());
        Mockito.verify(groupAdapter, Mockito.never()).onItemRangeInserted(expandableGroup, 1, 1);
    }

    @Test
    public void noChildAddNotificationWhenCollapsed() {
        ExpandableGroup expandableGroup = new ExpandableGroup(parent);
        expandableGroup.registerGroupDataObserver(groupAdapter);
        Section section = new Section();
        DummyItem item = new DummyItem();
        expandableGroup.add(section);
        section.add(item);
        Mockito.verify(groupAdapter, Mockito.never()).onItemRangeInserted(expandableGroup, 1, 1);
    }

    @Test
    public void addNotificationWhenExpanded() throws Exception {
        ExpandableGroup expandableGroup = new ExpandableGroup(parent);
        expandableGroup.onToggleExpanded();
        expandableGroup.registerGroupDataObserver(groupAdapter);
        expandableGroup.add(new DummyItem());
        Mockito.verify(groupAdapter).onItemRangeInserted(expandableGroup, 1, 1);
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
        Mockito.verify(groupAdapter).onItemRangeInserted(expandableGroup, 1, 1);
    }

    @Test
    public void testGetGroup() throws Exception {
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
    public void testGetHeaderPosition() throws Exception {
        ExpandableGroup expandableGroup = new ExpandableGroup(parent);
        assertEquals(0, expandableGroup.getPosition(parent));
    }

    @Test
    public void testUnexpandedGroupCount() throws Exception {
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
    public void expandNotifies() throws Exception {
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

        Mockito.verify(groupAdapter).onItemRangeInserted(expandableGroup, 1, 6);
    }

    @Test
    public void collapseNotifies() throws Exception {
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

        Mockito.verify(groupAdapter).onItemRangeRemoved(expandableGroup, 1, 6);
    }

    @Test
    public void testExpandedGroupCount() throws Exception {
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
    public void testExpandedGroupCountForAddAll() throws Exception {
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
    public void testExpandedGroupCountForAdd() throws Exception {
        ExpandableGroup expandableGroup = new ExpandableGroup(parent);
        DummyItem item = new DummyItem();
        expandableGroup.add(item);
        expandableGroup.registerGroupDataObserver(groupAdapter);
        expandableGroup.onToggleExpanded();
        assertEquals(2, expandableGroup.getGroupCount());
    }

    @Test
    public void testExpandedGroupCountForRemove() throws Exception {
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
    public void testGroupCountForRemoveAll() throws Exception {
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
}
