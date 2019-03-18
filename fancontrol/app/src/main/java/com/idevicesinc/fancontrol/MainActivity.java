package com.idevicesinc.fancontrol;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_THEME_ID = "com.idevicesinc.fancontrol.THEME_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView ocean = (ImageView) findViewById(R.id.ocean_button);
        ocean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSettingsActivity(R.id.ocean_button);
            }
        });
        ImageView mountains = (ImageView) findViewById(R.id.mountains_button);
        mountains.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSettingsActivity(R.id.mountains_button);
            }
        });
        ImageView wind = (ImageView) findViewById(R.id.wind_button);
        wind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSettingsActivity(R.id.wind_button);
            }
        });
    }

    void startSettingsActivity(int id) {
        Intent intent = new Intent(this, SettingsActivity.class);
        intent.putExtra(EXTRA_THEME_ID, id);
        startActivity(intent);
    }
}
