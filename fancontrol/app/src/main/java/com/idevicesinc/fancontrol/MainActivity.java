package com.idevicesinc.fancontrol;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import static com.idevicesinc.fancontrol.R.drawable.ocean;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_THEME_ID = "com.idevicesinc.fancontrol.THEME_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSettingsActivity(v.getId());
            }
        };
        View.OnLongClickListener longClickListener = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                startSettingsActivity(v.getId());
                return true;
            }
        };

        ImageView ocean = (ImageView) findViewById(R.id.ocean_button);
        ocean.setOnClickListener(clickListener);
        ocean.setOnLongClickListener(longClickListener);
        ImageView mountains = (ImageView) findViewById(R.id.mountains_button);
        mountains.setOnClickListener(clickListener);
        mountains.setOnLongClickListener(longClickListener);
        ImageView wind = (ImageView) findViewById(R.id.wind_button);
        wind.setOnClickListener(clickListener);
        wind.setOnLongClickListener(longClickListener);
    }

    void startSettingsActivity(int id) {
        Intent intent = new Intent(this, SettingsActivity.class);
        intent.putExtra(EXTRA_THEME_ID, id);
        startActivity(intent);
    }
}
