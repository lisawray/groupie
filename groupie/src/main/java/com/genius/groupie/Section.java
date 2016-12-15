package com.genius.groupie;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * A group which has a list of contents and an optional header and footer.
 */
public class Section extends NestedGroup {
    @Nullable
    private Group header;
    @Nullable
    private Group footer;
    @Nullable
    private Group placeholder;
    private final ArrayList<Group> children = new ArrayList<>();
    private boolean hideWhenEmpty = false;
    private boolean isHeaderAndFooterVisible = true;
    private boolean isPlaceholderVisible = false;

    public Section() {
        this(null, new ArrayList<Group>());
    }

    public Section(Group header) {
        this(header, new ArrayList<Group>());
    }

    public Section(List<? extends Group> children) {
        this(null, children);
    }

    public Section(@Nullable Group header, List<? extends Group> children) {
        this.header = header;
        addAll(children);
    }

    @Override
    public void add(int position, Group group) {
        super.add(position, group);
        children.add(position, group);
        final int notifyPosition = getHeaderItemCount() + getItemCount(children.subList(0, position));
        notifyItemRangeInserted(notifyPosition, group.getItemCount());
        refreshEmptyState();
    }

    @Override
    public void addAll(List<? extends Group> groups) {
        if (groups.isEmpty()) return;
        super.addAll(groups);
        int position = getItemCountWithoutFooter();
        this.children.addAll(groups);
        notifyItemRangeInserted(position, getItemCount(groups));
        refreshEmptyState();
    }

    @Override
    public void addAll(int position, List<? extends Group> groups) {
        if (groups.isEmpty()) {
            return;
        }

        super.addAll(position, groups);
        this.children.addAll(position, groups);

        final int notifyPosition = getHeaderItemCount() + getItemCount(children.subList(0, position));
        notifyItemRangeInserted(notifyPosition, getItemCount(groups));
        refreshEmptyState();
    }

    @Override
    public void add(Group group) {
        super.add(group);
        int position = getItemCountWithoutFooter();
        children.add(group);
        notifyItemRangeInserted(getHeaderItemCount() + position, group.getItemCount());
        refreshEmptyState();
    }

    @Override
    public void remove(Group group) {
        super.remove(group);
        int position = getPosition(group);
        children.remove(group);
        notifyItemRangeRemoved(position, group.getItemCount());
        refreshEmptyState();
    }

    /**
     * Optional. Set a placeholder for when the section's body is empty.
     * <p>
     * If setHideWhenEmpty(true) is set, then the empty placeholder will not be shown.
     *
     * @param placeholder A placeholder to be shown when there is no body content
     */
    public void setPlaceholder(@NonNull Group placeholder) {
        if (placeholder == null)
            throw new NullPointerException("Placeholder can't be null.  Please use removePlaceholder() instead!");
        this.placeholder = placeholder;
        refreshEmptyState();
    }

    public void removePlaceholder() {
        hidePlaceholder();
        this.placeholder = null;
    }

    private void showPlaceholder() {
        if (isPlaceholderVisible || placeholder == null) return;
        isPlaceholderVisible = true;
        notifyItemRangeInserted(getHeaderItemCount(), placeholder.getItemCount());
    }

    private void hidePlaceholder() {
        if (!isPlaceholderVisible || placeholder == null) return;
        isPlaceholderVisible = false;
        notifyItemRangeRemoved(getHeaderItemCount(), placeholder.getItemCount());
    }

    /**
     * Whether a section's contents are visually empty
     *
     * @return
     */
    protected boolean isEmpty() {
        return children.isEmpty() || getItemCount(children) == 0;
    }

    private void hideDecorations() {
        if (!isHeaderAndFooterVisible && !isPlaceholderVisible) return;
        int count = getHeaderItemCount() + getPlaceholderItemCount() + getFooterItemCount();
        isHeaderAndFooterVisible = false;
        isPlaceholderVisible = false;
        notifyItemRangeRemoved(0, count);
    }

