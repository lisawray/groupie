package com.genius.groupie;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

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
        List<Group> groups = new ArrayList<>();
        groups.add(null);
        groupAdapter.addAll(groups);
    }

    @Test(expected=RuntimeException.class)
    public void removeGroupMustBeNonNull() {
        groupAdapter.remove(null);
    }

    @Test(expected=RuntimeException.class)
    public void putGroupMustBeNonNull() {
        groupAdapter.putGroup(0, null);
    }

}