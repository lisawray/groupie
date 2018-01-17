package com.xwray.groupie;

import static org.mockito.Mockito.*;

import org.junit.*;
import org.junit.runner.*;
import org.mockito.*;
import org.mockito.runners.*;

@RunWith(MockitoJUnitRunner.class)
public class ToggleGroupTest {

    @Mock
    GroupAdapter groupAdapter;

    private final DummyItem firstItem = new DummyItem(0);

    private final DummyItem secondItem = new DummyItem(1);

    private final ToggleGroup toggleGroup = new ToggleGroup(firstItem);

    @Before
    public void setUp() throws Exception {
        toggleGroup.registerGroupDataObserver(groupAdapter);
    }

    @Test
    public void testAddingItemsToGroup_ShouldNotCallAnythingFromAdapter() {
        toggleGroup.add(secondItem);

        verify(groupAdapter, never()).onItemRangeInserted(any(Group.class), anyInt(), anyInt());
        verify(groupAdapter, never()).onItemRangeChanged(any(Group.class), anyInt(), anyInt());
        verify(groupAdapter, never()).onItemRangeRemoved(any(Group.class), anyInt(), anyInt());
    }

    @Test
    public void testAddingItemsToGroup_ChangeVisibility_ShouldInsertInAdapter() {
        toggleGroup.add(secondItem);

        toggleGroup.setVisible(1);

        verify(groupAdapter, times(1)).onItemRangeChanged(any(Group.class), eq(0), eq(1));
    }

    @Test
    public void testSetVisibleMultipleTimes_ShouldChangeItemInAdapter() {
        toggleGroup.add(secondItem);

        toggleGroup.setVisible(1);
        toggleGroup.setVisible(0);
        toggleGroup.setVisible(1);
        toggleGroup.setVisible(0);

        verify(groupAdapter, times(4)).onItemRangeChanged(any(Group.class), eq(0), eq(1));
    }

}