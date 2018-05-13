package com.xwray.groupie.groupiex

import com.xwray.groupie.Group
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.ViewHolder

operator fun Section.plusAssign(element: Group)  = this.add(element)


operator fun Section.plusAssign(groups: MutableCollection<out Group>) = this.addAll(groups)


operator fun Section.minusAssign(element: Group) = this.remove(element)


operator fun Section.minusAssign(groups: MutableCollection<out Group>) = this.removeAll(groups)


fun Section() {
    val groupAdapter = GroupAdapter<ViewHolder>()
    groupAdapter += Section()
    groupAdapter += mutableListOf(Section(), Section())
}