package com.xwray.groupie;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class GroupAdapterTest {

    private GroupAdapter<GroupieViewHolder> groupAdapter;

    @Before
    public void setUp() {
        groupAdapter = new GroupAdapter<>();
    }

    @Test(expected = RuntimeException.class)
    public void addItemMustBeNonNull() {
        groupAdapter.add(null);
    }

    @Test(expected = RuntimeException.class)
    public void addAllItemsMustBeNonNull() {
        List<Group> groups = new ArrayList<>();
        groups.add(null);
        groupAdapter.addAll(groups);
    }

    @Test(expected = RuntimeException.class)
    public void removeGroupMustBeNonNull() {
        groupAdapter.remove(null);
    }

    @Test(expected = RuntimeException.class)
    public void putGroupMustBeNonNull() {
        groupAdapter.add(0, null);
    }

    public void addAllWorksWithSets() {
        Set<Group> groupSet = new HashSet<>();
        groupSet.add(new DummyItem());
        groupSet.add(new DummyItem());

        groupAdapter.addAll(groupSet);
        assertEquals(2, groupAdapter.getItemCount());
    }

}
