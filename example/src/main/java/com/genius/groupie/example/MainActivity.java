package com.genius.groupie.example;

import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.genius.groupie.ExpandableGroup;
import com.genius.groupie.GroupAdapter;
import com.genius.groupie.Item;
import com.genius.groupie.Section;
import com.genius.groupie.TouchCallback;
import com.genius.groupie.UpdatingGroup;
import com.genius.groupie.example.databinding.ActivityMainBinding;
import com.genius.groupie.example.decoration.CarouselItemDecoration;
import com.genius.groupie.example.decoration.DebugItemDecoration;
import com.genius.groupie.example.decoration.HeaderItemDecoration;
import com.genius.groupie.example.decoration.InsetItemDecoration;
import com.genius.groupie.example.decoration.SwipeTouchCallback;
import com.genius.groupie.example.item.CardItem;
import com.genius.groupie.example.item.CarouselCardItem;
import com.genius.groupie.example.item.CarouselItem;
import com.genius.groupie.example.item.ColumnItem;
import com.genius.groupie.example.item.FullBleedCardItem;
import com.genius.groupie.example.item.HeaderItem;
import com.genius.groupie.example.item.SmallCardItem;
import com.genius.groupie.example.item.SwipeToDeleteItem;
import com.genius.groupie.example.item.UpdatableItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String INSET_TYPE_KEY = "inset_type";
    public static final String FULL_BLEED = "full_bleed";
    public static final String INSET = "inset";

    private ActivityMainBinding binding;
    private GroupAdapter groupAdapter;
    private GridLayoutManager layoutManager;
    private Prefs prefs;

    private int gray;
    private int betweenPadding;

    private Section infiniteLoadingSection;
    private Section swipeSection;

    // Normally there's no need to hold onto a reference to this list, but for demonstration
    // purposes, we'll shuffle this list and post an update periodically
    private ArrayList<UpdatableItem> updatableItems;

    // Hold a reference to the updating group, so we can, well, update it
    private UpdatingGroup<UpdatableItem> updatingGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        prefs = Prefs.get(this);

        gray = ContextCompat.getColor(this, R.color.background);
        betweenPadding = getResources().getDimensionPixelSize(R.dimen.padding_small);

        groupAdapter = new GroupAdapter(this);
        groupAdapter.setSpanCount(12);
        populateAdapter();
        layoutManager = new GridLayoutManager(this, groupAdapter.getSpanCount());
        layoutManager.setSpanSizeLookup(groupAdapter.getSpanSizeLookup());

        final RecyclerView recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new HeaderItemDecoration(gray, betweenPadding));
        recyclerView.addItemDecoration(new InsetItemDecoration(gray, betweenPadding));
        recyclerView.addItemDecoration(new DebugItemDecoration(this));
        recyclerView.setAdapter(groupAdapter);
        recyclerView.addOnScrollListener(new InfiniteScrollListener(layoutManager) {
            @Override public void onLoadMore(int current_page) {
                for (int i = 0; i < 5; i++) {
                    infiniteLoadingSection.add(new CardItem(R.color.blue_200));
                }
            }
        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(touchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            }
        });

        prefs.registerListener(onSharedPrefChangeListener);
    }

    private void populateAdapter() {

        // Full bleed item
        Section fullBleedItemSection = new Section(new HeaderItem(R.string.full_bleed_item));
        fullBleedItemSection.add(new FullBleedCardItem(R.color.purple_200));
        groupAdapter.add(fullBleedItemSection);

        // Update in place group
        Section updatingSection = new Section();
        View.OnClickListener onShuffleClicked = new View.OnClickListener() {
            @Override public void onClick(View view) {
                List<UpdatableItem> shuffled = new ArrayList<>(updatableItems);
                Collections.shuffle(shuffled);
                updatingGroup.update(shuffled);

                // You can also do this by forcing a change with payload
                binding.recyclerView.post(new Runnable() {
                    @Override public void run() {
                        binding.recyclerView.invalidateItemDecorations();
                    }
                });
            }
        };
        HeaderItem updatingHeader = new HeaderItem(
                R.string.updating_group,
                R.string.updating_group_subtitle,
                R.drawable.shuffle,
                onShuffleClicked);
        updatingSection.setHeader(updatingHeader);
        updatingGroup = new UpdatingGroup<>();
        updatableItems = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            updatableItems.add(new UpdatableItem(R.color.blue_200, i));
        }
        updatingGroup.update(updatableItems);
        updatingSection.add(updatingGroup);
        groupAdapter.add(updatingSection);

        // Expandable group
        ExpandableHeaderItem expandableHeaderItem = new ExpandableHeaderItem(R.string.expanding_group, R.string.expanding_group_subtitle);
        ExpandableGroup expandableGroup = new ExpandableGroup(expandableHeaderItem);
        for (int i = 0; i < 2; i++) {
            expandableGroup.add(new CardItem(R.color.red_200));
        }
        groupAdapter.add(expandableGroup);

        // Columns
        Section columnSection = new Section(new HeaderItem(R.string.vertical_columns));
        ColumnGroup columnGroup = makeColumnGroup();
        columnSection.add(columnGroup);
        groupAdapter.add(columnSection);

        // Group showing even spacing with multiple columns
        Section multipleColumnsSection = new Section(new HeaderItem(R.string.multiple_columns));
        for (int i = 0; i < 12; i++) {
            multipleColumnsSection.add(new SmallCardItem(R.color.indigo_200));
        }
        groupAdapter.add(multipleColumnsSection);

        // Swipe to delete (with add button in header)
        swipeSection = new Section(new HeaderItem(R.string.swipe_to_delete));
        for (int i = 0; i < 3; i++) {
            swipeSection.add(new SwipeToDeleteItem(R.color.blue_200));
        }
        groupAdapter.add(swipeSection);

        // Horizontal carousel
        Section carouselSection = new Section(new HeaderItem(R.string.carousel, R.string.carousel_subtitle));
        CarouselItem carouselItem = makeCarouselItem();
        carouselSection.add(carouselItem);
        groupAdapter.add(carouselSection);

        // Infinite loading section
        infiniteLoadingSection = new Section(new HeaderItem(R.string.infinite_loading));
        groupAdapter.add(infiniteLoadingSection);





        // if I have time ... multiple column group (doesn't have hanging items / no widows)

        // if I have time ... proper drag & drop support
