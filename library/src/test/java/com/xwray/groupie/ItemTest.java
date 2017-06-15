package com.xwray.groupie;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ItemTest {

    @Mock
    GroupAdapter groupAdapter;

    @Test
    public void selfPositionIs0() throws Exception {
        Item item = new DummyItem();
        Assert.assertEquals(0, item.getPosition(item));
    }

    @Test
    public void positionIsNegative1IfItemIsNotSelf() throws Exception {
        Item item = new DummyItem();
        Item differentItem = new DummyItem();
        Assert.assertEquals(-1, item.getPosition(differentItem));
    }

    @Test
    public void notifyChangeNotifiesParentObserver() {
        Item item = new DummyItem();
        item.registerGroupDataObserver(groupAdapter);
        item.notifyChanged();

        verify(groupAdapter).onItemChanged(item, 0);
    }
}