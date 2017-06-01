package com.xwray.groupie;

import android.view.View;

import com.xwray.groupie.core.UpdatingGroup;

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

    @Mock
    GroupAdapter groupAdapter;

    class AlwaysUpdatingItem extends Item {
        @Override public int getLayout() { return 0; }

        public AlwaysUpdatingItem(int id) {
            super(id);
        }

        @Override
        public boolean equals(Object obj) {
            return false;
        }

        @Override
        public ViewHolder createViewHolder(View itemView) {
            return null;
        }

        @Override
        public void bind(ViewHolder viewHolder, int position) {

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

    @Test
    public void changeAnItemNotifiesChange() {
        List<Item> children = new ArrayList<Item>();
        Item item = new DummyItem();
        children.add(item);

        UpdatingGroup group = new UpdatingGroup();
        group.update(children);
        group.setGroupDataObserver(groupAdapter);

        item.notifyChanged();

        verify(groupAdapter).onItemChanged(group, 0);
    }

}
