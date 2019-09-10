package com.wray.groupiekotlin

import android.util.Log

/**
 * The "collapsed"/header item of an expanded group.  Some part (or all) of it is a "toggle" to
 * expand the group.
 *
 * Collapsed:
 * - This
 *
 * Expanded:
 * - This
 * - Child
 * - Child
 * - etc
 *
 */
interface ExpandableItem: Group {
    fun setExpandableGroup(expandableGroup: ExpandableGroup)
}

/**
 * An `ExpandableGroup` is one "base" content item with a list of children (any of which
 * may themselves be a group.)
 **/

open class ExpandableGroup(
    private val parent: ExpandableItem,
    isExpanded: Boolean = false
): NestedGroup() {

    var isExpanded: Boolean = isExpanded
        set(value) {
            if (field != value) {
                onToggleExpanded()
            }
            field = value
        }

    private val children = mutableListOf<Group>()

    init {
        parent.setExpandableGroup(this)
    }

    override fun add(group: Group) {
        super.add(group)
        if (isExpanded) {
            val itemCount = itemCount
            children.add(group)
            notifyItemRangeInserted(itemCount, group.itemCount)
        }
        children.add(group)
    }

    override fun add(position: Int, group: Group) {
        super.add(position, group)
        children.add(position, group)
        if (isExpanded) {
            val notifyPosition = 1 + children.subList(0, position).itemCount
            notifyItemRangeInserted(notifyPosition, group.itemCount)
        }
    }

    override fun <T : Group> addAll(groups: Collection<T>) {
        if (groups.isEmpty()) return

        super.addAll(groups)

        if (isExpanded) {
            val itemCount = itemCount
            children.addAll(groups)
            notifyItemRangeInserted(itemCount, groups.itemCount)
        } else {
            this.children.addAll(groups)
        }
    }

    override fun <T : Group> addAll(position: Int, groups: Collection<T>) {
        if (groups.isEmpty()) {
            return
        }
        super.addAll(position, groups)
        this.children.addAll(position, groups)

        if (isExpanded) {
            val notifyPosition = 1 + children.subList(0, position).itemCount
            notifyItemRangeInserted(notifyPosition, groups.itemCount)
        }
    }

    override fun remove(group: Group) {
        if (!children.contains(group)) {
            Log.w(ExpandableGroup::class.java.simpleName, "Attempting to remove child from ExpandableGroup which does not contain it.")
            return
        }
        super.remove(group)

        if (isExpanded) {
            val position = getItemCountBeforeGroup(group)
            children.remove(group)
            notifyItemRangeRemoved(position, group.itemCount)
        } else {
            children.remove(group)
        }
    }

    override fun <T : Group> removeAll(groups: Collection<T>) {
        if (groups.isEmpty() || !this.children.containsAll(groups)) {
            Log.w(ExpandableGroup::class.java.simpleName, "Attempting to remove children from ExpandableGroup which does not contain them.")
            return
        }
        super.removeAll(groups)

        if (isExpanded) {
            this.children.removeAll(groups)
            groups.forEach { group ->
                val position = getItemCountBeforeGroup(group)
                children.remove(group)
                notifyItemRangeRemoved(position, group.itemCount)
            }
        } else {
            this.children.removeAll(groups)
        }
    }

    val childCount: Int
        get() = children.size

    fun onToggleExpanded() {
        val oldSize = itemCount
        isExpanded = !isExpanded
        val newSize = itemCount
        if (oldSize > newSize) {
            notifyItemRangeRemoved(newSize, oldSize - newSize)
        } else {
            notifyItemRangeInserted(oldSize, newSize - oldSize)
        }
    }

    private val Group.canDispatchChanges: Boolean
        get() = isExpanded || this === parent

    //region: NestedGroup methods

    override fun getGroup(position: Int): Group =
        if (position == 0) {
            parent
        } else {
            children[position - 1]
        }

    override val groupCount: Int
        get() = 1 + if (isExpanded) children.size else 0

    override fun getPosition(group: Group): Int {
        if (group === parent) {
            return 0
        }
        val index = children.indexOf(group)
        return if (index >= 0) {
            index + 1
        } else -1
    }

    override fun onChanged(group: Group) {
        if (group.canDispatchChanges) {
            super.onChanged(group)
        }
    }

    override fun onItemInserted(group: Group, position: Int) {
        if (group.canDispatchChanges) {
            super.onItemInserted(group, position)
        }
    }

    override fun onItemChanged(group: Group, position: Int) {
        if (group.canDispatchChanges) {
            super.onItemChanged(group, position)
        }
    }

    override fun onItemChanged(group: Group, position: Int, payload: Any) {
        if (group.canDispatchChanges) {
            super.onItemChanged(group, position, payload)
        }
    }

    override fun onItemRemoved(group: Group, position: Int) {
        if (group.canDispatchChanges) {
            super.onItemRemoved(group, position)
        }
    }

    override fun onItemRangeChanged(group: Group, positionStart: Int, itemCount: Int) {
        if (group.canDispatchChanges) {
            super.onItemRangeChanged(group, positionStart, itemCount)
        }
    }

    override fun onItemRangeChanged(group: Group, positionStart: Int, itemCount: Int, payload: Any) {
        if (group.canDispatchChanges) {
            super.onItemRangeChanged(group, positionStart, itemCount, payload)
        }
    }

    override fun onItemRangeInserted(group: Group, positionStart: Int, itemCount: Int) {
        if (group.canDispatchChanges) {
            super.onItemRangeInserted(group, positionStart, itemCount)
        }
    }

    override fun onItemRangeRemoved(group: Group, positionStart: Int, itemCount: Int) {
        if (group.canDispatchChanges) {
            super.onItemRangeRemoved(group, positionStart, itemCount)
        }
    }

    override fun onItemMoved(group: Group, fromPosition: Int, toPosition: Int) {
        if (group.canDispatchChanges) {
            super.onItemMoved(group, fromPosition, toPosition)
        }
    }

    //endregion: NestedGroup methods
}