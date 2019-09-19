package com.xwray.groupie;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GroupUtilsTest {

    @Test
    public void getItemReturnsCorrectItem() {
        final Group itemContainingGroup = createMockGroup(8);
        final Item mockItem = mock(Item.class);
        when(itemContainingGroup.getItem(1)).thenReturn(mockItem);

        final List<Group> groups = new ArrayList<>();
        groups.add(createMockGroup(5));
        groups.add(itemContainingGroup);
        groups.add(createMockGroup(2));

        assertEquals(mockItem, GroupUtils.getItem(groups, 6));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void getItemThrowsWhenItemNotPresent() {
        final List<Group> groups = new ArrayList<>();
        groups.add(createMockGroup(5));
        groups.add(createMockGroup(5));

        GroupUtils.getItem(groups, 10);
    }

    @Test
    public void getItemCountSumsItemsCorrectly() {
        final List<Group> groups = new ArrayList<>();
        groups.add(createMockGroup(5));
        groups.add(createMockGroup(8));
        groups.add(createMockGroup(2));

        assertEquals(15, GroupUtils.getItemCount(groups));
    }

    private Group createMockGroup(int itemCount) {
        final Group mock = mock(Group.class);

        when(mock.getItemCount()).thenReturn(itemCount);

        return mock;
    }
}