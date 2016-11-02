package com.genius.groupie;

import android.databinding.ViewDataBinding;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(MockitoJUnitRunner.class)
public class UpdatingGroupTest {

    @Mock GroupAdapter groupAdapter;

    class AlwaysUpdatingItem extends Item {
        @Override public int getLayout() { return 0; }
        @Override public void bind(ViewDataBinding viewBinding, int position) {}

        public AlwaysUpdatingItem(int id) {
            super(id);
        }

        @Override
        public boolean equals(Object obj) {
            return false;
        }
    }

    @Test
    public void updateGroupChangesRange() {
        List<Item> children = new ArrayList<Item>();
        children.add(new AlwaysUpdatingItem(1));
        children.add(new AlwaysUpdatingItem(2));

        UpdatingGroup group = new UpdatingGroup();
        group.setGroupDataObserver(groupAdapter);

        group.update(children);
        verify(groupAdapter).onItemRangeInserted(group, 0, 2);
        verifyNoMoreInteractions(groupAdapter);

        group.update(children);
        verify(groupAdapter).onItemRangeChanged(group, 0, 2);
        verifyNoMoreInteractions(groupAdapter);
    }

}
