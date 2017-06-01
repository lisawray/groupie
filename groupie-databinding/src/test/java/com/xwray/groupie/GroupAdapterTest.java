package com.xwray.groupie;

import com.xwray.groupie.core.Group;
import com.xwray.groupie.databinding.GroupAdapter;
import com.xwray.groupie.databinding.Item;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RunWith(MockitoJUnitRunner.class)
public class GroupAdapterTest {

    GroupAdapter groupAdapter;

    @Before
    public void setUp() throws Exception {
        groupAdapter = new GroupAdapter();
    }

    @Test(expected=RuntimeException.class)
    public void addItemMustBeNonNull() {
        groupAdapter.add(null);
    }

    @Test(expected=RuntimeException.class)
    public void addAllItemsMustBeNonNull() {
        List<Group<Item>> groups = new ArrayList<>();
        groups.add(null);
        groupAdapter.addAll(groups);
    }

    @Test(expected=RuntimeException.class)
    public void removeGroupMustBeNonNull() {
        groupAdapter.remove(null);
    }

    @Test(expected=RuntimeException.class)
    public void putGroupMustBeNonNull() {
        groupAdapter.add(0, null);
    }

    public void addAllWorksWithSets() {
        Set<Group<Item>> groupSet = new HashSet<>();
        groupSet.add(new DummyItem());
        groupSet.add(new DummyItem());

        groupAdapter.addAll(groupSet);
        Assert.assertEquals(2, groupAdapter.getItemCount());
    }

}