package com.xwray.groupie.groupiex

import com.wray.groupiekotlin.Group
import com.wray.groupiekotlin.GroupAdapter
import com.wray.groupiekotlin.GroupieViewHolder

operator fun <VH: GroupieViewHolder> GroupAdapter<VH>.plusAssign(element: Group) = this.add(element)

operator fun <VH: GroupieViewHolder> GroupAdapter<VH>.plusAssign(groups: Collection<Group>) = this.addAll(groups)

operator fun <VH: GroupieViewHolder> GroupAdapter<VH>.minusAssign(element: Group) = this.remove(element)

operator fun <VH: GroupieViewHolder> GroupAdapter<VH>.minusAssign(groups: Collection<Group>)  = this.removeAll(groups)

