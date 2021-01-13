package com.xwray.groupie;

import androidx.annotation.NonNull;

public interface GroupDataObserver {
    void onChanged(@NonNull Group group);

    void onItemInserted(@NonNull Group group, int position);

    void onItemChanged(@NonNull Group group, int position);

    void onItemChanged(@NonNull Group group, int position, Object payload);

    void onItemRemoved(@NonNull Group group, int position);

    void onItemRangeChanged(@NonNull Group group, int positionStart, int itemCount);

    void onItemRangeChanged(@NonNull Group group, int positionStart, int itemCount, Object payload);

    void onItemRangeInserted(@NonNull Group group, int positionStart, int itemCount);

    void onItemRangeRemoved(@NonNull Group group, int positionStart, int itemCount);

    void onItemMoved(@NonNull Group group, int fromPosition, int toPosition);

    void onDataSetInvalidated();
}
