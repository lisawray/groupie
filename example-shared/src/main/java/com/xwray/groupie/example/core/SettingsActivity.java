package com.xwray.groupie.example.core;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        final Prefs prefs = Prefs.get(this);

        ViewGroup showBoundsContainer = (ViewGroup) findViewById(R.id.show_bounds);
        SwitchCompat showBoundsSwitch = (SwitchCompat) showBoundsContainer.findViewById(R.id.the_switch);
        TextView showBoundsText = (TextView) showBoundsContainer.findViewById(R.id.text);

        ViewGroup showOffsetsContainer = (ViewGroup) findViewById(R.id.show_offsets);
        SwitchCompat showOffsetsSwitch =  (SwitchCompat) showOffsetsContainer.findViewById(R.id.the_switch);
        TextView showOffsetsText = (TextView) showOffsetsContainer.findViewById(R.id.text);

        showBoundsText.setText(R.string.show_bounds);
        showOffsetsText.setText(R.string.show_offsets);
        showBoundsSwitch.setChecked(prefs.getShowBounds());
        showOffsetsSwitch.setChecked(prefs.getShowOffsets());

        showBoundsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean showBounds) {
                prefs.setShowBounds(showBounds);
            }
        });

        showOffsetsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean showOffsets) {
                prefs.setShowOffsets(showOffsets);
            }
        });
    }
}
