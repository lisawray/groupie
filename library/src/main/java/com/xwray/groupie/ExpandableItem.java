package com.xwray.groupie;

import androidx.annotation.NonNull;

/**
 * The "collapsed"/header item of an expanded group.  Some part (or all) of it is a "toggle" to
 * expand the group.
 *
 * Collapsed:
 * - This
 *
 * Expanded:
 * - This
 *   - Child
 *   - Child
 *   - etc
 *
 */
public interface ExpandableItem {
    void setExpandableGroup(@NonNull ExpandableGroup onToggleListener);
}
