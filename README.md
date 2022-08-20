# groupie

Groupie is a simple, flexible library for complex RecyclerView layouts.  

Groupie lets you treat your content as logical groups and handles change notifications for you -- think sections with headers and footers, expandable groups, blocks of vertical columns, and much more.  It makes it easy to handle asynchronous content updates and insertions and user-driven content changes.  At the item level, it abstracts the boilerplate of item view types, item layouts, viewholders, and span sizes.  
 
<img src="http://i.imgur.com/eftOE0v.gif" width="300px"/>

# Try it out:

```gradle
implementation "com.github.lisawray.groupie:groupie:$groupie_version"
```

Groupie also has a support module for Android's [view binding](https://developer.android.com/topic/libraries/view-binding). This module also supports Android [data binding](https://developer.android.com/topic/libraries/data-binding/index.html), so if your project uses both data binding and view binding, you don't have to add the dependency on the data binding support module. [Setup here.](#view-binding)

```gradle
implementation "com.github.lisawray.groupie:groupie:$groupie_version"
implementation "com.github.lisawray.groupie:groupie-viewbinding:$groupie_version" 
```

### Note:
If using `groupie-viewbinding` in a databinding project is only available when using Android Gradle Plugin 3.6.0 or higher.

If using an older Gradle Plugin version with databinding the you can use the standalone `groupie-databinding` library to generate view holders. [Setup here.](#data-binding)

```gradle
implementation "com.github.lisawray.groupie:groupie:$groupie_version"
implementation "com.github.lisawray.groupie:groupie-databinding:$groupie_version" 
```

You can also use Groupie with Java and your existing ViewHolders. 

Which one to choose?  It's up to you and what your project already uses. You can even use Kotlin and data binding together.[<sup>*</sup>](#kotlin-and-data-binding) Or all your existing hand-written Java ViewHolders, and one new Kotlin item to try it out. Go crazy!  
    
## Get started

Use a `GroupieAdapter` anywhere you would normally use a `RecyclerView.Adapter`, and attach it to your RecyclerView as usual.

Kotlin
```kotlin
val adapter = GroupieAdapter()
recyclerView.adapter = adapter
```

Java
```java
GroupieAdapter adapter = new GroupieAdapter();
recyclerView.setAdapter(adapter);
```
    
## Groups

Groups are the building block of Groupie.  An individual `Item` (the unit which an adapter inflates and recycles) is a Group of 1.  You can add Groups and Items interchangeably to the adapter.

Kotlin
```kotlin
groupAdapter += HeaderItem()
groupAdapter += CommentItem()

val section = Section()
section.setHeader(HeaderItem())
section.addAll(bodyItems)
groupAdapter += section
```

Java
```java
groupAdapter.add(new HeaderItem());
groupAdapter.add(new CommentItem());

Section section = new Section();
section.setHeader(new HeaderItem());
section.addAll(bodyItems);
groupAdapter.add(section);
```
    
Modifying the contents of the GroupieAdapter in any way automatically sends change notifications.  Adding an item calls `notifyItemAdded()`; adding a group calls `notifyItemRangeAdded()`, etc.

Modifying the contents of a Group automatically notifies its parent.  When notifications reach the GroupieAdapter, it dispatches final change notifications.  There's never a need to manually notify or keep track of indices, no matter how you structure your data.

```java
section.removeHeader(); // results in a remove event for 1 item in the adapter, at position 2
```
    
There are a few simple implementations of Groups within the library:
- `Section`, a list of body content with an optional header group and footer group.  It supports diffing and animating moves, updates and other changes
- `ExpandableGroup`, a single parent group with a list of body content that can be toggled hidden or shown.
    
Groupie tries not to assume what features your groups require.  Instead, groups are flexible and composable.  They can be combined and nested to arbitrary depth.  
    
Life (and mobile design) is complicated, so groups are designed so that making new ones and defining their behavior is easy. You should make many small, simple, custom groups as the need strikes you.

You can implement the `Group` interface directly if you want.  However, in most cases, you can extend `Section` or the base implementation, `NestedGroup`.  Section supports common RV paradigms like diffing, headers, footers, and placeholders.  NestedGroup provides support for arbitrary nesting of groups, registering/unregistering listeners, and fine-grained change notifications to support animations and updating the adapter.
    
## Items

Groupie abstracts away the complexity of multiple item view types.  Each Item declares a view layout id, and gets a callback to `bind` the inflated layout.  That's all you need; you can add your new item directly to a `GroupieAdapter` and call it a day.

### Item with Kotlin-Android-Extensions:

**Note: `kotlin-android-extensions` is deprecated since Kotlin 1.4.20. Therefore, `groupie-kotlin-android-extensions` is also deprecated, and `viewbinding` should be preferred.**

Groupie includes a module for Kotlin and Kotlin Android extensions. [Setup here.](#kotlin)

```gradle
implementation "com.github.lisawray.groupie:groupie:$groupie_version"
implementation "com.github.lisawray.groupie:groupie-kotlin-android-extensions:$groupie_version"
```

The `Item` class gives you simple callbacks to bind your model object to the generated fields.

```kotlin
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import kotlinx.android.synthetic.main.song.*

class SongItem(private val song: Song) : Item() {

    override fun getLayout() = R.layout.song

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.title.text = song.title
        viewHolder.artist.text = song.artist
    }
}
```

### Item with data binding:

The `Item` class gives you simple callbacks to bind your model object to the generated binding.  Because of data binding, there's no need to write a view holder.  

```java
public class SongItem extends BindableItem<SongBinding> {

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

If you're converting existing ViewHolders, you can reference any named views (e.g. `R.id.title`) directly from the binding instead. 
```java
    @Override public void bind(SongBinding binding, int position) {
        binding.title.setText(song.getTitle());
    }
```

You can also mix and match `BindableItem` and other `Items` in the adapter, so you can leave legacy viewholders as they are by making an `Item<MyExistingViewHolder>`.  

### Legacy item (your own ViewHolder)
You can leave legacy viewholders as they are by converting `MyExistingViewHolder` to extend `GroupieViewHolder` rather than `RecyclerView.ViewHolder`. Make sure to change the imports to `com.xwray.groupie.Item` and `com.xwray.groupie.GroupieViewHolder`.

Finally, in your `Item<MyExistingViewHolder>`, override 

```java
    @Override
    public MyExistingViewHolder createViewHolder(@NonNull View itemView) {
        return new MyExistingViewHolder(itemView);
    }
```

### Note: 

Items can also declare their own column span and whether they are draggable or swipeable.  

# Gradle setup

## Kotlin

In your project level `build.gradle` file, include:

```
buildscript {
    ext.kotlin_version = '1.6.21'

    repositories {
        mavenCentral()
    }

    dependencies {
        classpath "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version"
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven { url "https://jitpack.io" }
    }
}
```

In new projects, the `settings.gradle` file has a `dependencyResolutionManagement` block, which needs to specify the repository as well:

```
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url 'https://jitpack.io' }  // <--
        jcenter() // Warning: this repository is going to shut down soon
    }
}
```

In your app `build.gradle` file, include:

```
implementation 'com.github.lisawray.groupie:groupie:$groupie_version'
```

### Using with Kotlin-Android-Extensions (deprecated)

If you are still relying on the deprecated `kotlin-android-extensions` module, then also apply the following:

```
apply plugin: 'kotlin-android'
apply plugin: 'kotlin-android-extensions'

