package com.xwray.groupie.groupiex

import com.xwray.groupie.Group
import com.xwray.groupie.Section

operator fun Section.plusAssign(element: Group)  = this.add(element)


operator fun Section.plusAssign(groups: Collection<Group>) = this.addAll(groups)


operator fun Section.minusAssign(element: Group) = this.remove(element)


operator fun Section.minusAssign(groups: Collection<Group>) = this.removeAll(groups)