//        HeaderItem dragDropHeader = new HeaderItem(R.string.multiple_columns);
//        groupAdapter.add(dragDropHeader);
//        for (int i = 0; i < 12; i++) {
//            groupAdapter.add(new DragDropItem(R.color.indigo_200, i));
//        }

    }

    private ColumnGroup makeColumnGroup() {
        List<ColumnItem> columnItems = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            columnItems.add(new ColumnItem(R.color.pink_200, i));
        }
        return new ColumnGroup(columnItems);
    }

    private CarouselItem makeCarouselItem() {
        CarouselItemDecoration carouselDecoration = new CarouselItemDecoration(gray, betweenPadding);
        GroupAdapter carouselAdapter = new GroupAdapter(this);
        for (int i = 0; i < 30; i++) {
            carouselAdapter.add(new CarouselCardItem(R.color.deep_purple_200));
        }
        CarouselItem carouselItem = new CarouselItem(carouselDecoration);
        carouselItem.setAdapter(carouselAdapter);
        return carouselItem;
    }

    @Override public void onClick(View view) {

    }

    @Override protected void onDestroy() {
        prefs.unregisterListener(onSharedPrefChangeListener);
        super.onDestroy();
    }

    private TouchCallback touchCallback = new SwipeTouchCallback(gray) {
        @Override public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            Item item = groupAdapter.getItem(viewHolder.getAdapterPosition());
            // Change notification to the adapter happens automatically when the section is
            // changed.
            swipeSection.remove(item);
        }
    };

    private SharedPreferences.OnSharedPreferenceChangeListener onSharedPrefChangeListener =
            new SharedPreferences.OnSharedPreferenceChangeListener() {
                @Override public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
                    // This is pretty evil, try not to do this
                    groupAdapter.notifyDataSetChanged();
                }
            };

}
