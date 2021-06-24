package com.xwray.groupie;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class DiffCallback extends DiffUtil.Callback {
    private final int oldBodyItemCount;
    private final int newBodyItemCount;
    private final List<Group> oldGroups;
    private final List<Group> newGroups;

    private final Map<Integer, Integer> changeMap = new HashMap<>();

    DiffCallback(Collection<? extends Group> oldGroups, Collection<? extends Group> newGroups) {
        this.oldBodyItemCount = GroupUtils.getItemCount(oldGroups);
        this.newBodyItemCount = GroupUtils.getItemCount(newGroups);
        this.oldGroups = new ArrayList<>(oldGroups);
        this.newGroups = new ArrayList<>(newGroups);
    }

    @Override
    public int getOldListSize() {
        return oldBodyItemCount;
    }

    @Override
    public int getNewListSize() {
        return newBodyItemCount;
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        Item oldItem = GroupUtils.getItem(oldGroups, oldItemPosition);
        Item newItem = GroupUtils.getItem(newGroups, newItemPosition);
        return newItem.isSameAs(oldItem);
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        Item oldItem = GroupUtils.getItem(oldGroups, oldItemPosition);
        Item newItem = GroupUtils.getItem(newGroups, newItemPosition);
        boolean sameContent = newItem.hasSameContentAs(oldItem);

        //false means changed
        if (sameContent) {
            changeMap.put(newItemPosition, oldItemPosition);
        }
        return sameContent;
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        Item oldItem = GroupUtils.getItem(oldGroups, oldItemPosition);
        Item newItem = GroupUtils.getItem(newGroups, newItemPosition);
        return oldItem.getChangePayload(newItem);
    }

    @NonNull
    public List<Group> mergeGroups() {
        ArrayList<Group> resultList = new ArrayList<>();

        for (int i = 0; i < newGroups.size(); i++) {
            Integer position = changeMap.get(i);

            if (position == null) {
                resultList.add(newGroups.get(i));
            } else {
                resultList.add(oldGroups.get(position));
            }
        }
        return resultList;
    }
}