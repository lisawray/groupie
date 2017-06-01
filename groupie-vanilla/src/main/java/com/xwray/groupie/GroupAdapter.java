package com.xwray.groupie;

import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xwray.groupie.core.BaseGroupAdapter;

/**
 * An adapter that holds a list of Groups.
 */
public class GroupAdapter extends BaseGroupAdapter<Item, ViewHolder> {

    private Item lastItemForViewTypeLookup;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int layoutResId) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        Item item = getItemForViewType(layoutResId);
        View itemView = inflater.inflate(layoutResId, parent, false);
        return item.createViewHolder(itemView);
    }

    @Override
    public int getItemViewType(int position) {
        lastItemForViewTypeLookup = getItem(position);
        return lastItemForViewTypeLookup.getLayout();
    }

    /**
     * This idea was copied from Epoxy. :wave: Bright idea guys!
     *
     * Find the model that has the given view type so we can create a viewholder for that model.
     *
     * To make this efficient, we rely on the RecyclerView implementation detail that {@link
     * BaseGroupAdapter#getItemViewType(int)} is called immediately before {@link
     * BaseGroupAdapter#onCreateViewHolder(android.view.ViewGroup, int)}. We cache the last model
     * that had its view type looked up, and unless that implementation changes we expect to have a
     * very fast lookup for the correct model.
     * <p>
     * To be safe, we fallback to searching through all models for a view type match. This is slow and
     * shouldn't be needed, but is a guard against RecyclerView behavior changing.
     */
    private Item getItemForViewType(@LayoutRes int layoutResId) {
        if (lastItemForViewTypeLookup != null
                && lastItemForViewTypeLookup.getLayout() == layoutResId) {
            // We expect this to be a hit 100% of the time
            return lastItemForViewTypeLookup;
        }

        // To be extra safe in case RecyclerView implementation details change...
        for (int i = 0; i < getItemCount(); i++) {
            Item item = getItem(i);
            if (item.getLayout() == layoutResId) {
                return item;
            }
        }

        throw new IllegalStateException("Could not find model for view type: " + layoutResId);
    }
}
