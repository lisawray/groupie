# groupie [alpha]

Groupie makes it simple to display logical "groups" of content, like sections with headers and footers, expandable groups, blocks of vertical columns, and much more.  Define your content the same way you think about it, and Groupie handles indices, changes, and notifications for you.

[gif placeholder]

    GroupAdapter groupAdapter = new GroupAdapter();
    
    Section section = new Section();
    section.addAll(bodyItems);
    section.setHeader(new HeaderItem());
    
    groupAdapter.add(section);

## Multiple view types made easy

Groupie abstracts the complexity of multiple item view types.  The smallest building block is an `Item`. 

Each Item declares a view layout id, and gets a callback to `bind` the inflated layout.  That's all you need; you can add your new item directly to a `GroupAdapter` and call it a day.  

## Groups are composable and flexible

The second fundamental part of Groupie is the `Group`.  A Group is a collection of 0 or more Items.  Add it directly to the GroupAdapter along with any other Items or Groups.  (An Item is a Group of 1!)  

When you edit the contents of a Group, the GroupAdapter uses the notification from the group, as well as its knowledge of the other groups it contains, to dispatch correct change notifications and trigger animations.  There's no need to remember to manually notify or keep track of indices.

    section.removeItem(item); // Change notification will be dispatched, and the change animated.

We've provided a few simple implementations of Groups within the library:
- `Section`, a list of body content with an optional header and footer.  
- `ExpandableGroup`, a single parent item with a list of body content that can be toggled hidden or shown.
- `UpdatingGroup`, a list of content which can diff its previous and new contents and animate moves, updates and other changes intelligently

Groupie is flexible because we found that our groups were rarely exactly the same.  We think you should make many small, simple, custom groups as the need strikes you.  Groups are designed to be nested; for example, you could make an UpdatingSection by adding a single UpdatingGroup to the content of a Section.  

You can implement the Group interface directly if you want.  However, we've provided NestedGroup as a base implementation, and in most cases, we strongly recommend extending it.  NestedGroup provides support for arbitrary nesting of groups, registering/unregistering listeners, and fine-grained change notifications to support animations and updating the adapter. 

## Responsive, interactive layouts
Items can also declare their own column span and whether they are draggable or swipeable.  

# groupie is currently in alpha.

Groupie was developed as an internal library at Genius because none of the existing solutions for multiple view types or groups were simple or flexible enough for us.  We decided to make it public at this early stage in order to encourage discussion about managing and developing complex, rich user interfaces with RecyclerView. 

## Limitations
- groupie is not tested (well)
- groupie requires you to use data binding. (for now)
- groupie is designed to work with a GridLayoutManager.
- groupie's API will definitely change.

Groupie is available on jcenter as an alpha.  If you try it out, we'd love to know what you think.  Please hit up Lisa at lisa@genius.com.

