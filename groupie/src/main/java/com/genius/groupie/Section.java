package com.genius.groupie;

import android.databinding.repacked.google.common.base.Preconditions;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * A group which has a list of contents and an optional header and footer.
 */
public class Section extends NestedGroup {
    private @Nullable Group header;
    private @Nullable Group footer;
    private final ArrayList<Group> children = new ArrayList<>();
    private boolean hideWhenEmpty = false;
    private boolean isHidden = false;

    public Section() {
        this(null, new ArrayList<Group>());
    }

    public Section(Group header) {
        this(header, new ArrayList<Group>());
    }

    public Section(List<Group> children) {
        this(null, children);
    }

    public Section(@Nullable Group header, List<Group> children) {
        this.header = header;
        addAll(children);
    }

    @Override public void add(int position, Group group) {
        super.add(position, group);
        children.add(position, group);
        notifyItemRangeInserted(getHeaderItemCount() + position, group.getItemCount());
        showHeadersAndFootersIfNecessary();
    }

    @Override public void addAll(int position, List<Group> groups) {
        super.addAll(position, groups);
        this.children.addAll(position, groups);
        notifyItemRangeInserted(getHeaderItemCount() + position, getItemCount(groups));
        showHeadersAndFootersIfNecessary();
    }

    @Override public void add(Group group) {
        super.add(group);
        int position = getItemCountWithoutFooter();
        children.add(group);
        notifyItemInserted(getHeaderItemCount() + position);
        showHeadersAndFootersIfNecessary();
    }

    @Override public void remove(Group group) {
        super.remove(group);
        int position = getPosition(group);
        children.remove(group);
        notifyItemRangeRemoved(position, group.getItemCount());
        hideHeadersAndFootersIfNecessary();
    }

    protected void hideHeadersAndFootersIfNecessary() {
        if (hideWhenEmpty && isEmpty() && !isHidden) {
            hideHeadersAndFooters();
        }
    }

    /**
     * Whether a section's contents are visually empty
     *
     * @return
     */
    protected boolean isEmpty() {
        return children.isEmpty() || getItemCount(children) == 0;
    }

    private void hideHeadersAndFooters() {
        isHidden = true;
        int count = getHeaderItemCount() + getFooterItemCount();
        notifyItemRangeRemoved(0, count);
    }

    protected void showHeadersAndFootersIfNecessary() {
        if (hideWhenEmpty && isHidden && !isEmpty()) {
            showHeadersAndFooters();
        }
    }

    private void showHeadersAndFooters() {
        isHidden = false;
        notifyItemRangeInserted(0, getHeaderItemCount());
        notifyItemRangeInserted(getItemCountWithoutFooter(), getFooterItemCount());
    }

    @Override public void addAll(List<Group> groups) {
        if (groups.isEmpty()) return;
        super.addAll(groups);
        int position = getItemCountWithoutFooter();
        this.children.addAll(groups);
        notifyItemRangeInserted(position, getItemCount(groups));
        showHeadersAndFootersIfNecessary();
    }

    private int getItemCountWithoutFooter() {
        return children.size() + getHeaderItemCount();
    }

    private int getHeaderCount() {
        return header == null ? 0 : 1;
    }

    private int getHeaderItemCount() {
        return header == null ? 0 : header.getItemCount();
    }

    private int getFooterItemCount() {
        return footer == null ? 0 : footer.getItemCount();
    }

    private int getFooterCount() {
        return footer == null ? 0 : 1;
    }

    @Override public Group getGroup(int position) {
        if (header != null && position == 0) return header;
        position -= getHeaderCount();
        if (position == children.size()) {
            return footer;
        } else {
            return children.get(position);
        }
    }

    @Override public int getGroupCount() {
        if (hideWhenEmpty && isEmpty()) {
            return 0;
        }

        return getHeaderCount() + getFooterCount() + children.size();
    }

    @Override public int getPosition(Group group) {
        int count = 0;
        if (header != null) {
            if (group == header) return count;
            count++;
        }
        int index = children.indexOf(group);
        if (index >= 0) return count + index;
        count += children.size();
        if (footer == group) {
            return count;
        }
        return -1;
    }

    public void setHeader(@NonNull Group header) {
        Preconditions.checkNotNull(header);
        int previousHeaderItemCount = getHeaderItemCount();
        this.header = header;
        notifyHeaderItemsChanged(previousHeaderItemCount);
    }

    public void removeHeader(){
        int previousHeaderItemCount = getHeaderItemCount();
        this.header = null;
        notifyHeaderItemsChanged(previousHeaderItemCount);
    }

    private void notifyHeaderItemsChanged(int previousHeaderItemCount) {
        int newHeaderItemCount = getHeaderItemCount();
        if (previousHeaderItemCount > 0) {
            notifyItemRangeRemoved(0, previousHeaderItemCount);
        }
        if (newHeaderItemCount > 0) {
            notifyItemRangeInserted(0, newHeaderItemCount);
        }
    }


    public void setFooter(@NonNull Group footer) {
        Preconditions.checkNotNull(footer);
        int previousFooterItemCount = getFooterItemCount();
        this.footer = footer;
        notifyFooterItemsChanged(previousFooterItemCount);
    }

    public void removeFooter(){
        int previousFooterItemCount = getFooterItemCount();
        this.footer = null;
        notifyFooterItemsChanged(previousFooterItemCount);
    }

    private void notifyFooterItemsChanged(int previousFooterItemCount) {
        int newFooterItemCount = getFooterItemCount();
        if (previousFooterItemCount > 0) {
            notifyItemRangeRemoved(getItemCountWithoutFooter(), previousFooterItemCount);
        }
        if (newFooterItemCount > 0) {
            notifyItemRangeInserted(getItemCountWithoutFooter(), newFooterItemCount);
        }
    }

    public void setHideWhenEmpty(boolean hide) {
        if (hideWhenEmpty == hide) return;
        if (!hide && isHidden) {
            showHeadersAndFooters();
        }

        hideWhenEmpty = hide;
        hideHeadersAndFootersIfNecessary();
    }

    @Override public void notifyItemRangeInserted(int positionStart, int itemCount) {
        super.notifyItemRangeInserted(positionStart, itemCount);
        showHeadersAndFootersIfNecessary();
    }

    @Override public void notifyItemRangeRemoved(int positionStart, int itemCount) {
        super.notifyItemRangeRemoved(positionStart, itemCount);
        hideHeadersAndFootersIfNecessary();
    }

    @Override public void notifyItemInserted(int position) {
        super.notifyItemInserted(position);
        showHeadersAndFootersIfNecessary();
    }

    @Override public void notifyItemRemoved(int position) {
        super.notifyItemRemoved(position);
        hideHeadersAndFootersIfNecessary();
    }
}
