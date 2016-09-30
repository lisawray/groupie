# groupie

Groupie helps you display and manage complex RecyclerView layouts.  It lets you treat your content as logical groups and handles change notifications for you -- think sections with headers and footers, expandable groups, blocks of vertical columns, and much more.  It makes it easy to handle asynchronous content updates and insertions and user-driven content changes.  At the item level, it abstracts the boilerplate of item view types, item layouts, viewholders, and span sizes.  

Groupie was developed as an internal library at Genius because none of the existing solutions for multiple view types or groups were simple or flexible enough for us.  We decided to make it public at this early stage in order to encourage discussion about managing and developing complex, rich user interfaces with RecyclerView. 
 

<img src="http://i.imgur.com/eftOE0v.gif" width="300px"/>

# Try it out:

Groupie is available on jcenter as an alpha version:

```gradle
compile 'com.genius:groupie:0.1.0'
```
    
## Setup

Use a `GroupAdapter` anywhere you would normally use a `RecyclerView.Adapter`, and attach it to your RecyclerView as usual.

```java
GroupAdapter adapter = new GroupAdapter();
recyclerView.setAdapter(adapter);
```
    
## Groups

Groups are the building block of Groupie.  An individual `Item` (the unit which an adapter inflates and recycles) is a Group of 1.  You can add Groups and Items interchangeably to the adapter.

```java
groupAdapter.add(new HeaderItem());
groupAdapter.add(new CommentItem());

Section section = new Section();
section.setHeader(new HeaderItem());
section.addAll(bodyItems);
groupAdapter.add(section);
```
    
Modifying the contents of the GroupAdapter in any way automatically sends change notifications.  Adding an item calls `notifyItemAdded()`; adding a group calls `notifyItemRangeAdded()`, etc.

Modifying the contents of a Group automatically notifies its parent.  When notifications reach the GroupAdapter, it dispatches final change notifications.  There's never a need to manually notify or keep track of indices, no matter how you structure your data.

```java
section.setHeader(null); // results in a remove event for header and move event for all following items
```
    
We've provided a few simple implementations of Groups within the library:
- `Section`, a list of body content with an optional header group and footer group.  
- `ExpandableGroup`, a single parent group with a list of body content that can be toggled hidden or shown.
- `UpdatingGroup`, a list of items which can diff its previous and new contents and animate moves, updates and other changes 
    
Groups are flexible and composable.  They can be combined and nested to arbitrary depth.  For example, you could make an UpdatingSection by adding a single UpdatingGroup to the content of a Section. 

```java
public class UpdatingSection extends Section {
    private final UpdatingGroup updatingGroup;

    public UpdatingSection() {
        setHeader(new HeaderItem("Updating section!");
        updatingGroup = new UpdatingGroup();
    }

    public void update(List<Item> list) {
        updatingGroup.update(list);
    }
}
```
    
At Genius, we found that our groups were rarely exactly the same.  Groups are designed so that making new ones and defining their behavior is easy.  We think you should make many small, simple, custom groups as the need strikes you.

You can implement the Group interface directly if you want.  However, we've provided NestedGroup as a base implementation, and in most cases, we strongly recommend extending it.  NestedGroup provides support for arbitrary nesting of groups, registering/unregistering listeners, and fine-grained change notifications to support animations and updating the adapter.
    
## Items

Groupie abstracts the complexity of multiple item view types.  Each Item declares a view layout id, and gets a callback to `bind` the inflated layout.  That's all you need; you can add your new item directly to a `GroupAdapter` and call it a day.

```java
public class SongItem extends Item<SongBinding> {

    public SongItem(Song song) {
        this(song);
    }    

    @Override public void bind(SongBinding binding, int position) {
        binding.setSong(song);
    }

    @Override public int getLayout() {
        return R.layout.song;
    }
}

```

Items can also declare their own column span and whether they are draggable or swipeable.  

# Limitations
- groupie is not tested (well)
- groupie requires you to use data binding. (for now)
- groupie's API will definitely change.

If you try it out, we'd love to know what you think! Please hit up Lisa at lisa@genius.com.
