package com.xwray.groupie.example;

import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.xwray.groupie.example.R;
import com.xwray.groupie.ExpandableGroup;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.Item;
import com.xwray.groupie.OnItemClickListener;
import com.xwray.groupie.Section;
import com.xwray.groupie.TouchCallback;
import com.xwray.groupie.UpdatingGroup;
import com.xwray.groupie.example.databinding.ActivityMainBinding;
import com.xwray.groupie.example.decoration.CarouselItemDecoration;
import com.xwray.groupie.example.decoration.DebugItemDecoration;
import com.xwray.groupie.example.decoration.HeaderItemDecoration;
import com.xwray.groupie.example.decoration.InsetItemDecoration;
import com.xwray.groupie.example.decoration.SwipeTouchCallback;
import com.xwray.groupie.example.item.CardItem;
import com.xwray.groupie.example.item.CarouselCardItem;
import com.xwray.groupie.example.item.CarouselItem;
import com.xwray.groupie.example.item.ColumnItem;
import com.xwray.groupie.example.item.FullBleedCardItem;
import com.xwray.groupie.example.item.HeaderItem;
import com.xwray.groupie.example.item.HeartCardItem;
import com.xwray.groupie.example.item.SmallCardItem;
import com.xwray.groupie.example.item.SwipeToDeleteItem;
import com.xwray.groupie.example.item.UpdatableItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String INSET_TYPE_KEY = "inset_type";
    public static final String FULL_BLEED = "full_bleed";
    public static final String INSET = "inset";

    private ActivityMainBinding binding;
    private StickyGroupAdapter groupAdapter;
    private GridLayoutManager layoutManager;
    private Prefs prefs;

    private int gray;
    private int betweenPadding;
    private int[] rainbow200;
    private int[] rainbow500;

    private Section infiniteLoadingSection;
    private Section swipeSection;

    // Normally there's no need to hold onto a reference to this list, but for demonstration
    // purposes, we'll shuffle this list and post an update periodically
    private ArrayList<UpdatableItem> updatableItems;

    // Hold a reference to the updating group, so we can, well, update it
    private UpdatingGroup updatingGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        prefs = Prefs.get(this);

        gray = ContextCompat.getColor(this, R.color.background);
        betweenPadding = getResources().getDimensionPixelSize(R.dimen.padding_small);
        rainbow200 = getResources().getIntArray(R.array.rainbow_200);
        rainbow500 = getResources().getIntArray(R.array.rainbow_500);

        groupAdapter = new StickyGroupAdapter(R.layout.item_header, R.layout.item_expandable_header);
        groupAdapter.setOnItemClickListener(onItemClickListener);
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
            @Override public void onLoadMore(int currentPage) {
                int color = rainbow200[currentPage % rainbow200.length];
                for (int i = 0; i < 5; i++) {
                    infiniteLoadingSection.add(new CardItem(color));
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
        updatingGroup = new UpdatingGroup();
        updatableItems = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            updatableItems.add(new UpdatableItem(rainbow200[i], i));
        }
        updatingGroup.update(updatableItems);
        updatingSection.add(updatingGroup);
        groupAdapter.add(updatingSection);

        // Expandable group
        ExpandableHeaderItem expandableHeaderItem = new ExpandableHeaderItem(R.string.expanding_group, R.string.expanding_group_subtitle);
        ExpandableGroup expandableGroup = new ExpandableGroup(expandableHeaderItem);
        for (int i = 0; i < 2; i++) {
            expandableGroup.add(new CardItem(rainbow200[1]));
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
            multipleColumnsSection.add(new SmallCardItem(rainbow200[5]));
        }
        groupAdapter.add(multipleColumnsSection);

        // Swipe to delete (with add button in header)
        swipeSection = new Section(new HeaderItem(R.string.swipe_to_delete));
        for (int i = 0; i < 3; i++) {
            swipeSection.add(new SwipeToDeleteItem(rainbow200[6]));
        }
        groupAdapter.add(swipeSection);

        // Horizontal carousel
        Section carouselSection = new Section(new HeaderItem(R.string.carousel, R.string.carousel_subtitle));
        CarouselItem carouselItem = makeCarouselItem();
        carouselSection.add(carouselItem);
        groupAdapter.add(carouselSection);

        // Update with payload
        Section updateWithPayloadSection = new Section(new HeaderItem(R.string.update_with_payload, R.string.update_with_payload_subtitle));
        for (int i = 0; i < rainbow500.length; i++) {
            updateWithPayloadSection.add(new HeartCardItem(rainbow200[i], i, onFavoriteListener));

        }
        groupAdapter.add(updateWithPayloadSection);

        // Infinite loading section
        infiniteLoadingSection = new Section(new HeaderItem(R.string.infinite_loading));
        groupAdapter.add(infiniteLoadingSection);
    }

    private ColumnGroup makeColumnGroup() {
        List<ColumnItem> columnItems = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            // First five items are red -- they'll end up in a vertical column
            columnItems.add(new ColumnItem(rainbow200[0], i));
        }
        for (int i = 6; i <= 10; i++) {
            // Next five items are pink
            columnItems.add(new ColumnItem(rainbow200[1], i));
        }
        return new ColumnGroup(columnItems);
    }

    private CarouselItem makeCarouselItem() {
        CarouselItemDecoration carouselDecoration = new CarouselItemDecoration(gray, betweenPadding);
        GroupAdapter carouselAdapter = new GroupAdapter();
        for (int i = 0; i < 30; i++) {
            carouselAdapter.add(new CarouselCardItem(rainbow200[7]));
        }
        CarouselItem carouselItem = new CarouselItem(carouselDecoration);
        carouselItem.setAdapter(carouselAdapter);
        return carouselItem;
    }

    private OnItemClickListener onItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(Item item, View view) {
            if (item instanceof CardItem) {
                CardItem cardItem = (CardItem) item;
                if (!TextUtils.isEmpty(cardItem.getText())) {
                    Toast.makeText(MainActivity.this, cardItem.getText(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

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

    private Handler handler = new Handler();
    private HeartCardItem.OnFavoriteListener onFavoriteListener = new HeartCardItem.OnFavoriteListener() {
        @Override
        public void onFavorite(final HeartCardItem item, final boolean favorite) {
            // Pretend to make a network request
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Network request was successful!
                    item.setFavorite(favorite);
                    item.notifyChanged(HeartCardItem.FAVORITE);
                }
            }, 1000);
        }
    };

}
