package com.xwray.groupie.groupiex

import com.xwray.groupie.Group
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder

operator fun GroupAdapter<GroupieViewHolder>.plusAssign(element: Group) = this.add(element)


operator fun GroupAdapter<GroupieViewHolder>.plusAssign(groups: Collection<Group>) = this.addAll(groups)


operator fun GroupAdapter<GroupieViewHolder>.minusAssign(element: Group) = this.remove(element)


operator fun GroupAdapter<GroupieViewHolder>.minusAssign(groups: Collection<Group>)  = this.removeAll(groups)