    protected void refreshEmptyState() {
        boolean isEmpty = isEmpty();
        if (isEmpty) {
            if (hideWhenEmpty) {
                hideDecorations();
            } else {
                showPlaceholder();
                showHeadersAndFooters();
            }
        } else {
            hidePlaceholder();
            showHeadersAndFooters();
        }
    }

    private void showHeadersAndFooters() {
        if (isHeaderAndFooterVisible) return;
        isHeaderAndFooterVisible = true;
        notifyItemRangeInserted(0, getHeaderItemCount());
        notifyItemRangeInserted(getItemCountWithoutFooter(), getFooterItemCount());
    }

    private int getBodyItemCount() {
        return isPlaceholderVisible ? getPlaceholderItemCount() : getItemCount(children);
    }

    private int getItemCountWithoutFooter() {
        return getBodyItemCount() + getHeaderItemCount();
    }

    private int getHeaderCount() {
        return header == null || !isHeaderAndFooterVisible ? 0 : 1;
    }

    private int getHeaderItemCount() {
        return getHeaderCount() == 0 ? 0 : header.getItemCount();
    }

    private int getFooterItemCount() {
        return getFooterCount() == 0 ? 0 : footer.getItemCount();
    }

    private int getFooterCount() {
        return footer == null || !isHeaderAndFooterVisible ? 0 : 1;
    }

    private int getPlaceholderCount() {
        return isPlaceholderVisible ? 1 : 0;
    }

    @Override
    public Group getGroup(int position) {
        if (isHeaderShown() && position == 0) return header;
        position -= getHeaderCount();
        if (isPlaceholderShown() && position == 0) return placeholder;
        position -= getPlaceholderCount();
        if (position == children.size()) {
            if (isFooterShown()) {
                return footer;
            } else {
                return null;
            }
        } else {
            return children.get(position);
        }
    }

    @Override
    public int getGroupCount() {
        return getHeaderCount() + getFooterCount() + getPlaceholderCount() + children.size();
    }

    @Override
    public int getPosition(Group group) {
        int count = 0;
        if (isHeaderShown()) {
            if (group == header) return count;
        }
        count += getHeaderCount();
        if (isPlaceholderShown()) {
            if (group == placeholder) return count;
        }
        count += getPlaceholderCount();

        int index = children.indexOf(group);
        if (index >= 0) return count + index;
        count += children.size();

        if (isFooterShown()) {
            if (footer == group) {
                return count;
            }
        }

        return -1;
    }

    private boolean isHeaderShown() {
        return getHeaderCount() > 0;
    }

    private boolean isFooterShown() {
        return getFooterCount() > 0;
    }

    private boolean isPlaceholderShown() {
        return getPlaceholderCount() > 0;
    }

    public void setHeader(@NonNull Group header) {
        if (header == null)
            throw new NullPointerException("Header can't be null.  Please use removeHeader() instead!");
        int previousHeaderItemCount = getHeaderItemCount();
        this.header = header;
        notifyHeaderItemsChanged(previousHeaderItemCount);
    }

    public void removeHeader() {
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
        if (footer == null)
            throw new NullPointerException("Footer can't be null.  Please use removeFooter() instead!");
        int previousFooterItemCount = getFooterItemCount();
        this.footer = footer;
        notifyFooterItemsChanged(previousFooterItemCount);
    }

    public void removeFooter() {
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
        hideWhenEmpty = hide;
        refreshEmptyState();
    }

    @Override
    public void onItemInserted(Group group, int position) {
        super.onItemInserted(group, position);
        refreshEmptyState();
    }

    @Override
    public void onItemRemoved(Group group, int position) {
        super.onItemRemoved(group, position);
        refreshEmptyState();
    }

    @Override
    public void onItemRangeInserted(Group group, int positionStart, int itemCount) {
        super.onItemRangeInserted(group, positionStart, itemCount);
        refreshEmptyState();
    }

    @Override
    public void onItemRangeRemoved(Group group, int positionStart, int itemCount) {
        super.onItemRangeRemoved(group, positionStart, itemCount);
        refreshEmptyState();
    }

    private int getPlaceholderItemCount() {
        if (isPlaceholderVisible && placeholder != null) {
            return placeholder.getItemCount();
        }
        return 0;
    }
}