android {
  ....
  
   // IMPORTANT!  Enables kotlin synthetic view properties.
   // See: https://github.com/Kotlin/KEEP/blob/master/proposals/android-extensions-entity-caching.md
    androidExtensions {
        experimental = true
    }
	
}

dependencies {
    implementation 'com.github.lisawray.groupie:groupie:$groupie_version'
    implementation 'com.github.lisawray.groupie:groupie-kotlin-android-extensions:$groupie_version'
}
```

Remember to include 
```kotlin
import kotlinx.android.synthetic.main.my_item_layout.*
```
in the corresponding Item class for generated view references.

## View binding

Add to your app module's `build.gradle`:

```gradle
android {
    buildFeatures {
        viewBinding true
    }
}

dependencies {
    implementation "com.github.lisawray.groupie:groupie:$groupie_version"
    implementation "com.github.lisawray.groupie:groupie-viewbinding:$groupie_version"
}
```

Then:

```kotlin
class MyLayoutItem: BindableItem<MyLayoutBinding>() {
    override fun initializeViewBinding(view: View): MyLayoutBinding {
        return MyLayoutBinding.bind(view)
    }

    // Other implementations...
}
```

### Note:

If you use `groupie-databinding` with data binding classes and your layouts have some variables or [observable objects](https://developer.android.com/topic/libraries/data-binding/observability), don't forget to run [`executePendingBindings`](https://developer.android.com/topic/libraries/data-binding/generated-binding#immediate_binding) at the last point in `bind`.

## Data binding

Add to your app module's build.gradle:

```gradle
android {
    buildFeatures {
        dataBinding true
    }
}

dependencies {
    implementation "com.github.lisawray.groupie:groupie:$groupie_version"
    implementation "com.github.lisawray.groupie:groupie-databinding:$groupie_version"
}
```

Then, just wrap each item layout in `<layout>` tags.  (The `<data>` section is optional.)  

`layout/item_song.xml`
```xml
<layout xmlns:android="http://schemas.android.com/apk/res/android" 
    xmlns:tools="http://schemas.android.com/tools">
    <data>
        <variable name="song" type="com.example.Song" />
    </data>

    <FrameLayout 
        android:layout_width="match_parent"
        android:layout_height="wrap_content" >

        <TextView
            android:id="@+id/title"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_gravity="center"
            android:text="@{song.title}"
            tools:text="A Song Title" />

    </FrameLayout>
</layout>
```

Bindings are only generated for layouts wrapped with <layout/> tags, so there's no need to convert the rest of your project (unless you want to).

You can add a `<data>` section to directly bind a model or ViewModel, but you don't have to.  The generated view bindings alone are a huge time saver.

### Kotlin AND data binding / view binding?
Sure, why not?  Follow all the instructions from *both* sections above.  You only need to include the `groupie-databinding` or `groupie-viewbinding` dependency, and omit the references to `android-extensions`.  You'll make `BindableItem`s instead of importing and using Kotlin extensions.


# Contributing
Contributions you say?  Yes please!

### Bug report? 
- If at all possible, please attach a *minimal* sample project or code which reproduces the bug. 
- Screenshots are also a huge help if the problem is visual.
### Send a pull request!
- If you're fixing a bug, please add a failing test or code that can reproduce the issue.


If you try it out, I'd love to know what you think. Please hit up Lisa at [first][last]@gmail.com or on Twitter at [@lisawrayz](https://twitter.com/lisawrayz).
