package com.genius.groupie;

import junit.framework.Assert;

import org.junit.Test;

public class ItemTest {

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
}